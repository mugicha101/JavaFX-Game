package application.bullet.staging;

import application.bullet.attr.BulletAttr;

public abstract class AttrStage extends BulletStage {
  private final String id; // id of targeted BulletAttr instance

  public AttrStage(int time, String bulletAttrId) {
    super(time);
    id = bulletAttrId;
  }

  public final String getId() {
    return id;
  }

  public abstract void action(BulletAttr ba);
}
