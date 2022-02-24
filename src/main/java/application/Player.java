package application;

import javafx.scene.image.Image;

public class Player {
  public int[] pos;
  public final int speed;
  public final int hitbox_radius;
  public final Image img;
  public Player(int speed, int hitbox_radius, Image img) {
    this.speed = speed;
    this.hitbox_radius = hitbox_radius;
    this.img = img;
  }
}
