package application;

import javafx.scene.ParallelCamera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Transform;

public class Player {
  public Position pos;
  public final double speed;
  public final double focus_multi;
  public final double hitbox_radius;
  private final Sprite sprite;
  public double dir;
  public Player(double speed, double focus_multi, double hitbox_radius, Sprite sprite) {
    this.pos = new Position(0,0);
    this.speed = speed;
    this.focus_multi = focus_multi;
    this.hitbox_radius = hitbox_radius;
    this.sprite = sprite;
    sprite.pos = pos;
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
