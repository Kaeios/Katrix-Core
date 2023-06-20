package fr.kaeios.plotting;

import fr.kaeios.api.computation.Function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FunctionPlotter {

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

    private final BufferedImage bufferedImage;
    private final Graphics2D graphics;

    private final Function[] functions;

    public FunctionPlotter(Function[] functions, double minX, double maxX, int sizeX, int sizeY) {
        this.functions = functions;

        this.minX = minX;
        this.maxX = maxX;

        this.bufferedImage =  new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        this.graphics = this.bufferedImage.createGraphics();

        this.graphics.translate(0, this.bufferedImage.getHeight());
        this.graphics.scale(1.0, -1.0);
    }

    public void plot() {
        // Clear
        graphics.setColor(Color.WHITE);
        graphics.setStroke(new BasicStroke(2));
        graphics.fillRect(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight());

        double[][] values = new double[functions.length][this.bufferedImage.getWidth()];

        double stepX = (maxX - minX) /this.bufferedImage.getWidth();

        for(int x = 0; x < this.bufferedImage.getWidth() ; x++) {
            for (int i = 0; i < functions.length; i++) {
                values[i][x] = functions[i].compute(minX + x * stepX);
                if(values[i][x] > maxY) this.maxY = values[i][x];
                if(values[i][x] < minY) minY = values[i][x];
            }
        }

        for (int i = 0; i < values.length; i++) {

            graphics.setColor(COLORS[i % COLORS.length]);

            for (int x = 0; x < values[i].length - 1; x++) {
                int yPos = (int) ((values[i][x] - minY)/(maxY - minY) * this.bufferedImage.getHeight());
                int nextYPos = (int) ((values[i][x+1] - minY)/(maxY - minY) * this.bufferedImage.getHeight());

                graphics.drawLine(x, yPos, x+1, nextYPos);
            }
        }
    }

    public void showAxis() {
        // Draw 1 axis
        graphics.setColor(Color.GRAY);
        int axisHeight = (int) Math.min(Math.max(0,  -minY/(maxY - minY) * this.bufferedImage.getHeight()), this.bufferedImage.getHeight() - 1);
        graphics.drawLine(0, axisHeight, this.bufferedImage.getWidth()-1, axisHeight);

        // Draw 2 axis
        int axisPos = (int) Math.min(Math.max(0,  -minX/(maxX - minX) * this.bufferedImage.getWidth()), this.bufferedImage.getWidth() - 1);
        graphics.drawLine(axisPos, 0, axisPos, this.bufferedImage.getHeight() - 1);
    }

    public void showGrid() {
        graphics.setColor(Color.GRAY);
        graphics.setStroke(new BasicStroke(1));

        int axisHeight = (int) Math.min(Math.max(0,  -minY/(maxY - minY) * this.bufferedImage.getHeight()), this.bufferedImage.getHeight() - 1);
        int axisPos = (int) Math.min(Math.max(0,  -minX/(maxX - minX) * this.bufferedImage.getWidth()), this.bufferedImage.getWidth() - 1);

        int axisStepY = this.bufferedImage.getHeight() / 10;

        for (int i = axisHeight - axisStepY * (axisHeight/ axisStepY); i < this.bufferedImage.getHeight(); i += axisStepY) {
            if(i == axisHeight) continue;
            graphics.drawLine(0, i, this.bufferedImage.getWidth() - 1, i);
        }

        int axisStepX = this.bufferedImage.getHeight() / 15;

        for (int i = axisPos - axisStepX * (axisPos/ axisStepX); i < this.bufferedImage.getWidth(); i += axisStepX) {
            if(i == axisPos) continue;
            graphics.drawLine(i, 0, i, this.bufferedImage.getHeight() - 1);
        }

        graphics.setStroke(new BasicStroke(2));
    }

    public void save() {
        File file = new File("myimage.png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
