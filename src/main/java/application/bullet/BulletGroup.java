package application.bullet;

import application.Position;
import application.bullet.bulletTypes.Bullet;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

public class BulletGroup {
  public final ArrayList<Bullet> bullets;
  private Rectangle2D bounds;
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

  public Rectangle2D getBounds() {
    if (needsUpdate) {
      double minX = Double.MAX_VALUE;
      double maxX = Double.MIN_VALUE;
      double minY = Double.MAX_VALUE;
      double maxY = Double.MIN_VALUE;
      for (Bullet b : bullets) {
        double radius = b.getRenderRadius();
        if (b.pos.x - radius < minX)
          minX = b.pos.x - radius;
        if (b.pos.x + radius > maxX)
          maxX = b.pos.x + radius;
        if (b.pos.y - radius < minY)
          minY = b.pos.y - radius;
        if (b.pos.y + radius > maxY)
          maxY = b.pos.y + radius;
      }
      bounds = minX == Double.MAX_VALUE? null : new Rectangle2D(minX, minY, maxX-minX, maxY-minY);
      needsUpdate = false;
    }
    return bounds;
  }

  public boolean intersects(BulletGroup bg) {
    return intersects(bg.getBounds());
  }

  public boolean intersects(Rectangle2D bounds) {
    if (bounds == null || getBounds() == null)
      return false;
    return getBounds().intersects(bounds);
  }

  public Position getCenter() {
    Rectangle2D rect = getBounds();
    return new Position(rect.getMinX() + rect.getWidth() * 0.5, rect.getMinY() + rect.getHeight() * 0.5);
  }
}
