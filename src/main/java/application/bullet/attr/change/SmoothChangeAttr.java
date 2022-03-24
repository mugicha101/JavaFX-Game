package application.bullet.attr.change;

import application.bullet.attr.Attr;
import application.bullet.attr.bullet.BulletAttr;

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

  public SmoothChangeAttr clone(String newId) {
    return new SmoothChangeAttr(newId, multi, limit);
  }

  @Override
  public void toMap(HashMap<String, Attr> map, String prefix) {
    map.put(prefix + getId(), this);
  }
}
