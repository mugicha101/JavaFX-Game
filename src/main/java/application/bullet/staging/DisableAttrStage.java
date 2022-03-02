package application.bullet.staging;

import application.bullet.attr.BulletAttr;

public class DisableAttrStage extends AttrStage {
  public DisableAttrStage(int time, String bulletAttrId) {
    super(time, bulletAttrId);
  }

  public void action(BulletAttr ba) {
    ba.enabled = false;
  }
}
