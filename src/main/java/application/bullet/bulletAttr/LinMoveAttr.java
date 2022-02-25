package application.bullet.bulletAttr;

import application.bullet.bulletTypes.Bullet;

public class LinMoveAttr extends BulletAttr {
  private double initSpeed;
  private double initDir;
  public LinMoveAttr(String id, double speed, double dir) {
    super(id);
    initSpeed = speed;
    initDir = dir;
  }

  public void init(Bullet b) {
    b.speed = initSpeed;
    b.dir = initDir;
  }

  public void prepTick(Bullet b) {

  }

  public void moveTick(Bullet b) {
    b.pos.move(b.dir, b.speed);
  }

  public boolean collisionTick(Bullet b) {
    return false;
  }

  public BulletAttr clone() {
    return new LinMoveAttr(getId(), initSpeed, initDir);
  }
}
