package application.patterns;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.bulletAttr.LinAccelAttr;
import application.bullet.bulletAttr.LinMoveAttr;
import application.bullet.bulletAttr.MoveAttr;
import application.bullet.bulletAttr.RotMoveAttr;
import application.bullet.bulletTypes.Bullet;
import application.bullet.bulletTypes.RiceBullet;

public class TestPattern extends Pattern {
  public TestPattern() {
    super("Test", 1000);
  }

  public void tick(int cycle, double width, double height) {
    if (cycle % 20 == 0) {
      Position pos = new Position(width * (0.25 + Math.random() * 0.5), height * (0.1 + Math.random() * 0.2));
      double dir = Math.random() * 360;
      for (int i = 0; i < 36; i++) {
        Bullet.spawnBullet(
            new RiceBullet(
                pos.clone().moveInDir(dir, 100),
                1,
                BulletColor.YELLOW,
                new MoveAttr[] {
                    new LinMoveAttr(null, 3, dir + i * 10, new LinAccelAttr(-0.05, -3))
                }));
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
                    new RotMoveAttr(null, 50, 15, dir + i * 45, 0.3*Math.sin(cycle*Math.PI/300), new LinAccelAttr(-1, 2))
                }));
      }
    }
  }
}
