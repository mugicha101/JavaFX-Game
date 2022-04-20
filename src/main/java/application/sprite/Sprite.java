package application.sprite;

import application.Game;
import application.Position;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Objects;

public abstract class Sprite {
  private static final String basePath = "images/";
  protected final ImageView iv;
  public double scale; // scale of image
  protected final double[] offset; // offset in pixels of image
  public double alpha;
  public Position pos; // dont make final to allow for shared references
  public double dir; // direction
  private Group sceneGroup; // scene group that the sprite is in
  private boolean enabled;
  protected static final HashMap<String, Image> imgCache = new HashMap<>();

  protected Group getSceneGroup() {
    return sceneGroup;
  }

  public void setSceneGroup(Group sceneGroup) {
    if (this.sceneGroup == sceneGroup)
      return;
    if (enabled) {
      disable();
      this.sceneGroup = sceneGroup;
      enable();
    } else {
      this.sceneGroup = sceneGroup;
    }
  }

  public Sprite(Group sceneGroup, double[] offset, double scale) {
    enabled = false;
    iv = new ImageView();
    this.sceneGroup = sceneGroup;
    this.offset = offset == null ? new double[] {0, 0} : offset;
    this.scale = scale;
    pos = new Position(0, 0);
    dir = 0;
    alpha = 1;
  }

  public final ImageView getIv() {
    return iv;
  }

  protected abstract Image getImage();

  public void drawUpdate() {
    Image img = getImage();
    if (iv.getImage() != img) iv.setImage(img);
    double x = pos.x - img.getWidth() / 2 + offset[0] * scale;
    double y = pos.y - img.getHeight() / 2 + offset[1] * scale;
    if (alpha != iv.getOpacity())
      iv.setOpacity(alpha);
    if (iv.getX() != x) iv.setX(x);
    if (iv.getY() != y) iv.setY(y);
    if (iv.getScaleX() != scale) {
      iv.setScaleX(scale);
      iv.setScaleY(scale);
    }
    if (iv.getRotate() != -dir) iv.setRotate(-dir);
  }

  public void enable() {
    if (enabled)
      return;
    if (sceneGroup != null) sceneGroup.getChildren().add(iv);
    enabled = true;
  }
  public void disable() {
    if (!enabled)
      return;
    if (sceneGroup != null) sceneGroup.getChildren().remove(iv);
    enabled = false;
  }

  public abstract Sprite clone();

  protected Image createImage(String imgPath) {
    if (!imgCache.containsKey(imgPath))
      imgCache.put(imgPath, new Image(Objects.requireNonNull(Game.class.getResourceAsStream(basePath + imgPath))));
    return imgCache.get(imgPath);
  }
}
