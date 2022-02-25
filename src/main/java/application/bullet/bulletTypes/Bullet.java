package application.bullet.bulletTypes;

import application.Game;
import application.Position;
import application.bullet.BulletColor;
import application.bullet.bulletAttr.BulletAttr;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public class Bullet {
  private static final int frontGradientLayers = 2;
  private static final int backGradientLayers = 3;
  private boolean alive;
  protected int time;
  public double radius;
  public BulletColor color;
  private final ArrayList<BulletAttr> attrList;
  public final Position pos;
  public double speed;
  public double dir;
  public Bullet(Position pos, double size, BulletColor color, BulletAttr[] attrArr) {
    this.pos = pos.clone();
    alive = true;
    time = 0;
    this.radius = size*5;
    this.color = color;
    attrList = new ArrayList<>();
    for (BulletAttr ba : attrArr) {
      attrList.add(ba.clone());
    }
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
      else if (pos.x > Game.width + radius*3 || pos.y > Game.height + radius*3 || pos.x < -radius*3 || pos.y < -radius*3)
        this.kill(true);
    }
  }

  protected double getScale() {
    if (!alive)
      return 1 - time / 10.0;
    else if (time < 10)
      return 2.0 / (1 + time * 0.2);
    else
      return 1;
  }

  public void drawBack(GraphicsContext gc) {
    double radius = this.radius * getScale();
    double alpha = alive? 1 : time / 10.0;
    gc.setFill(color.outerColor);
    gc.setGlobalAlpha(alpha*0.2);
    for (int i = 0; i < backGradientLayers; i++) {
      double size = radius * (2 - 1.0 * i / backGradientLayers);
      gc.fillArc(pos.x - size, pos.y - size, size * 2, size * 2, 0, 360, ArcType.ROUND);
    }
  }

  public void drawFront(GraphicsContext gc) {
    double radius = this.radius * getScale();
    double alpha = alive? 1 : time / 10.0;
    /*
    RadialGradient grad = new RadialGradient(0, 0, pos.x, pos.y, radius, false, CycleMethod.NO_CYCLE, Arrays.asList(
            new Stop(0, color.innerColor),
            new Stop(0.1, color.innerColor),
            new Stop(0.3, color.outerColor),
            new Stop(0.35, Color.color(color.outerColor.getRed(), color.outerColor.getGreen(), color.outerColor.getBlue(), 0.5)),
            new Stop(0.5, Color.color(color.outerColor.getRed(), color.outerColor.getGreen(), color.outerColor.getBlue(), 0))
    ));
    gc.setFill(grad);
    gc.fillArc(pos.x-radius, pos.y-radius, radius*2, radius*2, 0, 360, ArcType.ROUND);
     */
    double[] c1 = new double[] {color.outerColor.getRed(), color.outerColor.getGreen(), color.outerColor.getBlue()};
    double[] c2 = new double[] {color.innerColor.getRed(), color.innerColor.getGreen(), color.innerColor.getBlue()};
    double[] c3 = new double[3];
    gc.setGlobalAlpha(alpha);
    for (int i = 0; i <= frontGradientLayers; i++) {
      for (int j = 0; j < 3; j++)
        c3[j] = c1[j] + (c2[j] - c1[j]) * i / frontGradientLayers;
      gc.setFill(Color.color(c3[0], c3[1], c3[2]));
      double size = radius * (1.25 - 0.75 * i / frontGradientLayers);
      gc.fillArc(pos.x - size, pos.y - size, size * 2, size * 2, 0, 360, ArcType.ROUND);
    }
    gc.setGlobalAlpha(1);
  }

  public double getRenderRadius() { // radius of front render
    return radius * getScale() * 1.25;
  }

  public boolean intersects(Bullet bullet) {
    return pos.distSqd(bullet.pos) < Math.pow(getRenderRadius() + bullet.getRenderRadius(), 2);
  }
}
