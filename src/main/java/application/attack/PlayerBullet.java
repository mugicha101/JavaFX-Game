package application.attack;

import application.DirCalc;
import application.Game;
import application.Position;
import application.bullet.types.Bullet;
import application.enemy.types.Enemy;
import application.particle.CircleParticle;
import application.sprite.Sprite;
import application.stats.Stats;
import javafx.scene.Group;

import java.util.HashSet;
import java.util.Random;

public class PlayerBullet extends PlayerAttack {
  protected static final Random rand = new Random();
  protected static final double particleMulti = 6;
  protected static final double stepLength = 10;
  private final Position pos;
  private final Stats parentStats;
  private double dir;
  private double speed;
  private double damageMulti;
  protected double radius;
  private final HashSet<Enemy> hitEnemies;
  private int pierce;
  public PlayerBullet(Group group, Stats parentStats, Position pos, double dir, double damageMulti, double speedMulti) {
    super(group);
    this.parentStats = parentStats.clone();
    this.pos = pos.clone();
    this.dir = dir + (rand.nextDouble() - 0.5) * parentStats.projInacc * 2;
    this.damageMulti = damageMulti;
    this.radius = parentStats.projSize * Math.sqrt(parentStats.damage * damageMulti);
    speed = parentStats.projSpeed * speedMulti;
    hitEnemies = new HashSet<>();
    pierce = parentStats.projPierce;
  }

  public void moveTick() {
    int steps = (int)Math.ceil(speed / stepLength);
    double stepAmount = speed / steps;
    for (int i = 0; i < steps; i++) {
      moveStep(stepAmount);
      if (!isActive())
        return;
    }
  }

  private void hit(Enemy hitEnemy, Position hitPos) { // on hit (handles pierce and kill)
    Position particlePos = pos.clone().moveInDir(DirCalc.dirTo(pos, hitPos), parentStats.projSize);
    int amount = (int)(parentStats.damage * particleMulti);
    if (rand.nextDouble() > (parentStats.damage * particleMulti - amount))
      amount++;
    for (int i = 0; i < amount; i++)
      new CircleParticle(radius, parentStats.projColor, parentStats.projOpacity, particlePos, dir + 180 + (rand.nextDouble() * 2 - 1) * 45, speed * (0.25 + rand.nextDouble()/2), 5 + rand.nextInt(10));
    if (hitEnemy != null) hitEnemy.damage(parentStats.damage * damageMulti);
    if (pierce > 0) {
      pierce--;
      if (hitEnemy != null) hitEnemies.add(hitEnemy);
    } else {
      kill();
    }
  }

  private void moveStep(double step) {
    pos.moveInDir(dir, step);
    for (Enemy e : Enemy.enemyList) {
      if (hitEnemies.contains(e))
        continue;
      if (e.pos.distSqd(pos) <= Math.pow(e.hitRadius + parentStats.projSize, 2)) {
        hit(e, e.pos);
        return;
      }
    }
    if (parentStats.collidable) {
      for (Bullet b : Bullet.getBullets()) {
        if (!b.isAlive())
          continue;
        if (b.pos.distSqd(pos) <= Math.pow(b.getRadius() + parentStats.projSize, 2)) {
          b.kill();
          hit(null, b.pos);
          return;
        }
      }
    }
    if (pos.x < -parentStats.projSize
        || pos.y < -parentStats.projSize
        || pos.x > Game.width + parentStats.projSize
        || pos.y > Game.height + parentStats.projSize) kill();
  }

  public void drawTick() {
    group.setTranslateX(pos.x);
    group.setTranslateY(pos.y);
    group.setRotate(-dir);
    group.setOpacity(Game.player.alpha * parentStats.projOpacity);
  }

  public void killHelper() {

  }
}
