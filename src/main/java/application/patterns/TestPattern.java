package application.patterns;

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
    if (cycle % 30 == 0) {
      Position pos = new Position(width * (0.25 + Math.random() * 0.5), height * (0.2 + Math.random() * 0.2));
      double dir = Math.random() * 360;
      int rotMulti = rand.nextInt(2) * 2 - 1;
      double amount = 36;
      for (int i = 0; i < amount; i++) {
        for (int j = 2; j >= 0; j--) {
          int colorSwitchTime = rand.nextInt(60);
          Bullet.spawnBullet(
              new RiceBullet(
                  pos.clone().moveInDir(dir, 100),
                  1,
                  BulletColor.YELLOW,
                  new MoveAttr[] {
                    new RotMoveAttr("rot", 0, 2 + j * 0.2, dir + (i * 360.0 / amount), 2 * rotMulti, new LinAccelAttr("moveAcc", -0.05, 0), new LinAccelAttr("rotAcc", -0.025 * rotMulti, 0)),
                    new LinMoveAttr("lin", 0, 0, new LinAccelAttr("acc", 0.005, 2))
                  },
                  new BulletStage[] {
                    new DisableAttrStage(0, "lin"),
                    new ModifyStage(20 + colorSwitchTime, (Bullet b) -> b.setColor(BulletColor.ORANGE)),
                    new ModifyStage(20, (Bullet b) -> b.setColor(BulletColor.RED)),
                    new BlankStage(60 + j * 5 - colorSwitchTime),
                    new ModifyStage(0, (Bullet b) -> ((LinMoveAttr)b.getAttr("lin")).dir = ((RotMoveAttr)b.getAttr("rot")).dir + 180),
                    new DisableAttrStage(0, "rot"),
                    new EnableAttrStage(0, "lin"),
                    new ModifyAttrStage(60, "lin.acc", (ba) -> ((LinAccelAttr)ba).accelAmount = 0.1),
                  }));
        }
      }
    }
  }
}
