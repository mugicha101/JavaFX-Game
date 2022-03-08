package application.patterns;

import application.Game;
import application.Position;

import java.util.HashSet;
import java.util.Random;

public abstract class Pattern {
  private final String name;
  private final double maxHealth;
  private double health;
  private int cycle;
  protected final Random rand;
  public Position pos;

  public Pattern(String name, double health, Position pos) {
    this.name = name;
    maxHealth = health;
    health = 0;
    cycle = 0;
    rand = new Random();
    this.pos = pos == null? new Position(0, 0) : pos;
  }

  public void init() {
    health = maxHealth;
    cycle = 0;
  }

  public final String getName() {
    return name;
  }

  public final double getMaxHealth() {
    return maxHealth;
  }

  public final double getHealth() {
    return health;
  }

  public final void reduceHealth(int amount) {
    if (amount < 0) return;
    health -= amount;
  }

  public final double getHealthProportion() {
    return health / maxHealth;
  }

  public final void moveTick() {
    tick(cycle, Game.width, Game.height);
    cycle++;
  }

  protected abstract void tick(int cycle, double width, double height);

  public abstract Pattern clone();
}
