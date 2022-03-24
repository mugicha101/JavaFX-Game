package application.bullet.attr.change;

import application.bullet.attr.Attr;
import application.bullet.attr.bullet.BulletAttr;

import java.util.HashMap;

public class LinChangeAttr extends ChangeAttr {
  public double changeAmount;
  public Double limit;

  public LinChangeAttr(String id, double changeAmount, double limit) {
    super(id);
    this.changeAmount = changeAmount;
    this.limit = limit;
  }

  public LinChangeAttr(String id, double changeAmount) {
    super(id);
    this.changeAmount = changeAmount;
    this.limit = null;
  }

  public double tick(double value) {
    value += changeAmount;
    if (limit != null
        && ((changeAmount < 0 && value < limit) || (changeAmount > 0 && value > limit)))
      value = limit;
    return value;
  }

  public LinChangeAttr clone(String newId) {
    if (limit == null) return new LinChangeAttr(newId, changeAmount);
    else return new LinChangeAttr(newId, changeAmount, limit);
  }

  @Override
  public void toMap(HashMap<String, Attr> map, String prefix) {
    map.put(prefix + getId(), this);
  }
}
