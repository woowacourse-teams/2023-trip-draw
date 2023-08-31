package dev.tripdraw.draw.domain;

import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_ROUND;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class RouteImageDrawer {

    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final Color GRAY = new Color(0xD0, 0xD0, 0xDA);
    private static final int LINE_STROKE_WIDTH = 7;
    private static final Stroke LINE_STROKE = new BasicStroke(LINE_STROKE_WIDTH, CAP_ROUND, JOIN_ROUND);
    private static final Color BLUE = new Color(0x17, 0x46, 0xA2);
    private static final int POINT_STROKE_WIDTH = 20;
    private static final Stroke POINT_STROKE = new BasicStroke(POINT_STROKE_WIDTH, CAP_ROUND, JOIN_ROUND);
    private static final Map<Object, Object> renderingHints = Map.of(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY,
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC
    );

    private final BufferedImage bufferedImage;
    private final Graphics2D graphics2D;

    public RouteImageDrawer(BufferedImage bufferedImage, Graphics2D graphics2D) {
        this.bufferedImage = bufferedImage;
        this.graphics2D = graphics2D;
    }

    public static RouteImageDrawer from(int imageSize) {
        BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, IMAGE_TYPE);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setBackground(TRANSPARENT);
        graphics2D.clearRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics2D.setRenderingHints(renderingHints);
        graphics2D.setStroke(LINE_STROKE);
        graphics2D.setPaint(GRAY);
        return new RouteImageDrawer(bufferedImage, graphics2D);
    }

    public void drawLine(Positions positions) {
        graphics2D.setStroke(LINE_STROKE);
        graphics2D.setPaint(GRAY);

        List<Integer> xPositions = positions.xPositions();
        List<Integer> yPositions = positions.yPositions();

        for (int i = 0; i < positions.size() - 1; i++) {
            graphics2D.draw(generateLine(xPositions, yPositions, i));
        }
    }

    private Line2D.Double generateLine(List<Integer> xPositions, List<Integer> yPositions, int i) {
        return new Line2D.Double(xPositions.get(i), yPositions.get(i), xPositions.get(i + 1), yPositions.get(i + 1));
    }

    public void drawPoint(Positions positions) {
        graphics2D.setStroke(POINT_STROKE);
        graphics2D.setPaint(BLUE);

        List<Integer> xPositions = positions.xPositions();
        List<Integer> yPositions = positions.yPositions();

        for (int i = 0; i < positions.size(); i++) {
            graphics2D.draw(generatePoint(xPositions, yPositions, i));
        }
    }

    private Line2D.Double generatePoint(List<Integer> xPositions, List<Integer> yPositions, int i) {
        return new Line2D.Double(xPositions.get(i), yPositions.get(i), xPositions.get(i), yPositions.get(i));
    }

    public void dispose() {
        graphics2D.dispose();
    }
}
