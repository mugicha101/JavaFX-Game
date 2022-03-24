package application.bullet.staging;

public class BlankStage extends BulletStage {
  public BlankStage(int time) {
    super(time);
  }

  public void action() {}

  public BlankStage clone() {
    return new BlankStage(getTime());
  }
}
