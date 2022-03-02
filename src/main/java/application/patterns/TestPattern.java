package application.patterns;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.attr.LinAccelAttr;
import application.bullet.attr.LinMoveAttr;
import application.bullet.attr.MoveAttr;
import application.bullet.attr.RotMoveAttr;
import application.bullet.staging.BulletStage;
import application.bullet.staging.DisableStage;
import application.bullet.staging.EnableStage;
import application.bullet.types.Bullet;
import application.bullet.types.RiceBullet;

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
                    new LinMoveAttr("move", 1 + j * 0.2, dir + i * 10, new LinAccelAttr("acc", -0.1, -3))
                  },
                  new BulletStage[] {
                    new DisableStage("move.acc", 0), new EnableStage("move.acc", 60 + j * 3),
                  }));
        }
      }
    }
    if (cycle % 20 == 0) {
      double dir = Math.random() * 360;
      for (int i = 0; i < 8; i++) {
        Bullet.spawnBullet(
            new Bullet(
                new Position(width / 2, height * 0.2),
                1,
                BulletColor.RED,
                new MoveAttr[] {
                    new RotMoveAttr("move", 50, 15, dir + i * 45, 0.3*Math.sin(cycle*Math.PI/300), new LinAccelAttr("acc", -1, 2))
                }));
      }
    }
  }
}
