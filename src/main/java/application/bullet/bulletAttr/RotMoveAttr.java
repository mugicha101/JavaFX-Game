package application.bullet.bulletAttr;

import application.Position;
import application.bullet.bulletTypes.Bullet;

public class RotMoveAttr extends MoveAttr {
  private final double initMoveSpeed;
  private final double initRotSpeed;
  private final double initDir;
  private final Position center;
  private double dist;
  private double moveSpeed;
  private double rotSpeed;
  private double dir;
  private final AccelAttr moveAccelAttr;
  private final AccelAttr rotAccelAttr;
  public RotMoveAttr(String id, double moveSpeed, double dir, double rotSpeed, AccelAttr moveAccelAttr, AccelAttr rotAccelAttr) {
    super(id);
    initMoveSpeed = moveSpeed;
    initRotSpeed = rotSpeed;
    initDir = dir;
    this.moveAccelAttr = moveAccelAttr;
    this.rotAccelAttr = rotAccelAttr;
    this.center = new Position(0,0);
  }

  public RotMoveAttr(String id, double moveSpeed, double dir, double rotSpeed, AccelAttr moveAccelAttr) {
    this(id, moveSpeed, rotSpeed, dir, moveAccelAttr, null);
  }

  public RotMoveAttr(String id, double moveSpeed, double dir, double rotSpeed) {
    this(id, moveSpeed, rotSpeed, dir, null, null);
  }

  public void init(Bullet b) {
    moveSpeed = initMoveSpeed;
    rotSpeed = initRotSpeed;
    dir = initDir;
    b.dir = dir;
    center.set(b.pos);
  }

  public void prepTick(Bullet b) {
    if (moveAccelAttr != null)
      moveSpeed = moveAccelAttr.tick(moveSpeed);
    if (rotAccelAttr != null)
      rotSpeed = rotAccelAttr.tick(rotSpeed);
  }

  public void moveTick(Bullet b) {
    dist += moveSpeed;
    dir += rotSpeed;
    b.pos.set(center.clone().move(dir, dist));
    b.dir = dir;
  }

  public boolean collisionTick(Bullet b) {
    return false;
  }

  public MoveAttr clone() {
    return new RotMoveAttr(getId(), initMoveSpeed, initDir, initRotSpeed, moveAccelAttr, rotAccelAttr);
  }
}
