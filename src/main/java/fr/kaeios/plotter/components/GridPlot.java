package fr.kaeios.plotter.components;

import fr.kaeios.api.plotting.PositionedElement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GridPlot implements PositionedElement {

    private final double minX;
    private final double maxX;

    private final double minY;
    private final double maxY;

    private final BufferedImage image;
    private final Graphics2D graphics;

    public GridPlot(double minX, double maxX, double minY, double maxY, int sizeX, int sizeY) {
        this.minX = minX;
        this.maxX = maxX;

        this.minY = minY;
        this.maxY = maxY;

        this.image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        this.graphics = this.image.createGraphics();

        plot();
    }

    private void plot() {
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, this.image.getWidth(), this.image.getHeight());
        graphics.setComposite(AlphaComposite.Src);

        graphics.setColor(Color.GRAY);
        graphics.setStroke(new BasicStroke(1));

        int axisHeight = this.image.getHeight() - (int) Math.min(Math.max(20,  -minY/(maxY - minY) * this.image.getHeight()), this.image.getHeight() - 21);
        int axisPos = (int) Math.min(Math.max(20,  -minX/(maxX - minX) * this.image.getWidth()), this.image.getWidth() - 21);

        int axisStepY = this.image.getHeight() / 12;
        int axisStepX = this.image.getWidth() / 16;

        for (int i = axisHeight - axisStepY * (axisHeight/ axisStepY); i < this.image.getHeight(); i += axisStepY) {
            if(i == axisHeight) continue;
            graphics.drawLine(0, this.image.getHeight() - i, this.image.getWidth() - 1, this.image.getHeight() - i);
        }


        for (int i = axisPos - axisStepX * (axisPos/ axisStepX); i < this.image.getWidth(); i += axisStepX) {
            if(i == axisPos) continue;
            graphics.drawLine(i, this.image.getHeight() - 1, i, 0);
        }
    }

    @Override
    public int getX() {
        return 20;
    }

    @Override
    public int getY() {
        return 20;
    }

    @Override
    public Image render() {
        return image;
    }
}
