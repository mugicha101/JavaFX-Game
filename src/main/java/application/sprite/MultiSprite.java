package application.sprite;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

// sprite that can toggle between different sprites
public class MultiSprite extends Sprite {
  private final HashMap<String, Sprite> spriteMap;
  private final String startState;
  private String state;
  private Sprite prevSprite;

  public MultiSprite(Group sceneGroup, HashMap<String, Sprite> spriteMap, double[] offset, double scale, String startState)
          throws FileNotFoundException {
    super(sceneGroup, offset, scale);
    prevSprite = null;
    this.spriteMap = spriteMap;
    for (Sprite sprite : spriteMap.values()) {
      sprite.disable();
    }
    this.startState = startState;
    state = startState;
  }

  protected Image getImage() { // not used since drawUpdate is overriden
    return null;
  }

  @Override
  public void drawUpdate() {
    if (prevSprite != null)
      prevSprite.disable();
    Sprite sprite = spriteMap.get(state);
    sprite.setSceneGroup(getSceneGroup());
    sprite.pos.set(pos);
    sprite.dir = dir;
    sprite.alpha = alpha;
    double spriteScale = sprite.scale;
    sprite.scale *= scale;
    sprite.drawUpdate();
    sprite.scale = spriteScale;
    sprite.enable();
    prevSprite = sprite;
  }

  @Override
  public MultiSprite clone() {
    try {
      return new MultiSprite(getSceneGroup(), spriteMap, offset, scale, startState);
    } catch (FileNotFoundException e) {
      throw new RuntimeException();
    }
  }
}
