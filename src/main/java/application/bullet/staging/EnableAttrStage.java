package application.bullet.staging;

import application.bullet.attr.Attr;

public class EnableAttrStage extends AttrStage {
  public EnableAttrStage(int time, String bulletAttrId) {
    super(time, bulletAttrId);
  }

  public void action(Attr attr) {
    attr.enabled = true;
  }
}
