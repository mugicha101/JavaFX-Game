package application.bullet.staging;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.types.Bullet;

public class ModifyFactory {
  public static Modify setDir(double dir) {
    return (b) -> b.dir = dir % 360;
  }
  public static Modify changeDir(double dir) {
    return (b) -> b.dir = (b.dir + dir + 360) % 360;
  }

  public static Modify setPos(Position pos) {
    return (b) -> b.pos.set(pos);
  }

  public static Modify setPos(double x, double y) {
    return setPos(new Position(x, y));
  }

  public static Modify changePos(Position pos) {
    return (b) -> b.pos.move(pos);
  }

  public static Modify changePos(double x, double y) {
    return changePos(new Position(x, y));
  }



  public static Modify setColor(BulletColor color) {
    return (b) -> b.setColor(color);
  }

  public static Modify kill() {
    return Bullet::kill;
  }
}
