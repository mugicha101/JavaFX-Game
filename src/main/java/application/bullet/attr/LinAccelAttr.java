package application.bullet.attr;

import java.util.HashMap;

public class LinAccelAttr extends AccelAttr {
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

  public double tick(double speed) {
    speed += accelAmount;
    if (speedCap != null && ((accelAmount < 0 && speed < speedCap) || (accelAmount > 0 && speed > speedCap)))
      speed = speedCap;
    return speed;
  }

  public AccelAttr clone(String newId) {
    if (speedCap == null)
      return new LinAccelAttr(newId, accelAmount);
    else
      return new LinAccelAttr(newId, accelAmount, speedCap);
  }

  @Override
  public void toMap(HashMap<String, BulletAttr> map, String prefix) {
    map.put(prefix + getId(), this);
  }
}
