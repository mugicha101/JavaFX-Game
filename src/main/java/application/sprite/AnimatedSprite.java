package application.sprite;

import application.Game;
import application.Position;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AnimatedSprite extends Sprite {
  private final String[] imgPaths;
  private Image[] imgs;
  private final int frameDelay;

  public AnimatedSprite(Group sceneGroup, String[] imgPaths, double[] offset, double scale, int frameDelay)
      throws FileNotFoundException {
    super(sceneGroup, offset, scale);
    this.frameDelay = frameDelay;
    this.alpha = 1;
    this.imgPaths = imgPaths;
    setImages(imgPaths);
  }

  private void setImages(String[] filePaths)
      throws FileNotFoundException {
    imgs = new Image[filePaths.length];
    for (int i = 0; i < filePaths.length; i++) {
      InputStream stream = new FileInputStream("src/main/java/application/images/" + filePaths[i]);
      Image image = new Image(stream);
      imgs[i] = image;
    }
  }

  @Override
  protected Image getImage() {
    return imgs[(int) ((double) Game.frame / frameDelay) % imgs.length];
  }

  @Override
  public AnimatedSprite clone() {
    try {
      return new AnimatedSprite(getSceneGroup(), imgPaths, new double[] {offset[0], offset[1]}, scale, frameDelay);
    } catch (FileNotFoundException e) {
      throw new RuntimeException();
    }
  }
}
