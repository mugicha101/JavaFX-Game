package application;

public class DirCalc {
  public static double dirTo(double x1, double y1, double x2, double y2) {
    return Math.atan2(y1 - y2, x2 - x1) * 180 / Math.PI;
  }
  public static double dirTo(Position origin, Position target) {
    return dirTo(origin.x, origin.y, target.x, target.y);
  }

  public static double dirTo(double x, double y) {
    return dirTo(0, 0, x, y);
  }

  public static double dirTo(Position pos) {
    return dirTo(0, 0, pos.x, pos.y);
  }

  public static double dirToPlayer(Position pos) {
    return dirTo(pos, Game.player.pos);
  }
}
