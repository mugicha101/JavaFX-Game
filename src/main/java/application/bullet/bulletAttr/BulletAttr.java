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

  public boolean overridesDefaultCollision() {
    return false;
  }
  public abstract void init(Bullet b); // runs on spawn
  public abstract void prepTick(Bullet b); // before movement
  public abstract void moveTick(Bullet b); // moves bullet
  public abstract boolean collisionTick(Bullet b); // collision checks (returns true if collided)
  public abstract BulletAttr clone(); // deep clones object
}
