package application.stats;

import java.util.HashSet;

public class StatManager {
  private final Stats baseStats;
  private final HashSet<Item> itemSet = new HashSet<>();
  public Stats stats;

  public StatManager(Stats baseStats) {
    this.baseStats = baseStats;
    updateStats();
  }

  // checks if items contains synergyItems, if so remove synergyItems from items
  private boolean synergyCheck(HashSet<Item> items, Item... synergyItems) {
    for (Item item : synergyItems) {
      if (!items.contains(item))
        return false;
    }
    for (Item item : synergyItems)
      items.remove(item);
    return true;
  };

  public void updateStats() {
    stats = baseStats.clone();
    HashSet<Item> items = new HashSet<>(itemSet); // clone itemSet first
    // handle synergies
    if (synergyCheck(items, Item.AttackNeedles, Item.PlasmaCore)) {
      stats.projType = Stats.ProjType.PLASMA_NEEDLE;
      stats.projSpeed *= 2.5;
      stats.projSize *= 0.75;
      stats.firerate *= 3;
      stats.damage *= 0.75;
      stats.projInacc *= 0.5;
      stats.projPierce += 100;
    }

    // handle rest
    for (Item item : items) {
      switch (item) {
        case BoxingGloves -> {
          stats.projSpeed *= 1.5;
          stats.damage *= 1.2;
        }
        case PlasmaCore -> {
          stats.laserType = Stats.LaserType.NONE;
          stats.firerate *= 1.5;
        }
      }
    }
  }

  public void addItem(Item item) {
    itemSet.add(item);
    updateStats();
  }

  public void removeItem(Item item) {
    itemSet.remove(item);
    updateStats();
  }

  public void clearItems() {
    itemSet.clear();
    updateStats();
  }

  public boolean hasItem(Item item) {
    return itemSet.contains(item);
  }
}
