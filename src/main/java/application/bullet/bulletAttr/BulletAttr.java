package application.bullet.bulletAttr;

import application.bullet.bulletTypes.Bullet;

public abstract class BulletAttr {
  private String id;
  public String getId() {
    return id;
  }

  public BulletAttr(String id) {
    this.id = id;
  }

  public BulletAttr() {
    this.id = null;
  }

  public boolean overridesDefaultCollision() {
    return false;
  }
  public abstract void prepTick(Bullet bullet); // before movement
  public abstract void moveTick(Bullet bullet); // moves bullet
  public abstract boolean collisionTick(); // collision checks (returns true if collided)
  public abstract BulletAttr clone(); // deep clones object
}
