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
    groupBack.getChildren().clear();
    groupFront.getChildren().clear();
    // back
    if (groupBack.getChildren().size() == 0) {
      for (int i = 0; i < backGradientLayers; i++) {
        double radius = groupSize * (3 - 1.75 * i / backGradientLayers);
        groupBack.getChildren().add(new Ellipse(0, 0, radius * 2, radius));
      }
    }
    for (int i = 0; i < backGradientLayers; i++) {
      ((Ellipse) groupBack.getChildren().get(i))
          .setFill(
              Color.color(
                  getOuterColor().getRed(),
                  getOuterColor().getGreen(),
                  getOuterColor().getBlue(),
                  backOpacity));
    }
    // front
    if (groupFront.getChildren().size() == 0) {
      for (int i = 0; i <= frontGradientLayers; i++) {
        double radius = groupSize * (1.25 - 0.5 * i / frontGradientLayers);
        groupFront.getChildren().add(new Ellipse(0, 0, radius * 2, radius));
      }
    }
    double[] c1 =
        new double[] {
          getOuterColor().getRed(), getOuterColor().getGreen(), getOuterColor().getBlue()
        };
    double[] c2 =
        new double[] {
          getInnerColor().getRed(), getInnerColor().getGreen(), getInnerColor().getBlue()
        };
    double[] c3 = new double[3];
    for (int i = 0; i <= frontGradientLayers; i++) {
      for (int j = 0; j < 3; j++) c3[j] = c1[j] + (c2[j] - c1[j]) * i / frontGradientLayers;
      ((Ellipse) groupFront.getChildren().get(i)).setFill(Color.color(c3[0], c3[1], c3[2]));
    }
  }
}
