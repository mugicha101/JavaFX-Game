package application.bullet.attr.bullet;

import application.DirCalc;
import application.Game;
import application.Position;
import application.bullet.attr.Attr;
import application.bullet.attr.change.ChangeAttr;
import application.bullet.types.Bullet;

import java.util.HashMap;

public class RotMoveAttr extends BulletAttr {
  public enum DirMode {
    NONE, // does not edit direction
    ORIGIN, // faces away from origin
    MOVE, // faces in direction of motion
  }
  public double initDist;
  public double initMoveSpeed;
  public double initDir;
  public double initRotSpeed;
  public Position center;
  public double dist;
  public double moveSpeed;
  public double rotSpeed;
  public double dir;
  private final DirMode dirMode;
  private final ChangeAttr moveAccelAttr;
  private final ChangeAttr rotAccelAttr;

  public RotMoveAttr(
      String id,
      double startingDist,
      double moveSpeed,
      double dir,
      double rotSpeed,
      DirMode dirMode,
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
    this.dirMode = dirMode;
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
      DirMode dirMode,
      ChangeAttr moveAccelAttr) {
    this(id, startingDist, moveSpeed, dir, rotSpeed, dirMode, moveAccelAttr, null);
  }

  public RotMoveAttr(
      String id, double startingDist, double moveSpeed, double dir, double rotSpeed, DirMode dirMode) {
    this(id, startingDist, moveSpeed, dir, rotSpeed, dirMode, null, null);
  }

  // TO ADD: ability to move center

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
    Position oldPos = b.pos.clone();
    b.pos.set(center.clone().moveInDir(dir, dist));
    b.dir = switch(dirMode) {
      case ORIGIN -> dir;
      case MOVE -> DirCalc.dirTo(oldPos, b.pos);
      default -> b.dir;
    };
  }

  @Override
  public boolean overridesDefaultBorderCollision() {
    return true;
  }

  public boolean collisionTick(Bullet b) {
    return b.finishedStages() && dist * dist > Game.width * Game.width + Game.height * Game.height;
  }

  public RotMoveAttr clone(String newId) {
    return new RotMoveAttr(
        newId, initDist, initMoveSpeed, initDir, initRotSpeed, dirMode, moveAccelAttr, rotAccelAttr);
  }

  @Override
  public void toMap(HashMap<String, Attr> map, String prefix) {
    if (moveAccelAttr != null) moveAccelAttr.toMap(map, prefix + getId() + ".");
    if (rotAccelAttr != null) rotAccelAttr.toMap(map, prefix + getId() + ".");
    map.put(prefix + getId(), this);
  }
}
