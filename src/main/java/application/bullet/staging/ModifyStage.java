package application.bullet.staging;

import application.bullet.types.Bullet;

public class ModifyStage extends BulletStage {
  private final Modify mod;

  public ModifyStage(int time, Modify mod) {
    super(time);
    this.mod = mod;
  }

  public void action(Bullet b) {
    mod.action(b);
  }

  public ModifyStage clone() {
    return new ModifyStage(getTime(), mod);
  }
}
