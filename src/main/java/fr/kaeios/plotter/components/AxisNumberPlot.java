package fr.kaeios.plotter.components;

import fr.kaeios.api.plotting.PositionedElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AxisNumberPlot implements PositionedElement {

    private final double minX;
    private final double maxX;

    private final double minY;
    private final double maxY;

    private final BufferedImage image;
    private final Graphics2D graphics;

    private final int offset;

    private final int sizeX;
    private final int sizeY;

    public AxisNumberPlot(double minX, double maxX, double minY, double maxY, int sizeX, int sizeY, int offset) {
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
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, this.image.getWidth(), this.image.getHeight());
        graphics.setComposite(AlphaComposite.Src);

        graphics.setColor(Color.GRAY);
        graphics.setStroke(new BasicStroke(1));

        int axisHeight = sizeY - (int) Math.min(Math.max(0,  -minY/(maxY - minY) * sizeY), sizeY - 1);
        int axisPos = (int) Math.min(Math.max(0,  -minX/(maxX - minX) * sizeX), sizeX -1);

        int axisStepY = sizeY / 12;
        int axisStepX = sizeX / 16;

        for (int i = axisHeight - axisStepY * (axisHeight/ axisStepY); i < this.sizeY; i += axisStepY) {

            double value = ((1.0D - ((double) i / sizeY)) * (maxY-minY)) + minY;
            value = Math.ceil(value * 100) / 100;
            graphics.drawString(
                    String.format("%.2f", value),
                    0,
                    i + offset
            );
        }


        for (int i = axisPos - axisStepX * (axisPos/ axisStepX); i < this.sizeX; i += axisStepX) {
            double value = ((double) i / sizeX) * (maxX-minX) + minX;
            value = Math.ceil(value * 100) / 100;
            graphics.drawString(
                    String.format("%.2f", value),
                    i + offset/2,
                    sizeY + offset + offset/2
            );
          //  graphics.drawLine(i, 0, i, this.image.getHeight());
        }

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
