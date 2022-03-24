package application.bullet.attr.bullet;

import application.bullet.attr.Attr;
import application.bullet.attr.change.ChangeAttr;
import application.bullet.types.Bullet;

import java.util.HashMap;

public class GrowAttr extends BulletAttr {
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

  public GrowAttr clone(String newId) {
    return new GrowAttr(newId, changeAttr);
  }

  @Override
  public void toMap(HashMap<String, Attr> map, String prefix) {
    map.put(prefix + getId(), this);
    changeAttr.toMap(map, prefix + getId() + ".");
  }
}
