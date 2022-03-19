package application.particle;

import application.Position;
import application.sprite.Sprite;

public class SpriteParticle extends Particle {
  private final Sprite sprite;
  public SpriteParticle(Sprite sprite, Position pos, double dir, double speed, int time) {
    super(pos, dir, speed, time);
    this.sprite = sprite.clone();
    this.sprite.pos = this.pos;
    this.sprite.enable();
  }

  public void drawUpdate() {
    sprite.dir = dir;
    sprite.scale = 1 - getProgress();
    sprite.drawUpdate();
  }

  public void delete() {
    this.sprite.disable();
  }
}
