package application.enemy.types;

import application.Game;
import application.Position;
import application.sprite.Sprite;
import application.enemy.pathing.*;
import application.patterns.Pattern;

import java.util.ArrayList;

public class Enemy {
  private static ArrayList<Enemy> enemyList = new ArrayList<>();

  public static void spawn(Enemy enemy) { // spawns an enemy from an inactive template enemy
    Enemy enemyClone = enemy.clone();
    enemyClone.activate();
  }

  public static void moveEnemies() {
    ArrayList<Enemy> aliveEnemies = new ArrayList<>();
    for (Enemy e : enemyList) {
      e.move();
      if (e.isAlive())
        aliveEnemies.add(e);
      else
        e.delete();
    }
    enemyList = aliveEnemies;
  }

  public static void drawEnemies() {
    for (Enemy e : enemyList)
      e.drawUpdate();
  }

  private int time;
  private final int lifetime;
  private final Path path;
  public final Position pos;
  private final Sprite spriteSource;
  public Sprite sprite;
  public final double maxHealth;
  public double health;
  private boolean active;
  private final Pattern pattern;

  public Enemy(int lifetime, Path path, Sprite sprite, double health, Pattern pattern) {
    time = 0;
    this.lifetime = lifetime;
    this.path = path;
    pos = path.pos(0);
    this.spriteSource = sprite;
    maxHealth = health;
    this.health = health;
    active = false;
    this.pattern = pattern == null? null : pattern.clone();
    if (this.pattern != null)
      this.pattern.pos = pos;
  }

  public Enemy(int lifetime, Path path, Sprite sprite, double health) {
    this(lifetime, path, sprite, health, null);
  }

  public void activate() { // converts template enemy to active enemy
    if (active)
      return;
    active = true;
    this.sprite = spriteSource.clone();
    this.sprite.pos = pos;
    this.sprite.enable();
    enemyList.add(this);
  }

  public final boolean isAlive() {
    return health > 0 && time <= lifetime;
  }

  public final void move() {
    time++;
    pos.set(path.pos(time));
    if (pattern != null) pattern.moveTick();
  }

  public final void drawUpdate() {
    sprite.drawUpdate();
  }

  public void delete() {
    sprite.disable();
  }

  public Enemy clone() {
    return new Enemy(lifetime, path, spriteSource, maxHealth, pattern.clone());
  }
}
