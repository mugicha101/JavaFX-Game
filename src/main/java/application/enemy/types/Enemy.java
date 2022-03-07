package application.enemy.types;

import application.Position;
import application.Sprite;
import application.bullet.types.Bullet;
import application.enemy.pathing.*;

import java.util.ArrayList;

public class Enemy {
  private static ArrayList<Enemy> enemyList = new ArrayList<>();

  public static void spawn(Enemy enemy) {
    enemyList.add(enemy);
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
  public final Sprite sprite;
  public final double maxHealth;
  public double health;

  public Enemy(int lifetime, Path path, Sprite sprite, double health) {
    time = 0;
    this.lifetime = lifetime;
    this.path = path;
    pos = path.pos(0);
    this.sprite = sprite.clone();
    this.sprite.pos = pos;
    maxHealth = health;
    this.health = health;
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

  public Enemy clone(Enemy enemy) {
    return new Enemy(lifetime, path, sprite, maxHealth);
  }
}
