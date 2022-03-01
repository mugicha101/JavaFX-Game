package application.bullet.bulletAttr;

/*
BulletAttr inheritance diagram:
                           BulletAttr
                               |
                 ---------------
                 |
              MoveAttr <--------------- AccelAttr
                 |                          |
           ---------------           ----------------
           |             |           |              |
        LinMoveAttr RotMoveAttr LinAccelAttr SmoothAccelAttr
 */


public abstract class BulletAttr {
  private String id;
  public String getId() {
    return id;
  }

  public BulletAttr(String id) {
    this.id = id;
  }
  public abstract BulletAttr clone(); // deep clones object
}
