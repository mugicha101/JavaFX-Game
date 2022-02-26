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
import javafx.util.Pair;

import java.io.IOException;
import java.lang.reflect.Array;
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
  public static boolean debug = false;
  public static final double chunkSize = 50;

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
    }
    bullets = aliveBullets;

  }

  public void spawnBullets() {
    if (frame % 10 == 0) {
      Position pos = new Position(width * (0.25 + Math.random() * 0.5), height * (0.1 + Math.random() * 0.2));
      double dir = Math.random() * 360;
      for (int i = 0; i < 36; i++)
        bullets.add(
            new Bullet(
                pos,
                1,
                BulletColor.RED,
                new BulletAttr[] {new LinMoveAttr(null, 2, dir + i * 10)}));
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

  private static Integer getChunkId(double x, double y) {
    return (int)(y/chunkSize) * 1000 + (int)(x/chunkSize);
  }

  private static double getChunkIdX(Integer id) {
    return (id % 1000) * chunkSize;
  }

  private static double getChunkIdY(Integer id) {
    return (id / 1000) * chunkSize;
  }

  private static void groupHelper(ArrayList<Integer> group, HashMap<Integer, HashSet<Integer>> adjList, HashSet<Integer> visited, HashMap<Integer, BulletGroup> chunks, Integer id) {
    group.add(id);
    visited.add(id);
    for (Integer nid : adjList.get(id)) {
      if (!visited.contains(nid)) {
        groupHelper(group, adjList, visited, chunks, nid);
      }
    }
  }

  public static void drawBullets() {
    // group bullets by Bounding Circle intersection
    // TODO: switch to grid chunk based grouping (UnionFind chunks if bullets on edge)
    // Form Chunks
    HashMap<Integer, BulletGroup> chunks = new HashMap<>();
    HashMap<Integer, HashSet<Integer>> adjList = new HashMap<>();
    for (Bullet b : bullets) {
      // get chunks
      HashSet<Integer> chunkIdSet = new HashSet<>();
      double radius = b.getRenderRadius();
      chunkIdSet.add(getChunkId(b.pos.x - b.radius, b.pos.y - b.radius));
      chunkIdSet.add(getChunkId(b.pos.x + b.radius, b.pos.y - b.radius));
      chunkIdSet.add(getChunkId(b.pos.x - b.radius, b.pos.y + b.radius));
      chunkIdSet.add(getChunkId(b.pos.x + b.radius, b.pos.y + b.radius));
      for (Integer id : chunkIdSet) {
        if (!chunks.containsKey(id)) chunks.put(id, new BulletGroup());
      }

      // connect chunks
      List<Integer> chunkIdArr = chunkIdSet.stream().toList();
      for (Integer id : chunkIdArr) {
        if (!adjList.containsKey(id)) adjList.put(id, new HashSet<>());
      }
      chunks.get(chunkIdArr.get(0)).addBullet(b);
      if (chunkIdArr.size() > 1) {
        for (int i = 1; i < chunkIdArr.size(); i++) {
          adjList.get(chunkIdArr.get(0)).add(chunkIdArr.get(i));
          adjList.get(chunkIdArr.get(i)).add(chunkIdArr.get(0));
        }
      }
    }


    // form groups by dfs
    HashSet<Integer> visited = new HashSet<>();
    ArrayList<ArrayList<Integer>> chunkGroups = new ArrayList<>();
    for (Integer id : chunks.keySet()) {
      if (!visited.contains(id)) {
        ArrayList<Integer> group = new ArrayList<>();
        chunkGroups.add(group);
        groupHelper(group, adjList, visited, chunks, id);
      }
    }
    ArrayList<BulletGroup> bgArr = new ArrayList<>(chunkGroups.size());
    for (ArrayList<Integer> group : chunkGroups) {
      bgArr.add(chunks.get(group.get(0)));
      for (int j = 1; j < group.size(); j++) {
        bgArr.get(bgArr.size()-1).merge(chunks.get(group.get(j)));
      }
    }

    // group smallest groups until amount of groups is at most the amount of processors
    int cores = Runtime.getRuntime().availableProcessors();
    while (bgArr.size() > cores) {
      bgArr.sort(new BulletGroupComparator());
      bgArr.get(bgArr.size()-2).merge(bgArr.get(bgArr.size()-1));
      bgArr.remove(bgArr.size()-1);
    }
    bgArr.sort(new BulletGroupComparator());

    // for testing
    if (debug) {
      double c = 0;
      for (BulletGroup bg : bgArr) {
        Color color = Color.color(
                Math.sin(c) * 0.5 + 0.5,
                Math.sin(c + Math.PI * 2 / 3) * 0.5 + 0.5,
                Math.sin(c + Math.PI * 4 / 3) * 0.5 + 0.5);
        c += 2 * Math.PI / cores;
        for (Bullet b : bg.getBullets()) {
          b.color = new BulletColor(Color.WHITE, color);
        }
      }
    }

    // render front of bullets using 1 thread per group
    for (BulletGroup bg : bgArr) {
      BulletRenderThread br = new BulletRenderThread(bg);
      br.run();
    }
    gc.setGlobalAlpha(1);
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