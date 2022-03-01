package application;

import application.bullet.BulletColor;
import application.bullet.bulletAttr.LinAccelAttr;
import application.bullet.bulletAttr.LinMoveAttr;
import application.bullet.bulletAttr.MoveAttr;
import application.bullet.bulletTypes.Bullet;
import application.bullet.bulletAttr.BulletAttr;
import application.bullet.bulletTypes.RiceBullet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
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
  public static boolean debug = false;
  public static GraphicsContext gc;
  public static int frame = -1;
  public static int focusHold = 0;
  public static ArrayList<Bullet> bullets = new ArrayList<>();
  public static Group root;
  public static Group bulletGroupFront;
  public static Group bulletGroupBack;

  public void start(Stage stage) throws IOException {
    // setup JavaFX
    root = new Group();
    Canvas canvas = new Canvas(dim[0], dim[1]);
    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);
    Timeline tl = new Timeline(new KeyFrame(Duration.millis(17), e->run()));
    tl.setCycleCount(Timeline.INDEFINITE);
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setTitle("Game");
    tl.play();

    // setup input
    stage.getScene().setOnKeyPressed(e->Input.keyRequest(e.getCode(), true));
    stage.getScene().setOnKeyReleased(e->Input.keyRequest(e.getCode(), false));

    // setup scene graph bullets node
    bulletGroupBack = new Group();
    bulletGroupFront = new Group();
    root.getChildren().add(bulletGroupBack);
    root.getChildren().add(bulletGroupFront);

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
    debugToggle();
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

  private void debugToggle() {
    if (Input.getInput("debug").onInitialPress()) {
      debug = !debug;
    }
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
      else
        b.delete();
    }
    bullets = aliveBullets;

  }

  public void spawnBullets() {
    if (frame % 600 < 300? frame % 5 == 0 : frame % 20 == 0) {
      Position pos = new Position(width * (0.25 + Math.random() * 0.5), height * (0.1 + Math.random() * 0.2));
      double dir = Math.random() * 360;
      for (int i = 0; i < 36; i++) {
        bullets.add(
            new RiceBullet(
                pos,
                1,
                BulletColor.YELLOW,
                new MoveAttr[] {
                        new LinMoveAttr(null, 20, dir + i * 10, new LinAccelAttr(-1, 3))
                }));
        if (i % 2 == 0) {
          bullets.add(
              new Bullet(
                  pos,
                  1,
                  BulletColor.RED,
                  new MoveAttr[] {
                    new LinMoveAttr(null, 15, dir + i * 10, new LinAccelAttr(-1, 2))
                  }));
        }
      }
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
    for (Bullet b : bullets) {
      b.drawUpdate();
    }
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