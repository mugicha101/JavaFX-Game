package application.bullet.staging;

import application.bullet.attr.BulletAttr;

public class ModifyAttrStage extends AttrStage {
  private final ModifyAttr mod;

  public ModifyAttrStage(int time, String bulletAttrId, ModifyAttr mod) {
    super(time, bulletAttrId);
    this.mod = mod;
  }

  public void action(BulletAttr ba) {
    mod.action(ba);
  }
}
