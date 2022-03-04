package application.bullet.attr;

import java.util.HashMap;

public class SmoothChangeAttr extends ChangeAttr {
  public double multi;
  public double limit;

  public SmoothChangeAttr(String id, double multi, double limit) {
    super(id);
    this.multi = multi;
    this.limit = limit;
  }

  public double tick(double value) {
    return value + (limit - value) * multi;
  }

  public ChangeAttr clone(String newId) {
    return new SmoothChangeAttr(newId, multi, limit);
  }

  @Override
  public void toMap(HashMap<String, BulletAttr> map, String prefix) {
    map.put(prefix + getId(), this);
  }
}
