package application.bullet.bulletTypes;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.bulletAttr.BulletAttr;
import application.bullet.bulletAttr.MoveAttr;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Ellipse;

public class RiceBullet extends Bullet {
  public RiceBullet(Position pos, double size, BulletColor color, MoveAttr[] attrArr) {
    super(pos, size*0.35, color, attrArr);
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
    for (int i = 0; i < backGradientLayers; i++) {
      double radius = groupSize * (3 - 1.75 * i / backGradientLayers);
      Ellipse ellipse = new Ellipse(0, 0, radius * 2, radius);
      ellipse.setFill(Color.color(getOuterColor().getRed(), getOuterColor().getGreen(), getOuterColor().getBlue(), backOpacity));
      groupBack.getChildren().add(ellipse);
    }
    // front
    RadialGradient grad = new RadialGradient(0, 0, 0, 0, groupSize, false, CycleMethod.NO_CYCLE);
    double[] c1 = new double[] {getOuterColor().getRed(), getOuterColor().getGreen(), getOuterColor().getBlue()};
    double[] c2 = new double[] {getInnerColor().getRed(), getInnerColor().getGreen(), getInnerColor().getBlue()};
    double[] c3 = new double[3];
    for (int i = 0; i <= frontGradientLayers; i++) {
      double radius = groupSize * (1.25 - 0.5 * i / frontGradientLayers);
      Ellipse ellipse = new Ellipse(0, 0, radius * 2, radius);
      for (int j = 0; j < 3; j++)
        c3[j] = c1[j] + (c2[j] - c1[j]) * i / frontGradientLayers;
      ellipse.setFill(Color.color(c3[0], c3[1], c3[2]));
      groupFront.getChildren().add(ellipse);
    }
  }
}
