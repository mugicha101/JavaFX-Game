package application.bullet;

import application.Game;
import application.bullet.bulletTypes.Bullet;

import java.util.ArrayList;
import java.util.List;

public class BulletRenderThread implements Runnable {
  public final ArrayList<Bullet> bullets;
  public BulletRenderThread(List<Bullet> bullets) {
    this.bullets = new ArrayList<>();
    this.bullets.addAll(bullets);
  }

  public BulletRenderThread(BulletGroup bg) {
    this(bg.getBullets());
  }

  public void run() {
    for (Bullet b : bullets)
      b.drawBack(Game.gc);
    for (Bullet b : bullets)
      b.drawFront(Game.gc);
  }
}
