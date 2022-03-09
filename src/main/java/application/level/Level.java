package application.level;

// a single level/stage of the game (with enemies, background, patterns, boss, etc.)
public class Level {
  private static Level activeLevel = null;
  public static void setActive(Level level) {
    level.reset();
    activeLevel = level;
  }

  public static Level getActive() {
    return activeLevel;
  }

  public static void tickActive() {
    if (activeLevel != null) activeLevel.tick();
  }

  private final LevelComponent root;
  public Level(LevelComponent eventTreeRoot) {
    root = eventTreeRoot;
  }

  public void reset() {
    root.reset();
  }

  public void tick() {
    if (!root.isFinished())
      root.tick();
  }

  public boolean isFinished() {
    return root.isFinished();
  }
}
