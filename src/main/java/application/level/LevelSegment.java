package application.level;

// group of level components (non-leaf node of level event tree)
public class LevelSegment extends LevelComponent {
  int lcIndex = 0;
  LevelComponent[] levelComponents;
  public LevelSegment(LevelComponent[] levelActions) {
    super(0);
    int cumDuration = 0;
    for (LevelComponent lc : levelActions) {
      cumDuration += lc.getDuration();
    }
    setDuration(cumDuration);
    this.levelComponents = levelActions;
    lcIndex = 0;
  }

  public void action() {
    if (lcIndex == levelComponents.length)
      return;
    levelComponents[lcIndex].tick();
    if (levelComponents[lcIndex].isFinished())
      lcIndex++;
  }
}
