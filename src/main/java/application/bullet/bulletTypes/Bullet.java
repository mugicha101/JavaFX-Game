package application.bullet.bulletTypes;

import application.Game;
import application.Position;
import application.bullet.BulletColor;
import application.bullet.bulletAttr.BulletAttr;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Bullet {
  private static final int frontGradientLayers = 3;
  private static final int backGradientLayers = 6;
  private boolean alive;
  protected int time;
  public double radius;
  public BulletColor color;
  private final ArrayList<BulletAttr> attrList;
  public final Position pos;
  public double speed;
  public double dir;
  private static int nextId = 0;
  private int id;
  private Group groupBack;
  private Group groupFront;
  private static final double groupSize = 10;
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
    updateGroup();
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
      if (pos.distSqd(Game.player.pos) <= Math.pow(radius + Game.player.hbRadius, 2))
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

  public void drawUpdate() {
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
      Circle circle = new Circle(0, 0, groupSize * (2 - 1.0 * i / backGradientLayers));
      circle.setFill(Color.color(color.outerColor.getRed(), color.outerColor.getGreen(), color.outerColor.getBlue(), 0.3));
      groupBack.getChildren().add(circle);
    }
    // front
    double[] c1 = new double[] {color.outerColor.getRed(), color.outerColor.getGreen(), color.outerColor.getBlue()};
    double[] c2 = new double[] {color.innerColor.getRed(), color.innerColor.getGreen(), color.innerColor.getBlue()};
    double[] c3 = new double[3];
    for (int i = 0; i <= frontGradientLayers; i++) {
      Circle circle = new Circle(0, 0, groupSize * (1.25 - 0.75 * i / frontGradientLayers));
      for (int j = 0; j < 3; j++)
        c3[j] = c1[j] + (c2[j] - c1[j]) * i / frontGradientLayers;
      circle.setFill(Color.color(c3[0], c3[1], c3[2]));
      groupFront.getChildren().add(circle);
    }
  }

  public double getRenderRadius() {
    return radius * getScale() * 3;
  }

  public boolean intersects(Bullet bullet) {
    return pos.distSqd(bullet.pos) < Math.pow(getRenderRadius() + bullet.getRenderRadius(), 2);
  }
}
