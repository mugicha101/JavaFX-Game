package application.bullet;

import application.Position;
import javafx.scene.canvas.GraphicsContext;

public class RiceBullet extends Bullet {
  public RiceBullet(Position pos, double size) {
    super(pos, size*0.5);
  }

  @Override
  public void renderBullet(GraphicsContext gc) {

  }
}
