package application.enemy.types;

import application.Position;
import application.Sprite;
import application.enemy.pathing.*;

import java.util.ArrayList;

public class Enemy {
  private static ArrayList<Enemy> enemyList = new ArrayList<>();

  public static void spawn(Enemy enemy) { // spawns an enemy from an inactive template enemy
    Enemy enemyClone = enemy.clone();
    enemyClone.activate();
    enemyList.add(enemyClone);
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

  public Enemy(int lifetime, Path path, Sprite sprite, double health) {
    time = 0;
    this.lifetime = lifetime;
    this.path = path;
    pos = path.pos(0);
    this.spriteSource = sprite;
    this.sprite = null;
    maxHealth = health;
    this.health = health;
    active = false;
  }

  public void activate() { // converts template enemy to active enemy
    if (active)
      return;
    active = true;
    this.sprite = spriteSource.clone();
    enemyList.add(this);
  }

  public final boolean isAlive() {
    return health > 0 && time <= lifetime;
  }

  public final void move() {
    time++;
    pos.set(path.pos(time));
  }

  public final void drawUpdate() {
    sprite.drawUpdate();
  }

  public void delete() {
    sprite.delete();
  }

  public Enemy clone() {
    return new Enemy(lifetime, path, sprite, maxHealth);
  }
}
