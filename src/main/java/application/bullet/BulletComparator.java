package application.bullet;

import application.bullet.bulletTypes.Bullet;

import java.util.Comparator;

public class BulletComparator implements Comparator<Bullet> {
  public BulletComparator() {
    super();
  }
  public int compare(Bullet A, Bullet B) {
    return B.id() - A.id();
  }
}
