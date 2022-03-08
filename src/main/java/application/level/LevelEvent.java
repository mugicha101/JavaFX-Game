package application.level;

// does a single action (leaf node of level event tree)
public class LevelEvent extends LevelComponent {
  private LevelAction action;
  public LevelEvent(int duration, LevelAction action) {
    super(duration);
    this.action = action;
  }

  public void action() {
    action.run(getTime());
  }
}
