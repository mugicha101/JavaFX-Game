package application.bullet.bulletAttr;

import application.bullet.bulletTypes.Bullet;

public class LinMoveAttr extends BulletAttr {
  private final double initSpeed;
  private final double initDir;
  public LinMoveAttr(String id, double speed, double dir) {
    super(id);
    initSpeed = speed;
    initDir = dir % 360;
  }

  public void init(Bullet b) {
    b.speed = initSpeed;
    b.moveDir = initDir;
    b.drawDir = initDir;
  }

  public void prepTick(Bullet b) {

  }

  public void moveTick(Bullet b) {
    b.pos.move(b.moveDir, b.speed);
  }

  public boolean collisionTick(Bullet b) {
    return false;
  }

  public BulletAttr clone() {
    return new LinMoveAttr(getId(), initSpeed, initDir);
  }
}
