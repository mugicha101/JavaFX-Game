package application;

import javafx.scene.ParallelCamera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Transform;

public class Player {
  public Position pos;
  public final int speed;
  public final int hitbox_radius;
  private final Sprite sprite;
  public double dir;
  public Player(int speed, int hitbox_radius, Sprite sprite) {
    this.pos = new Position(0,0);
    this.speed = speed;
    this.hitbox_radius = hitbox_radius;
    this.sprite = sprite;
  }

  public Sprite getSprite() {
    this.updateSprite();
    return this.sprite;
  }

  private void updateSprite() {
    this.sprite.dir = this.dir;
  }

  public void draw(GraphicsContext gc) {
    this.getSprite().draw(gc);
  }
}
