package application.bullet.staging;

import application.bullet.attr.BulletAttr;

public class EnableStage extends BulletStage {
  public EnableStage(String bulletAttrId, int time) {
    super(bulletAttrId, time);
  }

  public void action(BulletAttr ba) {
    ba.enabled = true;
  }
}