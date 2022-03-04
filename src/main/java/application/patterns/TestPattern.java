package application.patterns;

import application.DirCalc;
import application.Position;
import application.bullet.BulletColor;
import application.bullet.attr.*;
import application.bullet.staging.*;
import application.bullet.types.*;

public class TestPattern extends Pattern {
  public TestPattern() {
    super("Test", 1000);
  }

  public void tick(int cycle, double width, double height) {
    if (cycle % 45 == 0) {
      Position pos =
          new Position(width * (0.25 + Math.random() * 0.5), height * (0.2 + Math.random() * 0.2));
      double rDir = Math.random() * 360;
      int rotMulti = rand.nextInt(2) * 2 - 1;
      double amount = 45;
      for (int i = 0; i < amount; i++) {
        for (int j = 2; j >= 0; j--) {
          int colorSwitchTime = rand.nextInt(60);
          double dir = rDir + (i * 360.0 / amount);
          Bullet.spawnBullet(
              new RiceBullet(
                  pos,
                  1,
                  BulletColor.INVERSE_RED,
                  new MoveAttr[] {
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
                  }));
        }
      }
      Bullet.spawnBullet(
              new Bullet(
                      pos,
                      1,
                      BulletColor.YELLOW,
                      new MoveAttr[] {
                              new LinMoveAttr("move", 0, 0, new LinChangeAttr("acc", 0.1, 5)),
                              new GrowAttr("grow", new SmoothChangeAttr("change", 0.02, 3))
                      },
                      new BulletStage[] {
                              new DisableAttrStage(0, "move"),
                              new EnableAttrStage(170, "move"),
                              new ModifyStage(0, (b) -> ((LinMoveAttr)b.getAttr("move")).dir = DirCalc.dirToPlayer(b.pos))
                      }));
    }
  }
}
