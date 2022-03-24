package application.bg;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class CloudLayer extends BackgroundLayer {
    private final double stretch;
    private final Color cloudColor;

    public CloudLayer(Group group, int width, int height, int scrollSpeed, Color cloudColor, double stretch) {
        super(group, width, height, scrollSpeed);
        this.cloudColor = cloudColor;
        this.stretch = stretch;
    }

    protected Color bgPixelColor(int x, int y) {
        double v = 0.5 + os.eval(x/stretch/3, y/stretch/3, 0) * 0.5;
        return Color.color(cloudColor.getRed(), cloudColor.getGreen(), cloudColor.getBlue(), v * cloudColor.getOpacity());
    }
}
