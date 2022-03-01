package application.bullet.bulletAttr;

public abstract class AccelAttr {
  public abstract double tick(double speed); // returns new speed
  public abstract AccelAttr clone();
}
