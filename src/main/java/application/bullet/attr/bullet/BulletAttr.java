package application.bullet.attr.bullet;

import application.bullet.attr.Attr;
import application.bullet.types.Bullet;

public abstract class BulletAttr extends Attr {
  public BulletAttr(String id) {
    super(id);
  }

  public boolean overridesDefaultBorderCollision() {
    return false;
  }

  public boolean overridesDefaultPlayerCollision() {
    return false;
  }

  public abstract void init(
          Bullet b); // runs on first frame of movement (does not run if disabled by stages on start,
  // internal fields should be set in constructor)

  public abstract void prepTick(Bullet b); // before movement

  public abstract void moveTick(Bullet b); // moves bullet

  public abstract boolean collisionTick(Bullet b); // collision checks (returns true if collided)

  public BulletAttr clone() {
    return clone(getId());
  }

  public abstract BulletAttr clone(String newId);
}
