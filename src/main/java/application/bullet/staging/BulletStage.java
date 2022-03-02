package application.bullet.staging;

import application.bullet.attr.BulletAttr;

// stages when to enable, disable, and modify bullet attributes during bullet's lifetime
public abstract class BulletStage {
  private final int time;
  private final String id; // id of targeted BulletAttr instance
  public BulletStage(String bulletAttrId, int time) {
    this.time = time;
    id = bulletAttrId;
  }

  public final int getTime() {
    return time;
  }

  public final String getId() {
    return id;
  }

  public abstract void action(BulletAttr ba);
}
