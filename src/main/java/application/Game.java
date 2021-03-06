package application;

import application.attack.PlayerAttack;
import application.bg.BackgroundLayer;
import application.bg.CloudLayer;
import application.bullet.BulletColor;
import application.bullet.attr.bullet.BulletAttr;
import application.bullet.attr.bullet.LinMoveAttr;
import application.bullet.attr.change.LinChangeAttr;
import application.bullet.types.BulletTemplate;
import application.bullet.types.BulletType;
import application.enemy.types.*;
import application.enemy.pathing.*;
import application.level.*;
import application.particle.Particle;
import application.pattern.*;
import application.bullet.types.Bullet;
import application.sprite.*;
import application.stats.Item;
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
  public static final int width = 600;
  public static final int height = 800;
  public static final int guiWidth = 300;
  public static final int playerMoveEdgeMargin = 15;
  public static Player player;
  public static ParallelCamera cam;
  public static boolean debug = false;
  public static boolean paused = false;
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
            new CloudLayer(backgroundGroup, width, height, 5,  Color.color(1, 1, 1, 0.1), 25),
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
            new AnimatedSprite(playerGroup, pImgArr, new double[] {6, 0}, 0.75, 20), new Stats(3, 12, 7, 0.5, 1, 5, 5, 10, 5, 1, 2, 0, false, false,  Stats.ProjType.BULLET, Stats.LaserType.NONE, Color.YELLOW));
    player.pos.set(width * 0.5, height * 0.8);

    // player.addItem(Item.BoxingGloves);
    // player.addItem(Item.TruePrecision);
    // player.addItem(Item.Delineator);
    // player.addItem(Item.Shotgun);
    // player.addItem(Item.DoubleShot);
    // player.addItem(Item.TripleShot);
    player.addItem(Item.AttackNeedles);
    // player.addItem(Item.PlasmaCore);
    // player.addItem(Item.Anvil);
    // player.addItem(Item.RainStorm);
    // player.addItem(Item.CompactSnow);
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
                    25,
                    5,
                    PatternFactory.Test()),
                new Enemy(
                    225,
                    PathFactory.linearPath(width, 0, 0, height * 0.5, 225),
                    new StaticSprite(enemyGroup, "enemy/bigPrismElite.png", null, 0.1),
                    Color.RED,
                    25,
                    5,
                    PatternFactory.Test())));
    LevelEvent streamSpawn1 =
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
                    15,
                    2,
                    PatternFactory.TestStream(BulletType.RICE, BulletColor.CYAN, 3, 15))));
    LevelEvent streamSpawn2 =
            new LevelEvent(
                    300,
                    LevelActionFactory.repeatedEnemySpawn(
                            0,
                            50,
                            new Enemy(
                                    120,
                                    PathFactory.linearPath(0, height * 0.25, width, height * 0.25, 120),
                                    new StaticSprite(enemyGroup, "enemy/smallPrismElite.png", null, 0.1),
                                    Color.RED,
                                    15,
                                    2,
                                    PatternFactory.TestStream(BulletType.RICE, BulletColor.ORANGE, 4, 15),
                                    PatternFactory.Ring(30, 30, 30, new BulletTemplate(BulletType.ORB, null, 1, BulletColor.YELLOW, new BulletAttr[] {
                                            new LinMoveAttr("move", 15, 0, new LinChangeAttr("acc", -0.5, 3))
                                    }), "move")
                            )));
    LevelEvent denseSpawn1 = new LevelEvent(
            300,
            LevelActionFactory.singleEnemySpawn(
                    new Enemy(
                        900,
                        PathFactory.convergePath((double)width/2, 0.0, (double)width/2, height * 0.2, 0.05),
                        new StaticSprite(enemyGroup, "enemy/bigPrism.png", null, 0.2),
                        Color.LIGHTBLUE,
                        50,
                        50,
                        PatternFactory.FalseDoubleRotate(45, BulletColor.create(0, 1, 1), BulletColor.create(0, 0.75, 1), BulletColor.create(0, 0.5, 1))
                    )
            )
    );
    Pattern[] wpArr = new Pattern[2];
    for (int i = 0; i < 2; i++)
      wpArr[i] = PatternFactory.WaveParticle(2, 120, 1, -90, i*2 - 1, 1.5, new BulletTemplate(BulletType.RICE, null, 1, i == 0? BulletColor.GREEN : BulletColor.MAGENTA, new BulletAttr[] {
            new LinMoveAttr("move", 25, 0, new LinChangeAttr("acc", -1, 2))
    }), "move");
    LevelEvent denseSpawn2 = new LevelEvent(
            900,
            LevelActionFactory.singleEnemySpawn(
                    new Enemy(
                            1200,
                            PathFactory.convergePath((double)width*0.3, 0.0, (double)width*0.3, height * 0.3, 0.05),
                            new StaticSprite(enemyGroup, "enemy/bigPrismElite.png", null, 0.2),
                            Color.RED,
                            50,
                            50,
                            wpArr[0]
                    ),
                    new Enemy(
                            1200,
                            PathFactory.convergePath((double)width*0.7, 0.0, (double)width*0.7, height * 0.3, 0.05),
                            new StaticSprite(enemyGroup, "enemy/bigPrismElite.png", null, 0.2),
                            Color.RED,
                            50,
                            50,
                            wpArr[1]
                    )
            )
    );
    LevelSegment testSeg = new LevelSegment(streamSpawn1, new LevelBreak(60), burstSpawn, new LevelBreak(180), streamSpawn2, denseSpawn1, new LevelBreak(300), denseSpawn2, new LevelBreak(300));
    Level testLevel = new Level(new LevelSegment(denseSpawn2, new LevelBreak(300), testSeg, testSeg, testSeg, testSeg, testSeg));
    Level.setActive(testLevel);

    // start game loop
    Timeline tl = new Timeline(new KeyFrame(Duration.millis(17), e -> run()));
    tl.setCycleCount(Timeline.INDEFINITE);
    tl.play();
  }

  private void run() {
    frame++;
    Input.keyTick();
    input();
    if (!paused) {
      calc();
      draw();
      if (debug) debugRun();
    }
  }

  private void input() {
    toggles();
  }

  private void calc() {
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
    if (Input.getInput("pause").onInitialPress()) {
      paused = !paused;
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
    player.dir = -moveOffset[0] * multi * 5;

    if (focusHold < 10 && Input.getInput("focus").isPressed()) focusHold++;
    else if (focusHold > 0 && !Input.getInput("focus").isPressed()) focusHold--;
  }

  private static void screenResize() {
    double w = borderWidth * 2 + width + guiWidth;
    double h = borderWidth * 2 + height;
    double topOffset = stage.getScene().getWindow().getHeight() - stage.getScene().getHeight();
    double sideOffset = stage.getScene().getWindow().getWidth() - stage.getScene().getWidth();
    double scaleVal = Math.min((stage.getWidth() - sideOffset * 0.5) / w, (stage.getHeight() - topOffset * 0.5) / h);
    Scale scale = new Scale();
    scale.setPivotX(0);
    scale.setPivotY(0);
    scale.setX(scaleVal);
    scale.setY(scaleVal);
    rootGroup.getTransforms().setAll(scale);
    rootGroup.setTranslateX((stage.getWidth() - scaleVal * w) / 2 + borderWidth * scaleVal - sideOffset * 0.5);
    rootGroup.setTranslateY((stage.getHeight() - scaleVal * h) / 2 + borderWidth * scaleVal - topOffset * 0.5);
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
