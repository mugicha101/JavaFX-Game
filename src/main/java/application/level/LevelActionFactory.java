package application.level;

import application.enemy.types.*;

public class LevelActionFactory {
  public static LevelAction singleEnemySpawn(Enemy[] templateEnemies) {
    return (time) -> {if (time == 0) for (Enemy te : templateEnemies) Enemy.spawn(te);};
  }

  public static LevelAction singleEnemySpawn(Enemy templateEnemy) {
    return singleEnemySpawn(new Enemy[] {templateEnemy});
  }

  public static LevelAction repeatedEnemySpawn(Enemy[] templateEnemies, int initialDelay, int repeatDelay) {
    return (time) -> {if (time >= initialDelay && (time - initialDelay) % repeatDelay == 0) for (Enemy te : templateEnemies) Enemy.spawn(te);};
  }

  public static LevelAction repeatedEnemySpawn(Enemy templateEnemy, int initialDelay, int repeatDelay) {
    return repeatedEnemySpawn(new Enemy[] {templateEnemy}, initialDelay, repeatDelay);
  }
}
