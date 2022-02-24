package application.bullet.bulletTypes;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.bulletAttr.BulletAttr;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;
import java.util.Arrays;

public class Bullet {
  private boolean alive;
  protected int time;
  public double radius;
  public BulletColor color;
  private ArrayList<BulletAttr> attrList;
  public final Position pos;
  public Bullet(Position pos, double size, BulletColor color, BulletAttr[] attrArr) {
    this.pos = pos.clone();
    alive = true;
    time = 0;
    this.radius = size*12;
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

  public final void kill() {
    if (!alive)
      return;
    alive = false;
    time = 0;
  }

  public final void move() {
    time++;
    if (!alive)
      return;

    // prep

    // move

    // collision
  }

  public void draw(GraphicsContext gc) {
    double radius = this.radius*3;
    RadialGradient grad = new RadialGradient(0, 0, pos.x, pos.y, radius, false, CycleMethod.NO_CYCLE, Arrays.asList(
            new Stop(0, color.innerColor),
            new Stop(0.1, color.innerColor),
            new Stop(0.3, color.outerColor),
            new Stop(0.35, Color.color(color.outerColor.getRed(), color.outerColor.getGreen(), color.outerColor.getBlue(), 0.5)),
            new Stop(0.5, Color.color(color.outerColor.getRed(), color.outerColor.getGreen(), color.outerColor.getBlue(), 0))
    ));
    gc.setFill(grad);
    gc.fillArc(pos.x-radius/2, pos.y-radius/2, radius, radius, 0, 360, ArcType.ROUND);
  }
}
