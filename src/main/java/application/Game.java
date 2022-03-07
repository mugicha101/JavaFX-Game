package application;

import application.enemy.types.Enemy;
import application.patterns.*;
import application.bullet.types.Bullet;
import application.patterns.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class Game extends Application {
  public static double rightMargin = 9;
  public static double topMargin = 24;
  public static final double width = 800;
  public static final double height = 600;
  public static final int edgeMargin = 15;
  public static Player player;
  public static ParallelCamera cam;
  public static boolean debug = false;
  public static int frame = -1;
  public static int focusHold = 0;
  public static Stage stage;
  //public static StackPane root;
  public static Group rootGroup;
  public static Group playerGroup;
  public static Group bulletGroupFront;
  public static Group bulletGroupBack;
  public static Group playerHBGroup;

  public void start(Stage stage) throws IOException {
    // setup JavaFX
    rootGroup = new Group();
    rootGroup.setClip(new Rectangle(0, 0, width, height));
    Rectangle bg = new Rectangle(0, 0, width, height);
    bg.setFill(Color.BLACK);
    rootGroup.getChildren().add(bg);
    // root.setClip(new Rectangle(0, 0, width, height));
    // Canvas canvas = new Canvas(dim[0], dim[1]);
    // root.getChildren().add(canvas);
    Timeline tl = new Timeline(new KeyFrame(Duration.millis(17), e -> run()));
    tl.setCycleCount(Timeline.INDEFINITE);
    Scene scene = new Scene(rootGroup);
    stage.setScene(scene);
    stage.setWidth(width + rightMargin);
    stage.setHeight(height + topMargin);
    stage.setMinWidth(0);
    stage.setMinHeight(0);
    // stage.initStyle(StageStyle.UNDECORATED);
    stage.setResizable(true);
    stage.show();
    stage.setTitle("Game");
    Game.stage = stage;
    tl.play();

    // setup input
    stage.getScene().setOnKeyPressed(e -> Input.keyRequest(e.getCode(), true));
    stage.getScene().setOnKeyReleased(e -> Input.keyRequest(e.getCode(), false));

    // setup scene graph bullets node
    playerGroup = new Group();
    rootGroup.getChildren().add(playerGroup);
    bulletGroupBack = new Group();
    rootGroup.getChildren().add(bulletGroupBack);
    bulletGroupFront = new Group();
    rootGroup.getChildren().add(bulletGroupFront);
    playerHBGroup = new Group();
    rootGroup.getChildren().add(playerHBGroup);

    // setup game
    String[] pImgArr = new String[4];
    for (int i = 0; i < 4; i++) {
      pImgArr[i] = "Reimu/Reimu" + (i + 1) + ".png";
    }
    player = new Player(7, 0.5, 3, new Sprite(playerGroup, pImgArr, new int[] {6, 0}, 20, 0.75));
    player.pos.set(width * 0.5, height * 0.8);
  }

  private void run() {
    frame++;
    Input.keyTick();
    calc();
    draw();
  }

  private void calc() {
    toggles();
    movePlayer();
    Pattern.patternTick();
    Bullet.moveBullets();
    Enemy.moveEnemies();
  }

  private void draw() {
    screenResize();
    drawPlayer();
    Bullet.drawBullets();
  }

  private void toggles() {
    if (Input.getInput("debug").onInitialPress()) {
      debug = !debug;
    }
    if (Input.getInput("fullscreen").onInitialPress()) {
      stage.setFullScreen(!stage.isFullScreen());
    }
  }

  private void movePlayer() {
    int[] moveOffset = new int[] {0, 0};
    if (Input.getInput("left").isPressed()) moveOffset[0]--;
    if (Input.getInput("right").isPressed()) moveOffset[0]++;
    if (Input.getInput("up").isPressed()) moveOffset[1]--;
    if (Input.getInput("down").isPressed()) moveOffset[1]++;

    double multi = player.speed * (Input.getInput("focus").isPressed() ? player.focusMulti : 1);
    player.pos.move(moveOffset, multi);
    if (player.pos.x < edgeMargin) player.pos.x = edgeMargin;
    else if (player.pos.x > width - edgeMargin) player.pos.x = width - edgeMargin;
    if (player.pos.y < edgeMargin) player.pos.y = edgeMargin;
    else if (player.pos.y > height - edgeMargin) player.pos.y = height - edgeMargin;
    player.dir = moveOffset[0] * multi * 5;

    if (focusHold < 10 && Input.getInput("focus").isPressed()) focusHold++;
    else if (focusHold > 0 && !Input.getInput("focus").isPressed()) focusHold--;
  }

  public static void screenResize() {
    double rm = stage.isFullScreen()? 0 : rightMargin;
    double tm = stage.isFullScreen()? 0 : topMargin;
    double scaleVal = Math.min((stage.getWidth() - rm) / width, (stage.getHeight() - tm) / height);
    Scale scale = new Scale();
    scale.setPivotX(0);
    scale.setPivotY(0);
    scale.setX(scaleVal);
    scale.setY(scaleVal);
    rootGroup.getTransforms().setAll(scale);
    rootGroup.setTranslateX((stage.getWidth() - scaleVal * width) / 2 - rm);
    rootGroup.setTranslateY((stage.getHeight() - scaleVal * height) / 2 - tm);
  }

  public static void drawPlayer() {
    player.alpha = 1 - (double) focusHold / 20;
    player.drawUpdate();
  }

  public static void main(String[] args) {
    Input.init();
    new TestPattern();
    launch();
  }
}
