package application.enemy.types;

import application.Position;
import application.Sprite;
import application.enemy.pathing.*;

public class Enemy {
  private int time;
  private final int lifetime;
  private final Path path;
  public final Position pos;
  public final Sprite sprite;
  public Enemy(int lifetime, Path path, Sprite sprite) {
    time = 0;
    this.lifetime = lifetime;
    this.path = path;
    pos = path.pos(0);
    this.sprite = sprite.clone();
    this.sprite.pos = pos;
  }

  public final boolean lifetimeOver() {
    return time >= lifetime;
  }

  public final void move() {
    time++;
    pos.set(path.pos(time));
  }
}
