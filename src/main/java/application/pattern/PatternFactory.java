package application.pattern;

import application.DirCalc;
import application.bullet.BulletColor;
import application.bullet.attr.bullet.BulletAttr;
import application.bullet.attr.bullet.GrowAttr;
import application.bullet.attr.bullet.LinMoveAttr;
import application.bullet.attr.bullet.RotMoveAttr;
import application.bullet.attr.change.LinChangeAttr;
import application.bullet.attr.change.SmoothChangeAttr;
import application.bullet.staging.*;
import application.bullet.types.*;

import java.util.Random;

public class PatternFactory {
  protected static final Random rand = new Random();

  public static Pattern Test() {
    return new Pattern(
        "test",
        null,
        (time, pos, width, height) -> {
          if (time >= 30 && (time - 30) % 90 == 0) {
            // Position pos = new Position(width * (0.25 + Math.random() * 0.5), height * (0.2 +
            // Math.random() * 0.2));
            double rDir = Math.random() * 360;
            int rotMulti = rand.nextInt(2) * 2 - 1;
            double amount = 45;
            for (int i = 0; i < amount; i++) {
              for (int j = 2; j >= 0; j--) {
                int colorSwitchTime = rand.nextInt(60);
                double dir = rDir + (i * 360.0 / amount);
                new RiceBullet(
                    pos,
                    1,
                    BulletColor.INVERSE_RED,
                    new BulletAttr[] {
                      new RotMoveAttr(
                          "rot",
                          0,
                          2 + j * 0.2,
                          dir,
                          2 * rotMulti,
                          new LinChangeAttr("moveAcc", -0.03, 0),
                          new LinChangeAttr("rotAcc", -0.02 * rotMulti, 0)),
                      new LinMoveAttr("lin", 0, 0, new LinChangeAttr("acc", 0.005, 3))
                    },
                    new BulletStage[] {
                      new DisableAttrStage(0, "lin"),
                      new ModifyStage(
                          20 + colorSwitchTime, (Bullet b) -> b.setColor(BulletColor.DARK_RED)),
                      new ModifyStage(
                          80 + j * 5 - colorSwitchTime, (Bullet b) -> b.setColor(BulletColor.RED)),
                      new ModifyStage(
                          0,
                          (Bullet b) ->
                              ((LinMoveAttr) b.getAttr("lin")).dir =
                                  ((RotMoveAttr) b.getAttr("rot")).dir + 180),
                      new DisableAttrStage(0, "rot"),
                      new EnableAttrStage(0, "lin"),
                      new ModifyAttrStage(
                          60, "lin.acc", (ba) -> ((LinChangeAttr) ba).changeAmount = 0.1),
                      new BlankStage(30),
                    });
              }
            }
            new Bullet(
                pos,
                1,
                BulletColor.YELLOW,
                new BulletAttr[] {
                  new LinMoveAttr("move", 0, 0, new LinChangeAttr("acc", 0.1, 5)),
                  new GrowAttr("grow", new SmoothChangeAttr("change", 0.02, 3))
                },
                new BulletStage[] {
                  new DisableAttrStage(0, "move"),
                  new EnableAttrStage(170, "move"),
                  new ModifyStage(
                      0, (b) -> ((LinMoveAttr) b.getAttr("move")).dir = DirCalc.dirToPlayer(b.pos))
                });
          }
        });
  }

  public static Pattern TestStream() {
    return new Pattern(
        "stream",
        null,
        (time, pos, width, height) -> {
          if (time < 60 && time % 5 == 0) {
            for (int i = 0; i < 5; i++) {
              for (int j = -1; j <= 1; j++) {
                new RiceBullet(
                    pos,
                    1,
                    BulletColor.CYAN,
                    new BulletAttr[] {
                      new LinMoveAttr(
                          "move", 0, DirCalc.dirToPlayer(pos) + j * 15, new LinChangeAttr("acc", 0.5, 5 + i))
                    },
                    null);
              }
            }
          }
        });
  }
}
