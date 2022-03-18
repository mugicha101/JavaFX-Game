package application.stats;

public class Stats {
  public enum ProjType {
    BULLET, NEEDLE, PLASMA_NEEDLE
  }
  public enum LaserType {
    NONE, NORMAL, LARGE
  }
  public double hitboxRadius; // player hitbox radius
  public double grazeRadius; // player graze radius
  public double speed; // player unfocused speed
  public double focusMulti; // player focus speed multiplier from unfocused speed
  public double damage; // player damage
  public double firerate; // player firerate
  public double projSize; // projectile size
  public double projSpeed; // projectile speed
  public double projInacc; // inaccuracy in degrees of projectiles
  public double projAmount; // amount of projectiles per shot
  public double projPierce; // projectile pierce
  public boolean projHoming; // if true, projectiles home to nearest target
  public ProjType projType; // type of projectile
  public LaserType laserType; // laser type

  public Stats(double hitboxRadius, double grazeRadius, double speed, double focusMulti, double damage) {
    this.hitboxRadius = hitboxRadius;
    this.grazeRadius = grazeRadius;
    this.speed = speed;
    this.focusMulti = focusMulti;
    this.damage = damage;
  }

  public Stats clone() {
    return new Stats(hitboxRadius, grazeRadius, speed, focusMulti, damage);
  }
}