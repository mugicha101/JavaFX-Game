package application.bg;

import application.OpenSimplexNoise;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class BackgroundLayer {
    protected static final OpenSimplexNoise os = new OpenSimplexNoise();
    private WritableImage generatingBackground;
    private WritableImage mainBackground;
    private final ImageView gbgIv;
    private final ImageView mbgIv;
    private int bgOffset = 0;
    private final int width;
    private final int height;
    private final int scrollSpeed;
    public BackgroundLayer(Group group, int width, int height, int scrollSpeed) {
        this.width = width;
        this.height = height;
        this.scrollSpeed = scrollSpeed;
        mbgIv = new ImageView();
        gbgIv = new ImageView();
        group.getChildren().addAll(mbgIv, gbgIv);
        mainBackground = new WritableImage(width, height);
        generatingBackground = new WritableImage(width, height);
        mbgIv.setImage(mainBackground);
        gbgIv.setImage(generatingBackground);
    }

    public final void drawUpdate() {
        for (int i = 0; i < scrollSpeed; i++) {
            advanceBG();
        }
    }

    private void advanceBG() {
        if (bgOffset != 0 && bgOffset % height == 0) { // move generating to main
            mbgIv.setImage(generatingBackground);
            generatingBackground = new WritableImage(width, height);
            gbgIv.setImage(generatingBackground);
        }
        for (int x = 0; x < generatingBackground.getWidth(); x++)
            generatingBackground.getPixelWriter().setColor(x, (height - bgOffset % height - 1), bgPixelColor(x, -bgOffset));
        mbgIv.setTranslateY(bgOffset % height);
        gbgIv.setTranslateY(bgOffset % height - height);
        bgOffset++;
    }

    public final void init() {
        for (int y = 0; y < mainBackground.getHeight(); y++)
            for (int x = 0; x < mainBackground.getWidth(); x++)
                mainBackground.getPixelWriter().setColor(x, y, bgPixelColor(x, y - bgOffset));
    }

    protected abstract Color bgPixelColor(int x, int y);
}
