package application.stats;

import javafx.scene.paint.Color;

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

    // pre synergy merging visuals
    if (items.contains(Item.BoxingGloves)) {
      stats.projColor = Color.RED;
    }
    if (items.contains(Item.Anvil)) {
      stats.projColor = Color.color(0.5, 0.5, 0.5);
    }
    if (items.contains(Item.TruePrecision)) {
      stats.projColor = Color.LIGHTBLUE;
    }

    // handle synergies and their visuals
    if (synergyCheck(items, Item.AttackNeedles, Item.PlasmaCore)) {
      stats.projColor = Color.MAGENTA;
      stats.projType = Stats.ProjType.NEEDLE;
      stats.projSpeed *= 2.5;
      stats.projSize *= 0.75;
      stats.firerate *= 3;
      stats.damage *= 0.75;
      stats.projInacc *= 0.5;
      stats.projPierce += 100;
    }

    // post synergy merging visuals
    if (items.contains(Item.AttackNeedles)) {
      stats.projType = Stats.ProjType.NEEDLE;
    }
    if (items.contains(Item.RainStorm)) {
      stats.projColor = Color.color(0, 0.5, 1);
    }

    // handle stat changes
    for (Item item : items) {
      switch (item) {
        case BoxingGloves -> {
          stats.projSpeed *= 1.5;
          stats.damage *= 1.2;
        }
        case PlasmaCore -> {
          stats.laserType = Stats.LaserType.NORMAL;
          stats.firerate *= 1.5;
        }
        case AttackNeedles -> {
          stats.projSpeed *= 1.5;
          stats.projSize *= 0.75;
          stats.firerate *=  2;
          stats.projInacc *= 0.5;
          stats.damage *= 0.5;
          stats.projPierce += 2;
        }
        case TruePrecision -> {
          stats.projInacc *= 0;
          stats.projSpeed *= 1.25;
        }
        case Delineator -> {
          stats.projAmount *= 2;
          stats.damage *= 0.5;
          stats.projSpeed *= 0.65;
          stats.projInacc *= 0.25;
        }
        case DoubleShot -> {
          stats.projAmount *= 2;
          stats.firerate *= 0.8;
          stats.damage *= 0.8;
        }
        case TripleShot -> {
          stats.projAmount *= 3;
          stats.firerate *= 0.65;
          stats.damage *= 0.65;
        }
        case Shotgun -> {
          stats.projAmount *= 10;
          stats.firerate *= 0.25;
          stats.damage *= 0.75;
          stats.projInacc *= 2;
        }
        case Anvil -> {
          stats.damage *= 2;
          stats.firerate *= 0.75;
          stats.projSpeed *= 0.75;
        }
        case InflatableBalloon -> {
          stats.projSize *= 2;
          stats.projSpeed *= 0.8;
          stats.projOpacity *= 0.5;
        }
        case RainStorm -> {
          stats.firerate *= 10;
          stats.damage *= 0.15;
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
