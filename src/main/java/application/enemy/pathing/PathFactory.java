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
}
