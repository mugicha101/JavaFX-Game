package application.level;

// a single level/stage of the game (with enemies, background, patterns, boss, etc.)
public class Level {
  private LevelSegment root;
  public Level(LevelSegment eventTreeRoot) {
    root = eventTreeRoot;
  }

  public void tick() {
    if (!root.isFinished())
      root.tick();
  }

  public boolean finished() {
    return root.isFinished();
  }
}
