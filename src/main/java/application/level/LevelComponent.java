package application.level;

// a node in a level event tree made up of segments and events
public abstract class LevelComponent {
  private int time = 0;
  private int duration = 0;
  public LevelComponent(int duration) {
    setDuration(duration);
    time = 0;
  }

  protected void setDuration(int duration) { // for setting duration in subclass constructors
    if (duration < 1)
      throw new IllegalArgumentException();
    this.duration = duration;
  }

  public int getDuration() {
    return duration;
  }

  public int getTime() {
    return time;
  }

  public final boolean isFinished() {
    return time == duration;
  }

  public final void tick() {
    if (isFinished()) return;
    action();
    time++;
  }

  public abstract void action(); // action to perform
}
