package application.level;

import application.enemy.types.*;

public class LevelActionFactory {
  public static LevelAction actionBreak() {
    return (time) -> {};
  }

  public static LevelAction singleEnemySpawn(Enemy... templateEnemies) {
    return (time) -> {if (time == 0) for (Enemy te : templateEnemies) Enemy.spawn(te);};
  }

  public static LevelAction repeatedEnemySpawn(int initialDelay, int repeatDelay, Enemy... templateEnemies) {
    return (time) -> {if (time >= initialDelay && (time - initialDelay) % repeatDelay == 0) for (Enemy te : templateEnemies) Enemy.spawn(te);};
  }
}
