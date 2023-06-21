package fr.kaeios.plotter.components;

import fr.kaeios.api.plotting.PositionedElement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ContourPlot implements PositionedElement {

    private final BufferedImage image;
    private final Graphics2D graphics;

    public ContourPlot(int sizeX, int sizeY) {

        this.image =  new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        this.graphics = this.image.createGraphics();

        plot();
    }

    private void plot() {
        graphics.setStroke(new BasicStroke(5));
        graphics.setColor(Color.GRAY);

        graphics.drawRect(0, 0, this.image.getWidth() - 1, this.image.getHeight() - 1);
    }

    @Override
    public int getX() {
        return 30;
    }

    @Override
    public int getY() {
        return 30;
    }

    @Override
    public Image render() {
        return image;
    }
}
