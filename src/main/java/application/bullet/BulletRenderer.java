package application.bullet;

import application.Game;
import application.bullet.bulletTypes.Bullet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BulletRenderer implements Runnable {
  public final ArrayList<Bullet> bullets;
  public BulletRenderer(List<Bullet> bullets) {
    this.bullets = new ArrayList<>();
    this.bullets.addAll(bullets);
  }

  public BulletRenderer(BulletGroup bg) {
    this(bg.bullets);
  }

  public void run() {
    for (Bullet b : bullets)
      b.drawBack(Game.gc);
    for (Bullet b : bullets)
      b.drawFront(Game.gc);
  }
}
