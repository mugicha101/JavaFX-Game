package application.pattern;

import application.Position;

public interface BulletSpawn {
  void run(int time, Position pos, double width, double height);
}
