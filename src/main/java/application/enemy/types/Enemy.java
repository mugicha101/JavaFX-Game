package application.enemy.types;

import application.Position;
import application.particle.CircleParticle;
import application.sprite.Sprite;
import application.enemy.pathing.*;
import application.pattern.Pattern;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {
  public static final Random rand = new Random();
  public static ArrayList<Enemy> enemyList = new ArrayList<>();

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
  public final double hitRadius;
  public final double maxHealth;
  protected double health;
  private boolean active;
  private final Pattern[] patterns;
  public final Color color;

  public Enemy(int lifetime, Path path, Sprite sprite, Color color, double hitRadius, double health, Pattern... patterns) {
    time = 0;
    this.lifetime = lifetime;
    this.path = path;
    pos = path.pos(0);
    this.spriteSource = sprite;
    this.color = color;
    this.hitRadius = hitRadius;
    maxHealth = health;
    this.health = health;
    active = false;
    this.patterns = new Pattern[patterns.length];
    for (int i = 0; i < patterns.length; i++) {
      this.patterns[i] = patterns[i].clone();
      this.patterns[i].pos = pos;
    }
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
    return health > 0 && (lifetime <= 0 || time <= lifetime);
  }

  public final void move() {
    time++;
    pos.set(path.pos(time));
    for (Pattern p : patterns)
      p.moveTick();
  }

  public final void drawUpdate() {
    sprite.drawUpdate();
  }

  public final void damage(double amount) {
    if (health == 0)
      return;
    health -= amount;
    if (health <= 0) {
      health = 0;
      deathParticles();
    }
  }

  private void deathParticles() {
    for (int i = 0; i < Math.pow(maxHealth*5, 0.75); i++) {
      new CircleParticle(hitRadius*0.2 * (0.5 + rand.nextDouble()/2), color, 1, pos, rand.nextDouble() * 360, 5 + rand.nextDouble() * 10, 10 + rand.nextInt(20));
    }
  }

  public void delete() {
    sprite.disable();
  }

  public Enemy clone() {
    return new Enemy(lifetime, path, spriteSource, color, hitRadius, maxHealth, patterns);
  }
}
