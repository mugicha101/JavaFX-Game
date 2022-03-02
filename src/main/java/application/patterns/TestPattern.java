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
    if (cycle % 20 == 0) {
      Position pos = new Position(width * (0.25 + Math.random() * 0.5), height * (0.2 + Math.random() * 0.2));
      double dir = Math.random() * 360;
      for (int i = 0; i < 36; i++) {
        for (int j = 2; j >= 0; j--) {
          Bullet.spawnBullet(
              new RiceBullet(
                  pos.clone().moveInDir(dir, 100),
                  1,
                  BulletColor.YELLOW,
                  new MoveAttr[] {
                    new RotMoveAttr("rot", 0, 3 + j * 0.2, dir + i * 10, 5, new LinAccelAttr("moveAcc", -0.1, 0), new LinAccelAttr("rotAcc", -0.1, 0)),
                    new LinMoveAttr("lin", 0, dir + i * 10, new LinAccelAttr("acc", 0.1, 3))
                  },
                  new BulletStage[] {
                    new DisableAttrStage(0, "lin"),
                    new ModifyStage(120 + j * 5, ModifyFactory.setColor(BulletColor.RED)),
                    new ModifyStage(0, (Bullet b) -> {((LinMoveAttr)b.getAttr("lin")).dir = ((RotMoveAttr)b.getAttr("rot")).dir;}),
                    new DisableAttrStage(0, "rot"),
                    new EnableAttrStage(0, "lin"),
                    new BlankStage(60)
                  }));
        }
      }
    }
  }
}
