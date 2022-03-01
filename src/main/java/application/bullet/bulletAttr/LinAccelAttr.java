package application.bullet.bulletAttr;

import application.bullet.bulletTypes.Bullet;

public class LinAccelAttr extends AccelAttr {
  private final double accelAmount;
  private final Double speedCap;
  public LinAccelAttr(double accelAmount, double speedCap) {
    this.accelAmount = accelAmount;
    this.speedCap = speedCap;
  }

  public LinAccelAttr(double accelAmount) {
    this.accelAmount = accelAmount;
    this.speedCap = null;
  }

  public double tick(double speed) {
    speed += accelAmount;
    if (speedCap != null && ((accelAmount < 0 && speed < speedCap) || (accelAmount > 0 && speed > speedCap)))
      speed = speedCap;
    return speed;
  }

  public AccelAttr clone() {
    if (speedCap == null)
      return new LinAccelAttr(accelAmount);
    else
      return new LinAccelAttr(accelAmount, speedCap);
  }
}
