package application.bullet.bulletAttr;

import application.bullet.bulletTypes.Bullet;

public abstract class MoveAttr extends BulletAttr {
  public MoveAttr(String id) {
    super(id);
  }

  public boolean overridesDefaultBorderCollision() {
    return false;
  }
  public boolean overridesDefaultPlayerCollision() {
    return false;
  }
  public abstract void init(Bullet b); // runs on spawn
  public abstract void prepTick(Bullet b); // before movement
  public abstract void moveTick(Bullet b); // moves bullet
  public abstract boolean collisionTick(Bullet b); // collision checks (returns true if collided)
  public abstract MoveAttr clone();
}
