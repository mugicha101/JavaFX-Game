package application.enemy.pathing;

import application.Position;

public class PathFactory {
  public Path linearPath(Position startPos, Position endPos, int time) {
    return (t) -> new Position(startPos.x + (endPos.x - startPos.x) * t / time, startPos.y + (endPos.y - startPos.y) * t / time);
  }
}
