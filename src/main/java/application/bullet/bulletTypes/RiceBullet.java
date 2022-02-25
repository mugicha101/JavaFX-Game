package application.bullet.bulletTypes;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.bulletAttr.BulletAttr;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RiceBullet extends Bullet {
  public RiceBullet(Position pos, double size, BulletColor color, BulletAttr[] attrArr) {
    super(pos, size*0.5, color, attrArr);
  }

  @Override
  public void drawBack(GraphicsContext gc) {

  }

  @Override
  public void drawFront(GraphicsContext gc) {

  }
}
