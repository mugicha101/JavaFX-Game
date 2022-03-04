package application.bullet.attr;

import application.Game;
import application.Position;
import application.bullet.types.Bullet;

import java.util.HashMap;

public class RotMoveAttr extends MoveAttr {
  private final double initDist;
  private final double initMoveSpeed;
  private final double initDir;
  private final double initRotSpeed;
  private final Position center;
  public double dist;
  public double moveSpeed;
  public double rotSpeed;
  public double dir;
  private final ChangeAttr moveAccelAttr;
  private final ChangeAttr rotAccelAttr;

  public RotMoveAttr(
      String id,
      double startingDist,
      double moveSpeed,
      double dir,
      double rotSpeed,
      ChangeAttr moveAccelAttr,
      ChangeAttr rotAccelAttr) {
    super(id);
    initDist = startingDist;
    initMoveSpeed = moveSpeed;
    initRotSpeed = rotSpeed;
    initDir = dir;
    this.dist = initDist;
    this.moveSpeed = initMoveSpeed;
    this.rotSpeed = initRotSpeed;
    this.dir = initDir;
    this.moveAccelAttr = moveAccelAttr == null ? null : moveAccelAttr.clone();
    this.rotAccelAttr = rotAccelAttr == null ? null : rotAccelAttr.clone();
    this.center = new Position(0, 0);
  }

  public RotMoveAttr(
      String id,
      double startingDist,
      double moveSpeed,
      double dir,
      double rotSpeed,
      ChangeAttr moveAccelAttr) {
    this(id, startingDist, moveSpeed, dir, rotSpeed, moveAccelAttr, null);
  }

  public RotMoveAttr(
      String id, double startingDist, double moveSpeed, double dir, double rotSpeed) {
    this(id, startingDist, moveSpeed, dir, rotSpeed, null, null);
  }

  public void init(Bullet b) {
    moveSpeed = initMoveSpeed;
    rotSpeed = initRotSpeed;
    dir = initDir;
    b.dir = dir;
    center.set(b.pos);
    dist = initDist;
  }

  public void prepTick(Bullet b) {
    if (moveAccelAttr != null && moveAccelAttr.enabled) moveSpeed = moveAccelAttr.tick(moveSpeed);
    if (rotAccelAttr != null && rotAccelAttr.enabled) rotSpeed = rotAccelAttr.tick(rotSpeed);
  }

  public void moveTick(Bullet b) {
    dist += moveSpeed;
    dir = (dir + rotSpeed) % 360;
    b.pos.set(center.clone().moveInDir(dir, dist));
    b.dir = dir;
  }

  @Override
  public boolean overridesDefaultBorderCollision() {
    return true;
  }

  public boolean collisionTick(Bullet b) {
    return b.finishedStages() && dist * dist > Game.width * Game.width + Game.height * Game.height;
  }

  public MoveAttr clone(String newId) {
    return new RotMoveAttr(
        newId, initDist, initMoveSpeed, initDir, initRotSpeed, moveAccelAttr, rotAccelAttr);
  }

  @Override
  public void toMap(HashMap<String, BulletAttr> map, String prefix) {
    if (moveAccelAttr != null) moveAccelAttr.toMap(map, prefix + getId() + ".");
    if (rotAccelAttr != null) rotAccelAttr.toMap(map, prefix + getId() + ".");
    map.put(prefix + getId(), this);
  }
}
