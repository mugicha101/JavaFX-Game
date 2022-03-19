package application.particle;

import application.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleParticle extends Particle {
  private final Circle circle;
  public CircleParticle(double radius, Color color, Position pos, double dir, double speed, int time) {
    super(pos, dir, speed, time);
    circle = new Circle(0, 0, radius);
    circle.setFill(color);
    Particle.particleGroup.getChildren().add(circle);
  }

  public void drawUpdate() {
    circle.setTranslateX(pos.x);
    circle.setTranslateY(pos.y);
    circle.setScaleX(1-getProgress());
    circle.setScaleY(1-getProgress());
  }

  public void delete() {
    Particle.particleGroup.getChildren().remove(circle);
  }
}
