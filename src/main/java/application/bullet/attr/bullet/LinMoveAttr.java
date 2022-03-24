package application.bullet.attr.bullet;

import application.bullet.attr.Attr;
import application.bullet.attr.change.ChangeAttr;
import application.bullet.types.Bullet;

import java.util.HashMap;

public class LinMoveAttr extends BulletAttr {
  public double initSpeed;
  public double initDir;
  public ChangeAttr accelAttr;
  public double speed;
  public double dir;

  public LinMoveAttr(String id, double speed, double dir, ChangeAttr accelAttr) {
    super(id);
    initSpeed = speed;
    initDir = dir % 360;
    this.speed = initSpeed;
    this.dir = initDir;
    this.accelAttr = accelAttr == null ? null : accelAttr.clone();
  }

  public LinMoveAttr(String id, double speed, double dir) {
    this(id, speed, dir, null);
  }

  public void init(Bullet b) {
    speed = initSpeed;
    dir = initDir;
  }

  public void prepTick(Bullet b) {
    if (accelAttr != null && accelAttr.enabled) speed = accelAttr.tick(speed);
  }

  public void moveTick(Bullet b) {
    b.dir = dir;
    b.pos.moveInDir(dir, speed);
  }

  public boolean collisionTick(Bullet b) {
    return false;
  }

  public LinMoveAttr clone(String newId) {
    return new LinMoveAttr(newId, initSpeed, initDir, accelAttr);
  }

  @Override
  public void toMap(HashMap<String, Attr> map, String prefix) {
    map.put(prefix + getId(), this);
    if (accelAttr != null) accelAttr.toMap(map, prefix + getId() + ".");
  }
}
