package application.attack;

import javafx.scene.Group;

import java.util.ArrayList;

public abstract class PlayerAttack {
  private static ArrayList<PlayerAttack> playerAttacks = new ArrayList<>(); // all active attacks (moved and rendered in game)
  public static final Group playerAttackGroup = new Group();
  public static void moveAttacks() { // move all attacks
    ArrayList<PlayerAttack> activeAttacks = new ArrayList<>();
    for (PlayerAttack attack : playerAttacks) {
      attack.moveTick();
      if (attack.active)
        activeAttacks.add(attack);
    }
    playerAttacks = activeAttacks;
  }
  public static void drawAttacks() { // draw-update all attacks
    for (PlayerAttack attack : playerAttacks)
      attack.drawTick();
  }

  private boolean active;
  protected final Group group;
  public PlayerAttack(Group group) {
    playerAttacks.add(this);
    active = true;
    this.group = group;
    playerAttackGroup.getChildren().add(group);
  }
  protected void kill() { // call to remove bullet
    if (!active)
      return;
    active = false;
    playerAttackGroup.getChildren().remove(group);
    killHelper();
  }

  public abstract void moveTick(); // 1 tick of movement (move, collision, etc.)
  public abstract void drawTick(); // update bullet rendering
  protected abstract void killHelper(); // removes all scenegraph objects and other stuff for deletion
}
