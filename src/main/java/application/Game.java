package application;

import application.attack.PlayerAttack;
import application.bg.BackgroundLayer;
import application.bg.CloudLayer;
import application.enemy.types.*;
import application.enemy.pathing.*;
import application.level.*;
import application.particle.Particle;
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
  public static final int borderWidth = 20;
  public static final int topMargin = 20;
  public static final int width = 600;
  public static final int height = 800;
  public static final int guiWidth = 300;
  public static final int playerMoveEdgeMargin = 15;
  public static Player player;
  public static ParallelCamera cam;
  public static boolean debug = false;
  public static int frame = -1;
  public static int focusHold = 0;
  public static Stage stage;
  public static Group rootGroup;
  public static Group mainGroup;
  public static Group guiGroup;
  public static Group backgroundGroup;
  public static Group playerGroup;
  public static Group enemyGroup;
  public static Group bulletGroupFront;
  public static Group bulletGroupBack;
  public static Group playerHBGroup;
  public static BackgroundLayer[] bgLayers;

  public void start(Stage stage) throws IOException {
    // setup JavaFX
    rootGroup = new Group();
    // root.setClip(new Rectangle(0, 0, width, height));
    // Canvas canvas = new Canvas(dim[0], dim[1]);
    // root.getChildren().add(canvas);
    Scene scene = new Scene(rootGroup);
    stage.setScene(scene);
    stage.setWidth(width + guiWidth + borderWidth * 2);
    stage.setHeight(height + borderWidth * 2);
    stage.setMinWidth(borderWidth);
    stage.setMinHeight(borderWidth);
    // stage.initStyle(StageStyle.UNDECORATED);
    stage.setResizable(true);
    stage.show();
    stage.setTitle("Game");
    Game.stage = stage;

    // setup input
    stage.getScene().setOnKeyPressed(e -> Input.keyRequest(e.getCode(), true));
    stage.getScene().setOnKeyReleased(e -> Input.keyRequest(e.getCode(), false));

    // setup screen regions
    mainGroup = new Group();
    guiGroup = new Group();
    rootGroup.getChildren().addAll(mainGroup, guiGroup);

    mainGroup.setClip(new Rectangle(0, 0, width, height));
    Rectangle bg = new Rectangle(0, 0, width, height);
    bg.setFill(Color.color(0, 0, 0.1));
    mainGroup.getChildren().add(bg);

    guiGroup.setClip(new Rectangle(0, 0, guiWidth, height));
    bg = new Rectangle(0, 0, guiWidth, height);
    bg.setFill(Color.ORANGE);
    scene.setFill(Color.YELLOW);
    guiGroup.getChildren().add(bg);
    guiGroup.setTranslateX(width);

    // setup scene graph nodes
    backgroundGroup = new Group();
    playerGroup = new Group();
    enemyGroup = new Group();
    bulletGroupBack = new Group();
    bulletGroupFront = new Group();
    playerHBGroup = new Group();
    mainGroup.getChildren().addAll(backgroundGroup, playerGroup, enemyGroup, PlayerAttack.playerAttackGroup, bulletGroupBack, bulletGroupFront, Particle.particleGroup, playerHBGroup);

    // setup background
    bgLayers = new BackgroundLayer[] {
            new CloudLayer(backgroundGroup, width, height, 1,  Color.color(1, 1, 1, 0.1), 50),
            new CloudLayer(backgroundGroup, width, height, 3,  Color.color(1, 1, 1, 0.1), 25),
    };
    for (BackgroundLayer bgLayer : bgLayers)
      bgLayer.init();

    // setup player
    String[] pImgArr = new String[4];
    for (int i = 0; i < 4; i++) {
      pImgArr[i] = "Reimu/Reimu" + (i + 1) + ".png";
    }
    player =
        new Player(
            new AnimatedSprite(playerGroup, pImgArr, new double[] {6, 0}, 0.75, 20), new Stats(3, 12, 7, 0.5, 1, 5, 5, 10, 5, 1, 2, 0, false, Stats.ProjType.BULLET, Stats.LaserType.NONE, Color.YELLOW));
    player.pos.set(width * 0.5, height * 0.8);

    // player.addItem(Item.BoxingGloves);
    //player.addItem(Item.TruePrecision);
    // player.addItem(Item.Delineator);
    // player.addItem(Item.Shotgun);
    // player.addItem(Item.DoubleShot);
    // player.addItem(Item.TripleShot);
    // player.addItem(Item.AttackNeedles);
    // player.addItem(Item.PlasmaCore);
    // player.addItem(Item.Anvil);
    // player.addItem(Item.RainStorm);
    // player.addItem(Item.InflatableBalloon);

    // setup level
    LevelEvent burstSpawn =
        new LevelEvent(
            225,
            LevelActionFactory.singleEnemySpawn(
                new Enemy(
                    225,
                    PathFactory.linearPath(0, 0, width, height * 0.5, 225),
                    new StaticSprite(enemyGroup, "enemy/bigPrismElite.png", null, 0.1),
                    Color.RED,
                    30,
                    5,
                    PatternFactory.Test()),
                new Enemy(
                    225,
                    PathFactory.linearPath(width, 0, 0, height * 0.5, 225),
                    new StaticSprite(enemyGroup, "enemy/bigPrismElite.png", null, 0.1),
                    Color.RED,
                    30,
                    5,
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
                    Color.LIGHTBLUE,
                    20,
                    2,
                    PatternFactory.TestStream())));
    LevelSegment testSeg = new LevelSegment(streamSpawn, new LevelBreak(60), burstSpawn, new LevelBreak(300));
    Level testLevel = new Level(new LevelSegment(testSeg, testSeg, testSeg, testSeg, testSeg));
    Level.setActive(testLevel);

    // start game loop
    Timeline tl = new Timeline(new KeyFrame(Duration.millis(17), e -> run()));
    tl.setCycleCount(Timeline.INDEFINITE);
    tl.play();
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
    PlayerAttack.moveAttacks();
    Enemy.moveEnemies();
    player.attackTick();
    Level.tickActive();
    Particle.moveParticles();
  }

  private void draw() {
    screenResize();
    drawBG();
    drawPlayer();
    Enemy.drawEnemies();
    PlayerAttack.drawAttacks();
    Bullet.drawBullets();
    Particle.drawParticles();
  }

  private void toggles() {
    if (Input.getInput("debug").onInitialPress()) {
      debug = !debug;
    }
    if (Input.getInput("fullscreen").onInitialPress()) {
      stage.setFullScreen(!stage.isFullScreen());
    }
  }

  private static void movePlayer() {
    int[] moveOffset = new int[] {0, 0};
    if (Input.getInput("left").isPressed()) moveOffset[0]--;
    if (Input.getInput("right").isPressed()) moveOffset[0]++;
    if (Input.getInput("up").isPressed()) moveOffset[1]--;
    if (Input.getInput("down").isPressed()) moveOffset[1]++;

    double multi = player.getStats().speed * (Input.getInput("focus").isPressed() ? player.getStats().focusMulti : 1);
    player.pos.move(moveOffset, multi);
    if (player.pos.x < playerMoveEdgeMargin) player.pos.x = playerMoveEdgeMargin;
    else if (player.pos.x > width - playerMoveEdgeMargin) player.pos.x = width - playerMoveEdgeMargin;
    if (player.pos.y < playerMoveEdgeMargin) player.pos.y = playerMoveEdgeMargin;
    else if (player.pos.y > height - playerMoveEdgeMargin) player.pos.y = height - playerMoveEdgeMargin;
    player.dir = moveOffset[0] * multi * 5;

    if (focusHold < 10 && Input.getInput("focus").isPressed()) focusHold++;
    else if (focusHold > 0 && !Input.getInput("focus").isPressed()) focusHold--;
  }

  private static void screenResize() {
    double w = borderWidth * 2 + width + guiWidth;
    double h = borderWidth * 2 + height;
    double scaleVal = Math.min((stage.getWidth()) / w, (stage.getHeight() - topMargin) / h);
    Scale scale = new Scale();
    scale.setPivotX(0);
    scale.setPivotY(0);
    scale.setX(scaleVal);
    scale.setY(scaleVal);
    rootGroup.getTransforms().setAll(scale);
    rootGroup.setTranslateX((stage.getWidth() - scaleVal * w) / 2 + borderWidth * scaleVal);
    rootGroup.setTranslateY((stage.getHeight() - scaleVal * h) / 2 + borderWidth * scaleVal - topMargin);
  }

  private static void drawBG() {
    for (BackgroundLayer bgLayer : bgLayers)
      bgLayer.drawUpdate();
  }

  private static void drawPlayer() {
    player.alpha = 1 - (double) focusHold / 20;
    player.drawUpdate();
  }

  private static void debugRun() {
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
