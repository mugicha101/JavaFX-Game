package application.sprite;

import javafx.scene.Group;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class StaticSprite extends Sprite {
  private Image img;
  private final String imgPath;
  public StaticSprite(Group sceneGroup, String imgPath, double[] offset, double scale)
          throws FileNotFoundException {
    super(sceneGroup, offset, scale);
    this.imgPath = imgPath;
    InputStream stream = new FileInputStream("src/main/java/application/images/" + imgPath);
    img = new Image(stream);
  }

  protected Image getImage() {
    return img;
  }

  @Override
  public StaticSprite clone() {
    try {
      return new StaticSprite(getSceneGroup(), imgPath,  new double[] {offset[0], offset[1]}, scale);
    } catch (FileNotFoundException e) {
      throw new RuntimeException();
    }
  }
}
