package application.bullet.bulletAttr;

import application.Game;
import application.Position;
import application.bullet.bulletTypes.Bullet;

public class RotMoveAttr extends MoveAttr {
  private final double initDist;
  private final double initMoveSpeed;
  private final double initDir;
  private final double initRotSpeed;
  private final Position center;
  private double dist;
  private double moveSpeed;
  private double rotSpeed;
  private double dir;
  private final AccelAttr moveAccelAttr;
  private final AccelAttr rotAccelAttr;
  public RotMoveAttr(String id, double startingDist, double moveSpeed, double dir, double rotSpeed, AccelAttr moveAccelAttr, AccelAttr rotAccelAttr) {
    super(id);
    initDist = startingDist;
    initMoveSpeed = moveSpeed;
    initRotSpeed = rotSpeed;
    initDir = dir;
    this.moveAccelAttr = moveAccelAttr == null? null : moveAccelAttr.clone();
    this.rotAccelAttr = rotAccelAttr == null? null : rotAccelAttr.clone();
    this.center = new Position(0,0);
  }

  public RotMoveAttr(String id, double startingDist, double moveSpeed, double dir, double rotSpeed, AccelAttr moveAccelAttr) {
    this(id, startingDist, moveSpeed, dir, rotSpeed, moveAccelAttr, null);
  }

  public RotMoveAttr(String id, double startingDist, double moveSpeed, double dir, double rotSpeed) {
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
    if (moveAccelAttr != null)
      moveSpeed = moveAccelAttr.tick(moveSpeed);
    if (rotAccelAttr != null)
      rotSpeed = rotAccelAttr.tick(rotSpeed);
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
    return dist * dist > Game.width * Game.width + Game.height * Game.height;
  }

  public MoveAttr clone() {
    return new RotMoveAttr(getId(), initDist, initMoveSpeed, initDir, initRotSpeed, moveAccelAttr, rotAccelAttr);
  }
}
