package application.attack;

import application.Game;
import application.Position;
import application.sprite.Sprite;
import application.stats.Stats;
import javafx.scene.Group;

import java.util.Random;

public class PlayerBullet extends PlayerAttack {
  protected static final Random rand = new Random();
  private final Position pos;
  private final Stats parentStats;
  private double dir;
  private double speed;
  public PlayerBullet(Group group, Stats parentStats, Position pos, double dir) {
    super(group);
    this.parentStats = parentStats.clone();
    this.pos = pos.clone();
    this.dir = dir + (rand.nextDouble() - 0.5) * parentStats.projInacc * 2;
    speed = parentStats.projSpeed;
  }

  public void moveTick() {
    pos.moveInDir(dir, speed);
    if (pos.x < -parentStats.projSize || pos.y < -parentStats.projSize || pos.x > Game.width + parentStats.projSize || pos.y > Game.height + parentStats.projSize)
      kill();
  }

  public void drawTick() {
    group.setTranslateX(pos.x);
    group.setTranslateY(pos.y);
    group.setRotate(-dir);
    group.setOpacity(Game.player.alpha);
  }

  public void killHelper() {

  }
}
