package application.bullet;

import application.Position;
import application.bullet.bulletTypes.Bullet;

import java.util.Comparator;

public class BulletGroupComparator implements Comparator<BulletGroup> {
  Position center;
  public BulletGroupComparator(BulletGroup bg) {
    center = bg.getCenter();
  }

  public int compare(BulletGroup A, BulletGroup B) {
    double diff = center.distSqd(B.getCenter()) - center.distSqd(A.getCenter());
    if (diff > 0)
      return 1;
    else if (diff < 0)
      return -1;
    else
      return 0;
  }
}
