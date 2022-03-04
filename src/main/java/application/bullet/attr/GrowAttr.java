package application.bullet.attr;

import application.bullet.types.Bullet;

import java.util.HashMap;

public class GrowAttr extends MoveAttr {
  private final ChangeAttr changeAttr;

  public GrowAttr(String id, ChangeAttr changeAttr) {
    super(id);
    if (changeAttr == null)
      throw new NullPointerException();
    this.changeAttr = changeAttr.clone();
  }

  public void init(Bullet b) {}

  public void prepTick(Bullet b) {
    b.scale = changeAttr.tick(b.scale);
  }

  public void moveTick(Bullet b) {}

  public boolean collisionTick(Bullet b) {
    return false;
  }

  public MoveAttr clone(String newId) {
    return new GrowAttr(newId, changeAttr);
  }

  @Override
  public void toMap(HashMap<String, BulletAttr> map, String prefix) {
    map.put(prefix + getId(), this);
    changeAttr.toMap(map, prefix + getId() + ".");
  }
}
