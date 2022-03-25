package application.stats;

import javafx.scene.paint.Color;

public class Stats {
  public enum ProjType {
    BULLET,
    NEEDLE,
  }

  public enum LaserType {
    NONE,
    NORMAL,
    LARGE
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
  public double projOpacity; // opacity of projectiles
  public int projAmount; // amount of projectiles per shot
  public int projPierce; // projectile pierce
  public boolean projHoming; // if true, projectiles home to nearest target
  public ProjType projType; // type of projectile
  public LaserType laserType; // laser type
  public Color projColor; // color of projectile
  public boolean collidable; // can collide with enemy bullets

  public Stats(
      double hitboxRadius,
      double grazeRadius,
      double speed,
      double focusMulti,
      double damage,
      double firerate,
      double projSize,
      double projSpeed,
      double projInacc,
      double projOpacity,
      int projAmount,
      int projPierce,
      boolean projHoming,
      boolean collidable,
      ProjType projType,
      LaserType laserType,
      Color projColor
  ) {
    this.hitboxRadius = hitboxRadius;
    this.grazeRadius = grazeRadius;
    this.speed = speed;
    this.focusMulti = focusMulti;
    this.damage = damage;
    this.firerate = firerate;
    this.projSize = projSize;
    this.projSpeed = projSpeed;
    this.projInacc = projInacc;
    this.projOpacity = projOpacity;
    this.projAmount = projAmount;
    this.projPierce = projPierce;
    this.projHoming = projHoming;
    this.collidable = collidable;
    this.projType = projType;
    this.laserType = laserType;
    this.projColor = projColor;
  }

  public Stats clone() {
    return new Stats(hitboxRadius, grazeRadius, speed, focusMulti, damage, firerate, projSize, projSpeed, projInacc, projOpacity, projAmount, projPierce, projHoming, collidable, projType, laserType, projColor);
  }
}
