package application;

import application.bullet.BulletColor;
import application.bullet.BulletGroup;
import application.bullet.BulletGroupComparator;
import application.bullet.BulletRenderThread;
import application.bullet.bulletAttr.LinMoveAttr;
import application.bullet.bulletTypes.Bullet;
import application.bullet.bulletAttr.BulletAttr;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.*;

public class Game extends Application {
  public static final double[] dim = {800, 600};
  public static final double width = 800;
  public static final double height = 600;
  public static final int edgeMargin = 15;
  public static Player player;
  public static ParallelCamera cam;
  public static GraphicsContext gc;
  public static int frame = -1;
  public static int focusHold = 0;
  public static ArrayList<Bullet> bullets = new ArrayList<>();

  public void start(Stage stage) throws IOException {
    // setup JavaFX
    Canvas canvas = new Canvas(dim[0], dim[1]);
    gc = canvas.getGraphicsContext2D();
    Timeline tl = new Timeline(new KeyFrame(Duration.millis(17), e->run()));
    tl.setCycleCount(Timeline.INDEFINITE);
    stage.setScene(new Scene(new StackPane(canvas)));
    stage.show();
    stage.setTitle("Game");
    tl.play();

    // setup input
    stage.getScene().setOnKeyPressed(e->Input.keyRequest(e.getCode(), true));
    stage.getScene().setOnKeyReleased(e->Input.keyRequest(e.getCode(), false));

    // setup game
    String[] pImgArr = new String[4];
    for (int i = 0; i < 4; i++) {
      pImgArr[i] = "Reimu/Reimu" + (i + 1) + ".png";
    }
    player = new Player(7, 0.5, 3, new Sprite(pImgArr, new int[] {6, 0}, 20, 0.75));
    player.pos.set(width*0.5, height*0.8);
  }

  private void run() {
    frame++;
    Input.keyTick();
    calc();
    draw();
  }

  private void calc() {
    movePlayer();
    spawnBullets();
    moveBullets();
  }

  private void draw() {
    drawBG();
    drawPlayer();
    drawBullets();
    drawPlayerHB();
  }

  private void movePlayer() {
    int[] moveOffset = new int[] {0, 0};
    if (Input.getInput("left").isPressed())
      moveOffset[0]--;
    if (Input.getInput("right").isPressed())
      moveOffset[0]++;
    if (Input.getInput("up").isPressed())
      moveOffset[1]--;
    if (Input.getInput("down").isPressed())
      moveOffset[1]++;

    double multi = player.speed * (Input.getInput("focus").isPressed()? player.focusMulti : 1);
    player.pos.move(moveOffset, multi);
    if (player.pos.x < edgeMargin)
      player.pos.x = edgeMargin;
    else if (player.pos.x > width - edgeMargin)
      player.pos.x = width - edgeMargin;
    if (player.pos.y < edgeMargin)
      player.pos.y = edgeMargin;
    else if (player.pos.y > height - edgeMargin)
      player.pos.y = height - edgeMargin;
    player.dir = moveOffset[0] * multi * 5;

    if (focusHold < 10 && Input.getInput("focus").isPressed())
      focusHold++;
    else if (focusHold > 0 && !Input.getInput("focus").isPressed())
      focusHold--;
  }

  public void moveBullets() {
    ArrayList<Bullet> aliveBullets = new ArrayList<>();
    for (Bullet b : bullets) {
      b.move();
      if (b.isAlive() || b.getTime() < 10)
        aliveBullets.add(b);
    }
    bullets = aliveBullets;

  }

  public void spawnBullets() {
    if (frame % 30 == 0) {
      for (int i = 0; i < 30; i++)
        bullets.add(
            new Bullet(
                new Position(width / 2, height * 0.2),
                0.75,
                BulletColor.RED,
                new BulletAttr[] {new LinMoveAttr(null, 2, frame + Math.random() * 360)}));
    }
  }

  private void drawBG() {
    // background
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, width, height);

    // setup text
    gc.setFill(Color.WHITE);
    gc.setFont(Font.font(25));
  }

  public static void drawPlayer() {
    player.alpha = 1-(double)focusHold/20;
    player.draw(gc);
  }

  public static void drawBullets() {
    // group bullets by Bounding Circle intersection
    HashSet<BulletGroup> groups = new HashSet<>();
    Stack<BulletGroup> stack = new Stack<>();
    BulletGroup[] bga = new BulletGroup[bullets.size()];
    for (int i = 0; i < bullets.size(); ++i)
      bga[i] = new BulletGroup(bullets.get(i));
    Arrays.sort(bga, new BulletGroupComparator(width/2, 0));
    Collections.reverse(Arrays.asList(bga));
    Collections.addAll(stack, bga);

    while (stack.size() != 0) {
      BulletGroup bg = stack.pop();
      boolean newGroup = true;
      BulletGroup[] bgArr = new BulletGroup[groups.size()];
      int i = 0;
      for (BulletGroup bg2 : groups)
        bgArr[i++] = bg2;
      Arrays.sort(bgArr, new BulletGroupComparator(bg.getCenter()));
      // Collections.reverse(Arrays.asList(bgArr));
      for (BulletGroup bg2 : bgArr) {
        if (bg.intersects(bg2)) {
          newGroup = false;
          bg2.merge(bg);
          groups.remove(bg2);
          stack.add(bg2);
          break;
        }
      }
      if (newGroup)
        groups.add(bg);
    }

    // for testing
    gc.setFill(Color.GRAY);
    for (BulletGroup bg : groups) {
      Circle c = bg.getBounds();
      gc.fillArc(c.getCenterX()-c.getRadius(), c.getCenterY()-c.getRadius(), c.getRadius()*2, c.getRadius()*2, 0, 360, ArcType.ROUND);
    }

    // render back of bullets on 1 thread (to allow for tighter grouping
    for (Bullet b : bullets)
      b.drawBack(gc);

    // render front of bullets using 1 thread per group
    for (BulletGroup bg : groups) {
      BulletRenderThread br = new BulletRenderThread(bg);
      br.run();
    }
    /*
    for (Bullet b : bullets)
      b.drawBack(gc);
    for (Bullet b : bullets)
      b.drawFront(gc);
     */
  }

  public static void drawPlayerHB() {
    if (focusHold == 0)
      return;
    double radius = (-2.913*Math.pow(((double)focusHold/10)-0.5859, 2)*2 + 2) * player.hbRadius * 6;
    gc.setFill(Color.color(0, 1, 1));
    RadialGradient grad = new RadialGradient(0, 0, player.pos.x, player.pos.y, radius, false, CycleMethod.NO_CYCLE, Arrays.asList(
            new Stop(0, Color.color(1, 1, 1, 1.0)),
            new Stop(0.1, Color.color(0.5, 1, 1, 1.0)),
            new Stop(0.2, Color.color(0, 0.5, 1, 0.5)),
            new Stop(0.4, Color.color(0, 0, 1, 0))
    ));
    gc.setFill(grad);
    gc.fillArc(player.pos.x-radius, player.pos.y-radius, radius*2, radius*2, 0, 360, ArcType.ROUND);
  }

  public static void main(String[] args) {
    Input.init();
    launch();
  }
}