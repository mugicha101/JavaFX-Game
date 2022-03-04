package application.bullet.attr;

public abstract class ChangeAttr extends BulletAttr {
  public ChangeAttr(String id) {
    super(id);
  }

  public abstract double tick(double value); // returns new value after change

  public ChangeAttr clone() {
    return clone(getId());
  }

  public abstract ChangeAttr clone(String newId);
}
