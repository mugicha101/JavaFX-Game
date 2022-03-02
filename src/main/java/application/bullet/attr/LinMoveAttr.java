package application.bullet.attr;

import application.bullet.types.Bullet;

import java.util.HashMap;

public class LinMoveAttr extends MoveAttr {
  private final double initSpeed;
  private final double initDir;
  private final AccelAttr accelAttr;
  public double speed;
  public double dir;
  public LinMoveAttr(String id, double speed, double dir, AccelAttr accelAttr) {
    super(id);
    initSpeed = speed;
    initDir = dir % 360;
    this.speed = initSpeed;
    this.dir = initDir;
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
    if (accelAttr != null && accelAttr.enabled)
      speed = accelAttr.tick(speed);
  }

  public void moveTick(Bullet b) {
    b.pos.moveInDir(dir, speed);
  }

  public boolean collisionTick(Bullet b) {
    return false;
  }

  public MoveAttr clone(String newId) {
    return new LinMoveAttr(newId, initSpeed, initDir, accelAttr);
  }

  @Override
  public void toMap(HashMap<String, BulletAttr> map, String prefix) {
    map.put(prefix + getId(), this);
    if (accelAttr != null)
      accelAttr.toMap(map, prefix + getId() + ".");
  }
}
