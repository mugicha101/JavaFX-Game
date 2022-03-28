package application.sprite;

import application.Game;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class AnimatedSprite extends Sprite {
  private final String[] imgPaths;
  private Image[] imgs;
  private final int frameDelay;

  public AnimatedSprite(Group sceneGroup, String[] imgPaths, double[] offset, double scale, int frameDelay) {
    super(sceneGroup, offset, scale);
    this.frameDelay = frameDelay;
    this.alpha = 1;
    this.imgPaths = imgPaths;
    setImages(imgPaths);
  }

  private void setImages(String[] filePaths) {
    imgs = new Image[filePaths.length];
    for (int i = 0; i < filePaths.length; i++) {
      imgs[i] = createImage(filePaths[i]);
    }
  }

  @Override
  protected Image getImage() {
    return imgs[(int) ((double) Game.frame / frameDelay) % imgs.length];
  }

  @Override
  public AnimatedSprite clone() {
    return new AnimatedSprite(
        getSceneGroup(), imgPaths, new double[] {offset[0], offset[1]}, scale, frameDelay);
  }
}
