package application.patterns;

import application.Game;
import application.Position;

import java.util.Random;

public class Pattern {
  private final String name;
  private int cycle;
  public Position pos;
  private BulletSpawn spawn;

  public Pattern(String name, Position pos, BulletSpawn spawn) {
    this.name = name;
    this.pos = pos == null? new Position(0, 0) : pos;
    this.spawn = spawn;
    cycle = 0;
  }

  public void init() {
    cycle = 0;
  }

  public final String getName() {
    return name;
  }

  public final void moveTick() {
    tick(cycle, Game.width, Game.height);
    cycle++;
  }

  protected void tick(int cycle, double width, double height) {
    spawn.run(cycle, pos, width, height);
  }

  public Pattern clone() {
    return new Pattern(name, pos.clone(), spawn);
  }
}
