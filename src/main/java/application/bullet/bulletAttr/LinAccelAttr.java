package application.bullet.bulletAttr;

import application.bullet.bulletTypes.Bullet;

public class LinAccelAttr extends BulletAttr {
  private final double accelAmount;
  private final Double speedCap;
  public LinAccelAttr(String id, double accelAmount, double speedCap) {
    super(id);
    this.accelAmount = accelAmount;
    this.speedCap = speedCap;
  }

  public LinAccelAttr(String id, double accelAmount) {
    super(id);
    this.accelAmount = accelAmount;
    this.speedCap = null;
  }

  public void init(Bullet b) {

  }

  public void prepTick(Bullet b) {
    b.speed += accelAmount;
    if (speedCap != null && ((accelAmount < 0 && b.speed < speedCap) || (accelAmount > 0 && b.speed > speedCap)))
      b.speed = speedCap;
  }

  public void moveTick(Bullet b) {

  }

  public boolean collisionTick(Bullet b) {
    return false;
  }

  public BulletAttr clone() {
    if (speedCap == null)
      return new LinAccelAttr(getId(), accelAmount);
    else
      return new LinAccelAttr(getId(), accelAmount, speedCap);
  }
}
