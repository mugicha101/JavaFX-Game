package application;

import application.enemy.types.*;
import application.enemy.pathing.*;
import application.level.*;
import application.pattern.*;
import application.bullet.types.Bullet;
import application.sprite.*;
import application.stats.Player;
import application.stats.Stats;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
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
  public static Group rootGroup;
  public static Group playerGroup;
  public static Group enemyGroup;
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
    enemyGroup = new Group();
    rootGroup.getChildren().add(enemyGroup);
    bulletGroupBack = new Group();
    rootGroup.getChildren().add(bulletGroupBack);
    bulletGroupFront = new Group();
    rootGroup.getChildren().add(bulletGroupFront);
    playerHBGroup = new Group();
    rootGroup.getChildren().add(playerHBGroup);

    // setup player
    String[] pImgArr = new String[4];
    for (int i = 0; i < 4; i++) {
      pImgArr[i] = "Reimu/Reimu" + (i + 1) + ".png";
    }
    player =
        new Player(
            new AnimatedSprite(playerGroup, pImgArr, new double[] {6, 0}, 0.75, 20), new Stats(3, 12, 7, 0.5, 1));
    player.pos.set(width * 0.5, height * 0.8);

    // setup level
    LevelEvent burstSpawn =
        new LevelEvent(
            225,
            LevelActionFactory.singleEnemySpawn(
                new Enemy(
                    225,
                    PathFactory.linearPath(0, 0, width, height * 0.5, 225),
                    new StaticSprite(enemyGroup, "enemy/bigPrism.png", null, 0.1),
                    100,
                    PatternFactory.Test()),
                new Enemy(
                    225,
                    PathFactory.linearPath(width, 0, 0, height * 0.5, 225),
                    new StaticSprite(enemyGroup, "enemy/bigPrism.png", null, 0.1),
                    100,
                    PatternFactory.Test())));
    LevelEvent streamSpawn =
        new LevelEvent(
            300,
            LevelActionFactory.repeatedEnemySpawn(
                0,
                50,
                new Enemy(
                    120,
                    PathFactory.linearPath(0, height * 0.25, width, height * 0.25, 120),
                    new StaticSprite(enemyGroup, "enemy/smallPrism.png", null, 0.1),
                    100,
                    PatternFactory.TestStream())));
    Level testLevel = new Level(new LevelSegment(streamSpawn, new LevelBreak(60), burstSpawn));
    Level.setActive(testLevel);
  }

  private void run() {
    frame++;
    Input.keyTick();
    calc();
    draw();
    if (debug) debugRun();
  }

  private void calc() {
    toggles();
    movePlayer();
    Bullet.moveBullets();
    Enemy.moveEnemies();
    Level.tickActive();
  }

  private void draw() {
    screenResize();
    drawPlayer();
    Enemy.drawEnemies();
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

    double multi = player.getStats().speed * (Input.getInput("focus").isPressed() ? player.getStats().focusMulti : 1);
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
    double rm = stage.isFullScreen() ? 0 : rightMargin;
    double tm = stage.isFullScreen() ? 0 : topMargin;
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

  public static void debugRun() {
    if (frame % 60 == 0) {
      System.out.println("cycle: " + frame);
      System.out.println(Bullet.getBullets().size());
      System.out.println(
          bulletGroupBack.getChildren().size() + ", " + bulletGroupFront.getChildren().size());
      System.out.println();
    }
  }

  public static void main(String[] args) {
    Input.init();
    launch();
  }
}
