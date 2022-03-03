package application.bullet.staging;

import application.bullet.attr.BulletAttr;

// NOTE: default border collision only works when stages are done

// stages when to enable, disable, and modify bullet attributes during bullet's lifetime
public abstract class BulletStage {
  private final int time;

  public BulletStage(int time) {
    this.time = time;
  }

  public final int getTime() {
    return time;
  }
}
