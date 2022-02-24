package application.bullet;

import application.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet {
  private boolean alive;
  protected int time;
  public double radius;
  public Color color;
  public Bullet(Position pos, double size, Color color) {
    alive = true;
    time = 0;
    this.radius = size*10;
    this.color = color;
  }

  public final boolean isAlive() {
    return alive;
  }

  public final int getTime() {
    return time;
  }

  public final void kill() {
    if (!alive)
      return;
    alive = false;
    time = 0;
  }

  public final void move() {
    time++;
    if (!alive)
      return;

    // prep

    // move

    // collision
  }

  public void renderBullet(GraphicsContext gc) {

  }
}
