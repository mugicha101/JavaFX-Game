package application.bullet;

import application.Position;
import application.bullet.bulletTypes.Bullet;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class BulletGroup {
  public final ArrayList<Bullet> bullets;
  private Circle bounds;
  private boolean needsUpdate;
  public BulletGroup() {
    bullets = new ArrayList<>() {};
    needsUpdate = false;
    bounds = null;
  }

  public BulletGroup(Bullet bullet) {
    this();
    addBullet(bullet);
  }

  public int size() {
    return bullets.size();
  }

  public void addBullet(Bullet bullet) {
    needsUpdate = true;
    bullets.add(bullet);
  }

  public void merge(BulletGroup group) {
    if (group.size() == 0)
      return;
    if (group.size() > 1000)
      throw new RuntimeException("Group Size Too Large: (size=" + group.size() + ")");
    needsUpdate = true;
    bullets.addAll(group.bullets);
  }

  public Circle getBounds() { // TODO: Use Circles Instead
    if (needsUpdate) {
      double x = 0, y = 0;
      // find average position
      for (Bullet b : bullets) {
        x += b.pos.x;
        y += b.pos.y;
      }
      x /= bullets.size();
      y /= bullets.size();

      // get max distance from position
      double maxDist = 0;
      for (Bullet b : bullets) {
        double dist = b.pos.dist(x, y) + b.getRenderRadius();
        if (dist > maxDist)
          maxDist = dist;
      }

      // set circle
      bounds = new Circle(x, y, maxDist);
      needsUpdate = false;
    }
    return bounds;
  }

  public boolean intersects(BulletGroup bg) {
    return intersects(bg.getBounds());
  }

  public boolean intersects(Circle bounds) {
    if (bounds == null || getBounds() == null)
      return false;
    return getCenter().distSqd(bounds.getCenterX(), bounds.getCenterY()) < Math.pow(getBounds().getRadius() + bounds.getRadius(), 2);
  }

  public Position getCenter() {
    Circle c = getBounds();
    return new Position(c.getCenterX(), c.getCenterY());
  }
}
