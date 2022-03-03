package application.bullet.types;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.attr.BulletAttr;
import application.bullet.attr.MoveAttr;
import application.bullet.staging.BulletStage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

public class RiceBullet extends Bullet {
  public RiceBullet(
      Position pos, double size, BulletColor color, MoveAttr[] attrArr, BulletStage[] stageArr) {
    super(pos, size * 0.35, color, attrArr, stageArr);
  }

  public RiceBullet(Position pos, double size, BulletColor color, MoveAttr[] attrArr) {
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
