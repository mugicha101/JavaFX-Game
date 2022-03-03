package application.bullet.attr;

/*
BulletAttr inheritance diagram:
                           BulletAttr
                               |
                 ----------------------------
                 |                          |
              MoveAttr <--------------- AccelAttr
                 |                          |
           ---------------           ----------------
           |             |           |              |
        LinMoveAttr RotMoveAttr LinAccelAttr SmoothAccelAttr
 */

import java.util.HashMap;

public abstract class BulletAttr {
  private String id;
  public boolean enabled;

  public String getId() {
    return id;
  }

  public BulletAttr(String id) {
    this.id = id;
    enabled = true;
  }

  public abstract BulletAttr clone(String newId); // deep clones object

  public BulletAttr clone() {
    return clone(id);
  }

  public abstract void toMap(
      HashMap<String, BulletAttr> map,
      String prefix); // add self and all child BulletAttr instances to map
}
