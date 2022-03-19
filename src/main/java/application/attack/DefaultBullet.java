package application.attack;

import application.Position;
import application.stats.Stats;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class DefaultBullet extends PlayerBullet {
  public DefaultBullet(Stats parentStats, Position pos, double dir) {
    super(new Group(), parentStats, pos, dir);
    ObservableList<Node> gList = group.getChildren();
    Circle circle = new Circle(0, 0, parentStats.projSize*1.5);
    circle.setScaleY(2.0/3);
    Color color = parentStats.projColor;
    circle.setFill(new RadialGradient(0, 0, 0, 0, parentStats.projSize*1.5, false, CycleMethod.NO_CYCLE, new Stop(0.5, Color.WHITE), new Stop(0.8, color)));
    gList.add(circle);
  }
}
