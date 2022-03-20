package application.bullet.attr.change;

import application.bullet.attr.Attr;

public abstract class ChangeAttr extends Attr {
  public ChangeAttr(String id) {
    super(id);
  }

  public abstract double tick(double value); // returns new value after change

  public ChangeAttr clone() {
    return clone(getId());
  }

  public abstract ChangeAttr clone(String newId);
}
