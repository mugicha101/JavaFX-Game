package application.patterns;

import application.Game;

import java.util.HashSet;
import java.util.Random;

public abstract class Pattern {
  private final String name;
  private final double maxHealth;
  private double health;
  private int cycle;
  private static final HashSet<Pattern> patternSet = new HashSet<>();
  protected final Random rand;

  public static void patternTick() {
    for (Pattern pattern : patternSet)
      pattern.spawnTick();
  }

  public static void removePattern(Pattern pattern) {
    patternSet.remove(pattern);
  }

  public Pattern(String name, double health) {
    this.name = name;
    maxHealth = health;
    health = 0;
    cycle = 0;
    patternSet.add(this);
    rand = new Random();
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
    if (amount < 0)
      return;
    health -= amount;
  }

  public final double getHealthProportion() {
    return health/maxHealth;
  }

  public final void remove() {
    Pattern.removePattern(this);
  }

  public final void spawnTick() {
    cycle++;
    tick(cycle, Game.width, Game.height);
  }

  protected abstract void tick(int cycle, double width, double height);
}
