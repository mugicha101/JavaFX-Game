package application.bullet;

import java.util.Comparator;

public class BulletGroupComparator implements Comparator<BulletGroup> {
  public BulletGroupComparator() {
    super();
  }
  public int compare(BulletGroup A, BulletGroup B) {
    return B.size() - A.size();
  }
}
