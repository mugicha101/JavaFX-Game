package application.bullet.bulletTypes;

import application.Game;
import application.Position;
import application.bullet.BulletColor;
import application.bullet.bulletAttr.BulletAttr;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Bullet {
  protected static final int frontGradientLayers = 3;
  protected static final int backGradientLayers = 5;
  protected static final double backOpacity = 0.1;
  private boolean alive;
  protected int time;
  public double radius;
  private BulletColor color;
  private final ArrayList<BulletAttr> attrList;
  public final Position pos;
  public double speed;
  public double dir;
  private static int nextId = 0;
  private int id;
  protected Group groupBack;
  protected Group groupFront;
  protected static final double groupSize = 10;
  private boolean needsUpdate = true;
  public Bullet(Position pos, double size, BulletColor color, BulletAttr[] attrArr) {
    this.pos = pos.clone();
    alive = true;
    time = 0;
    this.radius = size*8;
    this.color = color;
    attrList = new ArrayList<>();
    for (BulletAttr ba : attrArr) {
      attrList.add(ba.clone());
    }
    id = nextId++;
    groupFront = new Group();
    groupBack = new Group();
    needsUpdate = true;
    Game.bulletGroupBack.getChildren().add(groupBack);
    Game.bulletGroupFront.getChildren().add(groupFront);
  }

  public final int id() {
    return id;
  }

  public final boolean isAlive() {
    return alive;
  }

  public final int getTime() {
    return time;
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

    // prep
    for (BulletAttr ba : attrList)
      ba.prepTick(this);

    // move
    for (BulletAttr ba : attrList)
      ba.moveTick(this);

    // collision
    boolean defaultCollision = true;
    for (BulletAttr ba : attrList) {
      if (ba.overridesDefaultCollision())
        defaultCollision = false;
      if (ba.collisionTick(this)) {
        this.kill();
        break;
      }
    }
    if (alive && defaultCollision) {
      if (time >= 10 && pos.distSqd(Game.player.pos) <= Math.pow(radius + Game.player.hbRadius, 2))
        this.kill();
      else if (pos.x > Game.width + getRenderRadius() || pos.y > Game.height + getRenderRadius() || pos.x < -getRenderRadius() || pos.y < -getRenderRadius())
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

  public final void drawUpdate() {
    if (needsUpdate) {
      updateGroup();
      needsUpdate = false;
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
    groupBack.getChildren().clear();
    groupFront.getChildren().clear();
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
    needsUpdate = true;
  }

  public boolean intersects(Bullet bullet) {
    return pos.distSqd(bullet.pos) < Math.pow(getRenderRadius() + bullet.getRenderRadius(), 2);
  }
}
