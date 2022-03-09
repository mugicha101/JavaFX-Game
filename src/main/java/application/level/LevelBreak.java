package application.level;

public class LevelBreak extends LevelEvent {
  public LevelBreak(int duration) {
    super(duration, LevelActionFactory.actionBreak());
  }
}
