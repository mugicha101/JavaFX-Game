package application.bullet.bulletAttr;

import application.bullet.bulletTypes.Bullet;

public class LinMoveAttr extends MoveAttr {
  private final double initSpeed;
  private final double initDir;
  private final AccelAttr accelAttr;
  private double speed;
  private double dir;
  public LinMoveAttr(String id, double speed, double dir, AccelAttr accelAttr) {
    super(id);
    initSpeed = speed;
    initDir = dir % 360;
    this.accelAttr = accelAttr == null? null : accelAttr.clone();
  }

  public LinMoveAttr(String id, double speed, double dir) {
    this(id, speed, dir, null);
  }

  public void init(Bullet b) {
    speed = initSpeed;
    dir = initDir;
    b.dir = dir;
  }

  public void prepTick(Bullet b) {
    if (accelAttr != null)
      speed = accelAttr.tick(speed);
  }

  public void moveTick(Bullet b) {
    b.pos.move(dir, speed);
  }

  public boolean collisionTick(Bullet b) {
    return false;
  }

  public MoveAttr clone() {
    return new LinMoveAttr(getId(), initSpeed, initDir, accelAttr);
  }
}
