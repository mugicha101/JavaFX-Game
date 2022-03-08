package application;

import application.sprite.Sprite;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.Arrays;

public class Player {
  public Position pos;
  public final double speed;
  public final double focusMulti;
  public final double hbRadius;
  public final Sprite sprite;
  public double dir;
  public double alpha;
  private Circle hb;

  public Player(double speed, double focus_multi, double hitbox_radius, Sprite sprite) {
    this.pos = new Position(0, 0);
    this.speed = speed;
    this.focusMulti = focus_multi;
    this.hbRadius = hitbox_radius;
    this.sprite = sprite;
    this.alpha = 1;
    sprite.pos = pos;
    sprite.enable();
    initPlayerHB();
  }

  public void drawUpdate() {
    sprite.dir = dir;
    sprite.alpha = alpha;
    sprite.drawUpdate();
    double scale = -2.913 * Math.pow(((double) Game.focusHold / 10) - 0.5859, 2) * 2 + 2;
    if (hb.getScaleX() != scale) {
      hb.setScaleX(scale);
      hb.setScaleY(scale);
    }
    double x = pos.x - hb.getRadius() * scale / 2;
    double y = pos.y - hb.getRadius() * scale / 2;
    if (hb.getTranslateX() != x) {
      hb.setTranslateX(x);
    }
    if (hb.getTranslateX() != y) {
      hb.setTranslateY(y);
    }
  }

  private void initPlayerHB() {
    double radius = hbRadius * 6;
    hb = new Circle(radius);
    RadialGradient grad =
        new RadialGradient(
            0,
            0,
            radius / 2,
            radius / 2,
            radius,
            false,
            CycleMethod.NO_CYCLE,
            Arrays.asList(
                new Stop(0, Color.color(1, 1, 1, 1.0)),
                new Stop(0.1, Color.color(0.5, 1, 1, 1.0)),
                new Stop(0.2, Color.color(0, 0.5, 1, 0.5)),
                new Stop(0.4, Color.color(0, 0, 1, 0))));
    hb.setFill(grad);
    Game.playerHBGroup.getChildren().add(hb);
  }

  public void delete() {
    sprite.disable();
    Game.playerHBGroup.getChildren().remove(hb);
  }
}
