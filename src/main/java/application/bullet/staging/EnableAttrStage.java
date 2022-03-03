package application.bullet.staging;

import application.bullet.attr.BulletAttr;

public class EnableAttrStage extends AttrStage {
  public EnableAttrStage(int time, String bulletAttrId) {
    super(time, bulletAttrId);
  }

  public void action(BulletAttr ba) {
    ba.enabled = true;
  }
}
