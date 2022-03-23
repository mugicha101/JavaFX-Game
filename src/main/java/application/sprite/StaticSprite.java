package application.sprite;

import javafx.scene.Group;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class StaticSprite extends Sprite {
  private final Image img;
  private final String imgPath;
  public StaticSprite(Group sceneGroup, String imgPath, double[] offset, double scale)
          throws IOException {
    super(sceneGroup, offset, scale);
    this.imgPath = imgPath;
    InputStream stream = new FileInputStream("src/main/java/application/images/" + imgPath);
    img = new Image(stream);
    stream.close();
  }

  protected Image getImage() {
    return img;
  }

  @Override
  public StaticSprite clone() {
    try {
      return new StaticSprite(getSceneGroup(), imgPath,  new double[] {offset[0], offset[1]}, scale);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
