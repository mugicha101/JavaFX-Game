package application.bullet.attr;

import java.util.HashMap;

public abstract class AccelAttr extends BulletAttr {
  public AccelAttr(String id) {
    super(id);
  }

  public abstract double tick(double speed); // returns new speed

  public AccelAttr clone() {
    return clone(getId());
  }

  public abstract AccelAttr clone(String newId);
}
