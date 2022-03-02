package application.bullet.types;

import application.Game;
import application.Position;
import application.bullet.BulletColor;
import application.bullet.attr.BulletAttr;
import application.bullet.attr.MoveAttr;
import application.bullet.staging.AttrStage;
import application.bullet.staging.BlankStage;
import application.bullet.staging.BulletStage;
import application.bullet.staging.ModifyStage;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Bullet {
  private static ArrayList<Bullet> bullets = new ArrayList<>();
  protected static final int frontGradientLayers = 3;
  protected static final int backGradientLayers = 5;
  protected static final double backOpacity = 0.1;
  public static final int offscreenMargin = 0;
  private boolean alive;
  protected int time;
  public double radius;
  private BulletColor color;
  private final ArrayList<MoveAttr> attrList;
  public final Position pos;
  public double dir; // direction bullet is facing (not necessarily direction its moving)
  private static int nextId = 0;
  private int id;
  protected Group groupBack;
  protected Group groupFront;
  protected static final double groupSize = 10;
  private String groupId;
  private static HashMap<String, ArrayList<Group[]>> groupCache = new HashMap<>(); // holds [groupBack, groupFront] for previously deleted bullets to be reused
  private final ArrayList<BulletStage> stageList;
  private int stageIndex;
  private int stageTime;
  private final HashMap<String, BulletAttr> attrMap;

  public static ArrayList<Bullet> getBullets() {
    return bullets;
  }

  public static void moveBullets() {
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

  public static void drawBullets() {
    for (Bullet b : bullets) {
      b.drawUpdate();
    }
  }

  public static void spawnBullet(Bullet bullet) {
    bullets.add(bullet);
  }

  public Bullet(Position pos, double size, BulletColor color, MoveAttr[] attrArr, BulletStage[] stageArr) {
    this.pos = pos.clone();
    alive = true;
    time = 0;
    this.radius = size*8;
    this.color = color;
    attrList = new ArrayList<>();
    attrMap = new HashMap<>();
    for (MoveAttr ma : attrArr) {
      MoveAttr maClone = ma.clone();
      attrList.add(maClone);
      maClone.toMap(attrMap, "");
    }
    if (stageArr == null)
      stageList = null;
    else {
      stageList = new ArrayList<>();
      stageList.addAll(Arrays.stream(stageArr).toList());
    }
    id = nextId++;
    groupFront = new Group();
    groupBack = new Group();
    Game.bulletGroupBack.getChildren().add(groupBack);
    Game.bulletGroupFront.getChildren().add(groupFront);
    groupId = null;
    drawUpdate();
    stageIndex = 0;
  }

  public Bullet(Position pos, double size, BulletColor color, MoveAttr[] attrArr) {
    this(pos, size, color, attrArr, null);
  }

  public String getType() {
    return "normal";
  }

  public final int getId() {
    return id;
  }

  public final BulletAttr getAttr(String id) {
    return attrMap.get(id);
  }

  public final boolean isAlive() {
    return alive;
  }

  public final int getTime() {
    return time;
  }

  public final boolean finishedStages() {
    return stageList == null || stageIndex == stageList.size();
  }

  public final void kill(boolean quick) {
    if (!alive)
      return;
    alive = false;
    time = quick? 10 : 0;
  }

  public final void kill() {
    kill(false);
  }

  public void delete() {
    transferGroupsToCache();
    Game.bulletGroupBack.getChildren().remove(groupBack);
    Game.bulletGroupFront.getChildren().remove(groupFront);
  }

  public final void move() {
    time++;
    if (!alive)
      return;

    // init
    if (time == 1) {
      for (MoveAttr ma : attrList)
        ma.init(this);
    }

    // update stages
    if (stageList != null && stageIndex < stageList.size()) {
      stageTime++;
      while (stageTime >= stageList.get(stageIndex).getTime()) {
        BulletStage baseStage = stageList.get(stageIndex);
        if (baseStage instanceof BlankStage stage) {
          stage.action();
        } else if (baseStage instanceof AttrStage stage) {
          BulletAttr ba = getAttr(stage.getId());
          if (ba == null)
            throw new NullPointerException(
                "null BulletAttr instance assigned to stage (id="
                    + stage.getId()
                    + " is not in available ids: "
                    + attrMap.keySet());
          stage.action(ba);
        } else if (baseStage instanceof ModifyStage stage) {
          stage.action(this);
        }
        stageTime = 0;
        stageIndex++;
        if (stageIndex == stageList.size())
          break;
      }
    }

    // prep
    for (MoveAttr ma : attrList)
      if (ma.enabled)
        ma.prepTick(this);

    // move
    for (MoveAttr ma : attrList)
      if (ma.enabled)
       ma.moveTick(this);

    // collision
    boolean defaultPlayerCollision = true;
    boolean defaultBorderCollision = true;
    for (MoveAttr ma : attrList) {
      if (!ma.enabled)
        continue;
      if (ma.overridesDefaultPlayerCollision())
        defaultPlayerCollision = false;
      if (ma.overridesDefaultBorderCollision())
        defaultBorderCollision = false;
      if (ma.collisionTick(this)) {
        this.kill();
        break;
      }
    }
    if (alive) {
      if (defaultPlayerCollision && (time >= 10 && pos.distSqd(Game.player.pos) <= Math.pow(radius + Game.player.hbRadius, 2)))
        this.kill();
      else if (defaultBorderCollision && finishedStages() && (pos.x > Game.width + offscreenMargin + getRenderRadius() || pos.y > Game.height + offscreenMargin + getRenderRadius() || pos.x < -getRenderRadius() - offscreenMargin || pos.y < -getRenderRadius() - offscreenMargin))
        this.kill(true);
    }
  }

  protected double getScale() {
    if (!alive)
      return 1 + time / 5.0;
    else if (time < 10)
      return 5 / (0.5 + time * 0.5);
    else
      return 1;
  }

  private String generateGroupId() {
    return getType() + ": " + color.getId();
  }

  private void copyGroups(Group backSource, Group frontSource) {
    groupBack.getChildren().clear();
    groupBack.getChildren().addAll(backSource.getChildren());
    groupFront.getChildren().clear();
    groupFront.getChildren().addAll(frontSource.getChildren());
  }

  private void transferGroupsToCache() { // saves groups to cache and clears groups
    if (groupId == null)
      return;
    Group gbCopy = new Group();
    Group gfCopy = new Group();
    gbCopy.getChildren().addAll(groupBack.getChildren());
    gfCopy.getChildren().addAll(groupFront.getChildren());
    groupBack.getChildren().clear();
    groupFront.getChildren().clear();
    if (!groupCache.containsKey(groupId))
      groupCache.put(groupId, new ArrayList<>());
    groupCache.get(generateGroupId()).add(new Group[] {gbCopy, gfCopy});
  }

  public final void drawUpdate() {
    String id = generateGroupId();
    if (groupId == null || !groupId.equals(id)) {
      // save old groups to cache and clear current groups
      transferGroupsToCache();
      // get/make new groups
      if (groupCache.containsKey(id) && groupCache.get(id).size() != 0) {
        // System.out.println("FROM CACHE");
        ArrayList<Group[]> groupList = groupCache.get(id);
        Group[] groups = groupList.remove(groupList.size()-1);
        copyGroups(groups[0], groups[1]);
      } else {
        // System.out.println("NEW");
        updateGroup();
      }
      groupId = id;
    }
    double scale = this.radius * getScale() / groupSize;
    double alpha = alive? Math.min(time / 10.0, 1) : 1 - time / 10.0;
    if (scale != groupFront.getScaleX()) {
      groupBack.setScaleX(scale);
      groupBack.setScaleY(scale);
      groupFront.setScaleX(scale);
      groupFront.setScaleY(scale);
    }
    if (alpha != groupFront.getOpacity()) {
      groupFront.setOpacity(alpha);
      groupBack.setOpacity(alpha);
    }
    if (-dir != groupFront.getRotate()) {
      groupFront.setRotate(-dir);
      groupBack.setRotate(-dir);
    }
    groupBack.setTranslateX(pos.x);
    groupBack.setTranslateY(pos.y);
    groupFront.setTranslateX(pos.x);
    groupFront.setTranslateY(pos.y);
  }

  public void updateGroup() {
    // back
    for (int i = 0; i < backGradientLayers; i++) {
      Circle circle = new Circle(0, 0, groupSize * (3 - 1.75 * i / backGradientLayers));
      circle.setFill(Color.color(getOuterColor().getRed(), getOuterColor().getGreen(), getOuterColor().getBlue(), backOpacity));
      groupBack.getChildren().add(circle);
    }
    // front
    RadialGradient grad = new RadialGradient(0, 0, 0, 0, groupSize, false, CycleMethod.NO_CYCLE);
    double[] c1 = new double[] {getOuterColor().getRed(), getOuterColor().getGreen(), getOuterColor().getBlue()};
    double[] c2 = new double[] {getInnerColor().getRed(), getInnerColor().getGreen(), getInnerColor().getBlue()};
    double[] c3 = new double[3];
    for (int i = 0; i <= frontGradientLayers; i++) {
      Circle circle = new Circle(0, 0, groupSize * (1.25 - 0.5 * i / frontGradientLayers));
      for (int j = 0; j < 3; j++)
        c3[j] = c1[j] + (c2[j] - c1[j]) * i / frontGradientLayers;
      circle.setFill(Color.color(c3[0], c3[1], c3[2]));
      groupFront.getChildren().add(circle);
    }
  }

  public double getRenderRadius() {
    return radius * getScale() * 3;
  }

  public final BulletColor getColor() {
    return color;
  }

  public final Color getOuterColor() {
    return color.outerColor;
  }

  public final Color getInnerColor() {
    return color.innerColor;
  }

  public final void setColor(BulletColor color) {
    this.color = color;
  }

  public boolean intersects(Bullet bullet) {
    return pos.distSqd(bullet.pos) < Math.pow(getRenderRadius() + bullet.getRenderRadius(), 2);
  }
}
