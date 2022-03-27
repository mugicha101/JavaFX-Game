package application;

import javafx.scene.input.KeyCode;
import javafx.util.Pair;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Input {
  private static final HashMap<String, Input> inputMap = new HashMap<>();
  private static final ArrayList<Pair<KeyCode, Boolean>> inputQueue =
      new ArrayList<>(); // async -> sync

  public static void init() {
    inputMap.put("left", new Input(KeyCode.A, KeyCode.LEFT));
    inputMap.put("right", new Input(KeyCode.D, KeyCode.RIGHT));
    inputMap.put("up", new Input(KeyCode.W, KeyCode.UP));
    inputMap.put("down", new Input(KeyCode.S, KeyCode.DOWN));
    inputMap.put("shoot", new Input(KeyCode.Z, KeyCode.BACK_SLASH));
    inputMap.put("focus", new Input(KeyCode.SHIFT));
    inputMap.put("debug", new Input(KeyCode.F5));
    inputMap.put("fullscreen", new Input(KeyCode.F));
    inputMap.put("pause", new Input(KeyCode.ESCAPE));
  }

  public static Input getInput(String name) {
    return inputMap.get(name);
  }

  public static void keyRequest(KeyCode key, boolean press) {
    inputQueue.add(new Pair<>(key, press));
  }

  private static void keyPress(KeyCode key) {
    for (Input in : inputMap.values()) {
      if (in.hasKey(key)) in.press();
    }
  }

  private static void keyRelease(KeyCode key) {
    for (Input in : inputMap.values()) {
      if (in.hasKey(key)) in.release();
    }
  }

  public static void keyTick() {
    for (Input in : inputMap.values()) in.tick();
    for (Pair<KeyCode, Boolean> req : inputQueue) {
      if (req.getValue()) keyPress(req.getKey());
      else keyRelease(req.getKey());
    }
    inputQueue.clear();
  }

  private boolean pressed = false;
  private boolean initial = false;
  private HashSet<KeyCode> keys;

  public Input(KeyCode... keys) {
    this.keys = new HashSet<>();
    this.keys.addAll(Arrays.asList(keys));
  }

  public boolean hasKey(KeyCode key) {
    return keys.contains(key);
  }

  public void press() {
    pressed = true;
    initial = true;
  }

  public void release() {
    pressed = false;
    initial = true;
  }

  public void tick() {
    initial = false;
  }

  public boolean isPressed() {
    return pressed;
  }

  public boolean onInitialPress() {
    return initial && pressed;
  }

  public boolean onInitialRelease() {
    return initial && !pressed;
  }
}
