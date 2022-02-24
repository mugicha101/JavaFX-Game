package application;

import javafx.scene.canvas.GraphicsContext;

public class Player {
  public Position pos;
  public final double speed;
  public final double focus_multi;
  public final double hitbox_radius;
  private final Sprite sprite;
  public double dir;
  public double alpha;
  public Player(double speed, double focus_multi, double hitbox_radius, Sprite sprite) {
    this.pos = new Position(0,0);
    this.speed = speed;
    this.focus_multi = focus_multi;
    this.hitbox_radius = hitbox_radius;
    this.sprite = sprite;
    this.alpha = 1;
    sprite.pos = pos;
  }

  public Sprite getSprite() {
    updateSprite();
    return sprite;
  }

  private void updateSprite() {
    sprite.dir = dir;
    sprite.alpha = alpha;
  }

  public void draw(GraphicsContext gc) {
    getSprite().draw(gc);
  }
}
