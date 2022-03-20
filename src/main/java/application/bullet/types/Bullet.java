package application.bullet.types;

import application.Game;
import application.Position;
import application.bullet.BulletColor;
import application.bullet.attr.Attr;
import application.bullet.attr.bullet.BulletAttr;
import application.bullet.staging.AttrStage;
import application.bullet.staging.BlankStage;
import application.bullet.staging.BulletStage;
import application.bullet.staging.ModifyStage;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Bullet {
  public static final boolean hqBullets = true;
  private static ArrayList<Bullet> bullets = new ArrayList<>();
  protected static final double backOpacity = 0.3;
  public static final int offscreenMargin = 0;
  private boolean alive;
  protected int time;
  private final double radius; // spawn radius
  public double scale; // scaling since spawned
  private BulletColor color;
  private final ArrayList<BulletAttr> attrList;
  public final Position pos;
  public double dir; // direction bullet is facing (not necessarily direction its moving)
  protected Group groupBack;
  protected Group groupFront;
  protected static final double groupSize = 10;
  private String groupId;
  private final ArrayList<BulletStage> stageList;
  private int stageIndex;
  private int stageTime;
  private final HashMap<String, Attr> attrMap;

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

  public Bullet(Position pos, double size, BulletColor color, BulletAttr[] attrArr, BulletStage[] stageArr) {
    this.pos = pos.clone();
    alive = true;
    time = 0;
    this.radius = size*8;
    this.scale = 1;
    this.color = color;
    attrList = new ArrayList<>();
    attrMap = new HashMap<>();
    for (BulletAttr ba : attrArr) {
      BulletAttr baClone = ba.clone();
      attrList.add(baClone);
      baClone.toMap(attrMap, "");
    }
    if (stageArr == null)
      stageList = null;
    else {
      stageList = new ArrayList<>();
      stageList.addAll(Arrays.stream(stageArr).toList());
    }
    groupFront = new Group();
    groupBack = new Group();
    Game.bulletGroupBack.getChildren().add(groupBack);
    Game.bulletGroupFront.getChildren().add(groupFront);
    groupId = null;
    stageIndex = 0;
    Bullet.bullets.add(this);
  }

  public Bullet(Position pos, double size, BulletColor color, BulletAttr[] attrArr) {
    this(pos, size, color, attrArr, null);
  }

  public String getType() {
    return "normal";
  }

  public final Attr getAttr(String id) {
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
    Game.bulletGroupBack.getChildren().remove(groupBack);
    Game.bulletGroupFront.getChildren().remove(groupFront);
  }

  public final void move() {
    time++;
    if (!alive)
      return;

    // init
    if (time == 1) {
      for (BulletAttr ba : attrList)
        ba.init(this);
    }

    // update stages
    if (stageList != null && stageIndex < stageList.size()) {
      stageTime++;
      while (stageTime >= stageList.get(stageIndex).getTime()) {
        BulletStage baseStage = stageList.get(stageIndex);
        if (baseStage instanceof BlankStage stage) {
          stage.action();
        } else if (baseStage instanceof AttrStage stage) {
          Attr attr = getAttr(stage.getId());
          if (attr == null)
            throw new NullPointerException(
                "null BulletAttr instance assigned to stage (id="
                    + stage.getId()
                    + " is not in available ids: "
                    + attrMap.keySet());
          stage.action(attr);
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
    for (BulletAttr ba : attrList)
      if (ba.enabled)
        ba.prepTick(this);

    // move
    for (BulletAttr ba : attrList)
      if (ba.enabled)
       ba.moveTick(this);

    // collision
    boolean defaultPlayerCollision = true;
    boolean defaultBorderCollision = true;
    for (BulletAttr ba : attrList) {
      if (!ba.enabled)
        continue;
      if (ba.overridesDefaultPlayerCollision())
        defaultPlayerCollision = false;
      if (ba.overridesDefaultBorderCollision())
        defaultBorderCollision = false;
      if (ba.collisionTick(this)) {
        this.kill();
        break;
      }
    }
    if (alive) {
      if (defaultPlayerCollision && (time >= 10 && pos.distSqd(Game.player.pos) <= Math.pow(radius * scale + Game.player.getStats().hitboxRadius, 2)))
        this.kill();
      else if (defaultBorderCollision && finishedStages() && (pos.x > Game.width + offscreenMargin + getRenderRadius() || pos.y > Game.height + offscreenMargin + getRenderRadius() || pos.x < -getRenderRadius() - offscreenMargin || pos.y < -getRenderRadius() - offscreenMargin))
        this.kill(true);
    }
  }

  protected double getVisualScale() {
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

  public final void drawUpdate() {
    String id = generateGroupId();
    if (groupId == null || !groupId.equals(id)) {
      updateGroup();
      groupId = id;
    }
    double scale = this.radius * this.scale * getVisualScale() / groupSize;
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
    double[] c1 = new double[] {getOuterColor().getRed(), getOuterColor().getGreen(), getOuterColor().getBlue()};
    double[] c2 = new double[] {getInnerColor().getRed(), getInnerColor().getGreen(), getInnerColor().getBlue()};
    if (groupBack.getChildren().size() == 0) {
      groupBack.getChildren().add(new Circle(0, 0, groupSize * 3));
      groupFront.getChildren().add(new Circle(0, 0, groupSize * 1.25));
      if (!hqBullets)
        groupFront.getChildren().add(new Circle(0, 0, groupSize * 0.75));
    }
    if (hqBullets) {
      Circle circle = (Circle)groupBack.getChildren().get(0);
      circle.setFill(new RadialGradient(0, 0, 0, 0, groupSize * 3, false, CycleMethod.NO_CYCLE, new Stop(0.3, Color.color(c1[0], c1[1], c1[2], backOpacity)), new Stop(1, Color.color(c1[0], c1[1], c1[2], 0))));
      circle = (Circle)groupFront.getChildren().get(0);
      circle.setFill(new RadialGradient(0, 0, 0, 0, groupSize * 1.25, false, CycleMethod.NO_CYCLE, new Stop(0.5, color.innerColor), new Stop(0.8, color.outerColor), new Stop(0.9, color.outerColor), new Stop(1, Color.color(c1[0], c1[1], c1[2], 0))));
    } else {
      ((Circle)groupBack.getChildren().get(0)).setFill(Color.color(c1[0], c1[1], c1[2], 0.2));
      ((Circle)groupFront.getChildren().get(0)).setFill(color.outerColor);
      ((Circle)groupFront.getChildren().get(1)).setFill(color.innerColor);
    }
  }

  public double getRenderRadius() {
    return radius * getVisualScale() * 3 * scale;
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
