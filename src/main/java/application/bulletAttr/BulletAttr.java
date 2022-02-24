package application.bulletAttr;

import application.bullet.Bullet;

public abstract class BulletAttr {
  public final boolean overrideDefaultMovement = false;
  public final boolean overrideDefaultCollision = false;
  public abstract void prepTick(Bullet bullet); // before movement
  public abstract void moveTick(Bullet bullet); // moves bullet
  public abstract boolean collisionTick(); // collision checks (returns true if collided)
}
