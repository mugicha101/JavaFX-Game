package application.bullet.staging;

import application.bullet.attr.BulletAttr;

public class DisableStage extends BulletStage {
  public DisableStage(String bulletAttrId, int time) {
    super(bulletAttrId, time);
  }

  public void action(BulletAttr ba) {
    ba.enabled = false;
  }
}
