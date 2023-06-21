package fr.kaeios.plotter.components;

import fr.kaeios.api.plotting.PositionedElement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AxisPlot implements PositionedElement {

    private final double minX;
    private final double maxX;

    private final double minY;
    private final double maxY;

    private final int sizeX;
    private final int sizeY;

    private final int offset;

    private final BufferedImage image;
    private final Graphics2D graphics;

    public AxisPlot(double minX, double maxX, double minY, double maxY, int sizeX, int sizeY, int offset) {
        this.minX = minX;
        this.maxX = maxX;

        this.minY = minY;
        this.maxY = maxY;

        this.offset = offset;

        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.image = new BufferedImage(sizeX + 2 * offset, sizeY + 2 * offset, BufferedImage.TYPE_INT_ARGB);
        this.graphics = this.image.createGraphics();

        plot();
    }

    private void plot() {
        graphics.setStroke(new BasicStroke(3));

        graphics.setColor(Color.GRAY);

        int axisHeight = (int) Math.min(Math.max(0,  -minY/(maxY - minY) * sizeY), sizeY - 1);
        graphics.drawLine(offset, sizeY - axisHeight + offset, sizeX - 1 + offset, sizeY - axisHeight + offset);

        int axisPos = (int) Math.min(Math.max(0,  -minX/(maxX - minX) * sizeX), sizeX - 1);
        graphics.drawLine(axisPos + offset, offset, axisPos + offset, sizeY - 1 + offset);
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public Image render() {
        return image;
    }
}
