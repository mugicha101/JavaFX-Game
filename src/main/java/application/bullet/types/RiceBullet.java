package application.bullet.types;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.attr.bullet.BulletAttr;
import application.bullet.staging.BulletStage;

public class RiceBullet extends Bullet {
  public RiceBullet(
          Position pos, double size, BulletColor color, BulletAttr[] attrArr, BulletStage[] stageArr) {
    super(pos, size * 0.35, color, attrArr, stageArr);
  }

  public RiceBullet(Position pos, double size, BulletColor color, BulletAttr[] attrArr) {
    this(pos, size, color, attrArr, null);
  }

  @Override
  public String getType() {
    return "rice";
  }

  @Override
  public void updateGroup() {
    super.updateGroup();
    groupBack.getChildren().get(0).setScaleX(2);
    groupFront.getChildren().get(0).setScaleX(2);
  }
}
