package application.attack;

import application.DirCalc;
import application.Game;
import application.Position;
import application.enemy.types.Enemy;
import application.particle.CircleParticle;
import application.sprite.Sprite;
import application.stats.Stats;
import javafx.scene.Group;

import java.util.Random;

public class PlayerBullet extends PlayerAttack {
  protected static final Random rand = new Random();
  protected static final double particleMulti = 2;
  private final Position pos;
  private final Stats parentStats;
  private double dir;
  private double speed;
  private double damageMulti;
  protected double radius;
  public PlayerBullet(Group group, Stats parentStats, Position pos, double dir, double damageMulti, double speedMulti) {
    super(group);
    this.parentStats = parentStats.clone();
    this.pos = pos.clone();
    this.dir = dir + (rand.nextDouble() - 0.5) * parentStats.projInacc * 2;
    this.damageMulti = damageMulti;
    this.radius = parentStats.projSize * Math.sqrt(parentStats.damage * damageMulti);
    speed = parentStats.projSpeed * speedMulti;
  }

  public void moveTick() {
    pos.moveInDir(dir, speed);
    for (Enemy e : Enemy.enemyList) {
      if (e.pos.distSqd(pos) <= Math.pow(e.hitRadius + parentStats.projSize, 2)) {
        Position particlePos = pos.clone().moveInDir(DirCalc.dirTo(pos, e.pos), parentStats.projSize);
        int amount = (int)(parentStats.damage * particleMulti);
        if (rand.nextDouble() > (parentStats.damage * particleMulti - amount))
          amount++;
        for (int i = 0; i < 3 * amount; i++)
          new CircleParticle(radius, parentStats.projColor, parentStats.projOpacity, particlePos, dir + 180 + (rand.nextDouble() * 2 - 1) * 45, speed * (0.25 + rand.nextDouble()/2), 5 + rand.nextInt(10));
        e.damage(parentStats.damage * damageMulti);
        kill();
        return;
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
