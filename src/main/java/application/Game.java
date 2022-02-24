package application;

import application.bullet.BulletColor;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Game extends Application {
  public static final double[] dim = {800, 600};
  public static final double width = 800;
  public static final double height = 600;
  public static final int edgeMargin = 15;
  public static Player player;
  public static ParallelCamera cam;
  public static GraphicsContext gc;
  public static int frame = 0;
  public static int focusHold = 0;
  public static ArrayList<Bullet> bullets = new ArrayList<>();

  public void start(Stage stage) throws IOException {
    // setup JavaFX
    Canvas canvas = new Canvas(dim[0], dim[1]);
    gc = canvas.getGraphicsContext2D();
    cam = new ParallelCamera();
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
    player = new Player(7, 0.5, 10, new Sprite(pImgArr, new int[] {6, 0}, 20, 0.75));
    player.pos.set(width*0.5, height*0.8);

    // bullet testing
    BulletColor[] bColors;
    bColors = new BulletColor[] {
            BulletColor.RED,
            BulletColor.ORANGE,
            BulletColor.YELLOW,
            BulletColor.GREEN,
            BulletColor.TURQUOISE,
            BulletColor.CYAN,
            BulletColor.BLUE,
            BulletColor.PURPLE,
            BulletColor.MAGENTA,
            BulletColor.ROSE
    };
    for (int i = 0; i < bColors.length; i++)
      bullets.add(new Bullet(new Position(width/2 + ((double)i-((double)bColors.length-1)/2)*30, height/2), 1, bColors[i], new BulletAttr[] {}));
    bColors = new BulletColor[] {
            BulletColor.INVERSE_RED,
            BulletColor.INVERSE_ORANGE,
            BulletColor.INVERSE_YELLOW,
            BulletColor.INVERSE_GREEN,
            BulletColor.INVERSE_TURQUOISE,
            BulletColor.INVERSE_CYAN,
            BulletColor.INVERSE_BLUE,
            BulletColor.INVERSE_PURPLE,
            BulletColor.INVERSE_MAGENTA,
            BulletColor.INVERSE_ROSE
    };
    for (int i = 0; i < bColors.length; i++)
      bullets.add(new Bullet(new Position(width/2 + ((double)i-((double)bColors.length-1)/2)*30, height/2+30), 1, bColors[i], new BulletAttr[] {}));
    bColors = new BulletColor[] {
            BulletColor.DARK_RED,
            BulletColor.DARK_ORANGE,
            BulletColor.DARK_YELLOW,
            BulletColor.DARK_GREEN,
            BulletColor.DARK_TURQUOISE,
            BulletColor.DARK_CYAN,
            BulletColor.DARK_BLUE,
            BulletColor.DARK_PURPLE,
            BulletColor.DARK_MAGENTA,
            BulletColor.DARK_ROSE
    };
    for (int i = 0; i < bColors.length; i++)
      bullets.add(new Bullet(new Position(width/2 + ((double)i-((double)bColors.length-1)/2)*30, height/2+60), 1, bColors[i], new BulletAttr[] {}));
  }

  private void run() {
    Input.keyTick();
    calc();
    draw();
    frame++;
  }

  private void calc() {
    movePlayer();
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

    double multi = player.speed * (Input.getInput("focus").isPressed()? player.focus_multi : 1);
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
      b.draw(gc);
    }
  }

  public static void drawPlayerHB() {
    if (focusHold == 0)
      return;
    double radius = (-2.913*Math.pow(((double)focusHold/10)-0.5859, 2) + 1) * player.hitbox_radius*4;
    gc.setFill(Color.color(0, 1, 1));
    RadialGradient grad = new RadialGradient(0, 0, player.pos.x, player.pos.y, radius, false, CycleMethod.NO_CYCLE, Arrays.asList(
            new Stop(0, Color.color(1, 1, 1, 1.0)),
            new Stop(0.1, Color.color(0.5, 1, 1, 1.0)),
            new Stop(0.2, Color.color(0, 0.5, 1, 0.5)),
            new Stop(0.4, Color.color(0, 0, 1, 0))
    ));
    gc.setFill(grad);
    gc.fillArc(player.pos.x-radius/2, player.pos.y-radius/2, radius, radius, 0, 360, ArcType.ROUND);
  }

  public static void main(String[] args) {
    Input.init();
    launch();
  }
}