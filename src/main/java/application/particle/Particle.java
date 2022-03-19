package application.particle;

import application.Position;
import application.sprite.Sprite;
import javafx.scene.Group;

import java.util.ArrayList;

public abstract class Particle {
  private static ArrayList<Particle> particleList = new ArrayList<>();
  public static final Group particleGroup = new Group();

  public static void moveParticles() {
    ArrayList<Particle> aliveParticles = new ArrayList<>();
    for (Particle p : particleList) {
      p.move();
      if (p.isAlive())
        aliveParticles.add(p);
      else
        p.delete();
    }
    particleList = aliveParticles;
  }

  public static void drawParticles() {
    for (Particle p : particleList)
      p.drawUpdate();
  }


  protected final Position pos;
  protected double dir;
  private final double speed;
  private final int lifetime;
  private int time;

  public Particle(Position pos, double dir, double speed, int time) {
    this.dir = dir;
    this.pos = pos.clone();
    this.speed = speed;
    lifetime = time;
    this.time = 0;
    particleList.add(this);
  }

  public double getProgress() {
    return (double)time / lifetime;
  }

  public boolean isAlive() {
    return time < lifetime;
  }

  public final void move() {
    pos.moveInDir(dir, speed);
    time++;
  }

  public abstract void drawUpdate();

  public abstract void delete();
}
