package application.stats;

import application.Game;
import application.Position;
import application.attack.DefaultBullet;
import application.attack.NeedleBullet;
import application.attack.PlayerBullet;
import application.sprite.Sprite;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.Arrays;

public class Player {
  private static final double HB_RENDER_SIZE = 10;
  public Position pos;
  private final StatManager sm;
  public final Sprite sprite;
  public double dir;
  public double alpha;
  private Circle hb;
  private double fireDelay;

  public Player(Sprite sprite, Stats baseStats) {
    this.pos = new Position(0, 0);
    this.sm = new StatManager(baseStats);
    this.sprite = sprite;
    this.alpha = 1;
    sprite.pos = pos;
    sprite.enable();
    fireDelay = 1;
    initPlayerHB();
  }

  public Stats getStats() {
    return sm.stats;
  }

  public void addItem(Item item) {
    sm.addItem(item);
  }

  public void attackTick() {
    class ProjOrigin {
      private Position pos;
      private double dir;
      private double speedMulti;
      public ProjOrigin(Position pos, double dirOffset, double speedMulti) {
        this.pos = pos;
        this.speedMulti = speedMulti;
        dir = dirOffset + 90;
        spawnProj();
      }
      public ProjOrigin(Position pos, double dirOffset) {
        this(pos, dirOffset, 1);
      }

      private void spawnProj() {
        switch(getStats().laserType) {
          case NORMAL -> {
            // UNIMPLEMENTED
          }
          case LARGE -> {
            // UNIMPLEMENTED
          }
          default -> {
            switch(getStats().projType) {
              case NEEDLE -> {
                new NeedleBullet(getStats(), pos, dir, 1, speedMulti);
              }
              default -> {
                new DefaultBullet(getStats(), pos, dir, 1, speedMulti);
              }
            }
          }
        }
      }
    }

    fireDelay--;
    while (fireDelay <= 0) {
      fireDelay += 60.0/getStats().firerate;
      if (sm.hasItem(Item.Delineator)) {
        for (int i = 0; i < getStats().projAmount; i++)
          new ProjOrigin(pos.clone().move(0, -30), 0, 1 + i*0.2);
      } else {
        if (getStats().projAmount == 1) {
          new ProjOrigin(pos.clone().move(0, -30), 0);
        } else if (getStats().projAmount == 2) {
          new ProjOrigin(pos.clone().move(-10, -30), 0);
          new ProjOrigin(pos.clone().move(10, -30), 0);
        } else if (getStats().projAmount == 3) {
          new ProjOrigin(pos.clone().move(-10, -30), 2 * getStats().projInacc);
          new ProjOrigin(pos.clone().move(10, -30), -2 * getStats().projInacc);
          new ProjOrigin(pos.clone().move(0, -30), 0);
        } else if (getStats().projAmount == 4) {
          new ProjOrigin(pos.clone().move(-10, -30), 2 * getStats().projInacc);
          new ProjOrigin(pos.clone().move(10, -30), -2 * getStats().projInacc);
          new ProjOrigin(pos.clone().move(-5, -30), 0);
          new ProjOrigin(pos.clone().move(5, -30), 0);
        } else {
          int a = getStats().projAmount;
          for (int i = 0; i < a; i++)
            new ProjOrigin(pos.clone().move(-10 + 20.0 * i / (a - 1), -30), (getStats().projInacc + 2) * (3 - 6.0 * i / (a - 1)));
        }
      }
    }
  }

  public void drawUpdate() {
    sprite.dir = dir;
    sprite.alpha = alpha;
    sprite.drawUpdate();
    double scale =
        (-2.913 * Math.pow(((double) Game.focusHold / 10) - 0.5859, 2) * 2 + 2)
            * getStats().hitboxRadius / HB_RENDER_SIZE;
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
    double radius = HB_RENDER_SIZE * 6;
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
