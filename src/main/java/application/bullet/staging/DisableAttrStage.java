package application.bullet.staging;

import application.bullet.attr.Attr;

public class DisableAttrStage extends AttrStage {
  public DisableAttrStage(int time, String bulletAttrId) {
    super(time, bulletAttrId);
  }

  public void action(Attr attr) {
    attr.enabled = false;
  }

  public DisableAttrStage clone() {
    return new DisableAttrStage(getTime(), getId());
  }
}
