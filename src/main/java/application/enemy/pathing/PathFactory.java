package application.enemy.pathing;

import application.Position;

public class PathFactory {
  public static Path linearPath(Position startPos, Position endPos, int time) {
    return (t) ->
            new Position(
                    startPos.x + (endPos.x - startPos.x) * t / time,
                    startPos.y + (endPos.y - startPos.y) * t / time);
  }

  public static Path linearPath(double x1, double y1, double x2, double y2, int time) {
    return linearPath(new Position(x1, y1), new Position(x2, y2), time);
  }

  public static Path convergePath(Position startPos, Position endPos, double speed) {
    return (t) ->
            new Position(
                    endPos.x - (endPos.x - startPos.x) / (t * speed + 1),
                    endPos.y - (endPos.y - startPos.y) / (t * speed + 1));
  }

  public static Path convergePath(double x1, double y1, double x2, double y2, double speed) {
    return convergePath(new Position(x1, y1), new Position(x2, y2), speed);
  }
}
