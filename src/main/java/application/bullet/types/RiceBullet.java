package application.bullet.types;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.attr.bullet.BulletAttr;
import application.bullet.staging.BulletStage;

public class RiceBullet extends Bullet {
  public RiceBullet(
          BulletTemplate template) {
    super(template, 4);
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
