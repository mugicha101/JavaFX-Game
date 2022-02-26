package application.bullet;

import application.Position;
import application.bullet.bulletTypes.Bullet;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class BulletGroup {
  private final ArrayList<Bullet> bullets;
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

  public ArrayList<Bullet> getBullets() {
    if (needsUpdate) {
      needsUpdate = false;
      bullets.sort(new BulletComparator());
    }
    return bullets;
  }
}
