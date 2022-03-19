package application.level;

// group of level components (non-leaf node of level event tree)
public class LevelSegment extends LevelComponent {
  int lcIndex = 0;
  LevelComponent[] levelComponents;

  public LevelSegment(LevelComponent... levelComponents) {
    super(1);
    int cumDuration = 0;
    for (LevelComponent lc : levelComponents) {
      cumDuration += lc.getDuration();
    }
    setDuration(cumDuration);
    this.levelComponents = levelComponents;
    lcIndex = 0;
  }

  public void action() {
    if (lcIndex == levelComponents.length)
      return;
    levelComponents[lcIndex].tick();
    if (levelComponents[lcIndex].isFinished()) {
      levelComponents[lcIndex].reset();
      lcIndex++;
    }
  }

  @Override
  public void reset() {
    super.reset();
    for (LevelComponent lc : levelComponents) {
      lc.reset();
    }
    lcIndex = 0;
  }
}
