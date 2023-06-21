package fr.kaeios.plotting;

import fr.kaeios.api.computation.Function;
import fr.kaeios.api.plotting.PositionedElement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FunctionPlot implements PositionedElement {

    private static final Color[] COLORS = new Color[] {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.ORANGE,
            Color.MAGENTA
    };

    private final double minX;
    private final double maxX;

    private double minY = Double.MAX_VALUE;
    private double maxY = Double.MIN_VALUE;

    private final BufferedImage image;
    private final Graphics2D graphics;

    private final Function[] functions;

    public FunctionPlot(Function[] functions, double minX, double maxX, int sizeX, int sizeY) {
        this.functions = functions;

        this.minX = minX;
        this.maxX = maxX;

        this.image =  new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        this.graphics = this.image.createGraphics();

        plot();
    }

    private void plot() {
        // Clear
        graphics.setComposite(AlphaComposite.Clear);
        graphics.setStroke(new BasicStroke(2));
        graphics.fillRect(0, 0, this.image.getWidth(), this.image.getHeight());
        graphics.setComposite(AlphaComposite.Src);

        double[][] values = new double[functions.length][this.image.getWidth()];

        double stepX = (maxX - minX) /this.image.getWidth();

        for(int x = 0; x < this.image.getWidth() ; x++) {
            for (int i = 0; i < functions.length; i++) {
                values[i][x] = functions[i].compute(minX + x * stepX);
                if(values[i][x] > maxY) this.maxY = values[i][x];
                if(values[i][x] < minY) minY = values[i][x];
            }
        }

        for (int i = 0; i < values.length; i++) {

            graphics.setColor(COLORS[i % COLORS.length]);

            for (int x = 0; x < values[i].length - 1; x++) {
                int yPos = this.image.getHeight() - (int) ((values[i][x] - minY)/(maxY - minY) * this.image.getHeight());
                int nextYPos = this.image.getHeight() - (int) ((values[i][x+1] - minY)/(maxY - minY) * this.image.getHeight());

                graphics.drawLine(x, yPos, x+1, nextYPos);
            }
        }
    }

    @Override
    public Image render() {
        return image;
    }

    @Override
    public int getX() {
        return 20;
    }

    @Override
    public int getY() {
        return 20;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }
}
