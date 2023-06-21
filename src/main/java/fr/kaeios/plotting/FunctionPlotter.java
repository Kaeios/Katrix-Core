package fr.kaeios.plotting;

import fr.kaeios.api.computation.Function;
import fr.kaeios.api.plotting.PositionedElement;
import fr.kaeios.api.plotting.RenderedComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionPlotter {

    private final List<PositionedElement> components = new ArrayList<>();

    public FunctionPlotter(Function[] functions, double minX, double maxX, int sizeX, int sizeY) {
        FunctionPlot functionPlot = new FunctionPlot(functions, minX, maxX, sizeX, sizeY);
        GridPlot gridPlot = new GridPlot(minX, maxX, functionPlot.getMinY(), functionPlot.getMaxY(), sizeX , sizeY);
        AxisPlot axisPlot = new AxisPlot(minX, maxX, functionPlot.getMinY(), functionPlot.getMaxY(), sizeX, sizeY, 20);

        components.add(gridPlot);
        components.add(functionPlot);
        components.add(axisPlot);
        components.add(new ContourPlot(sizeX, sizeY));
    }

    /*
    public void showAxis() {
        // Draw 1 axis
        graphics.setColor(Color.GRAY);
        int axisHeight = (int) Math.min(Math.max(0,  -minY/(maxY - minY) * this.bufferedImage.getHeight()), this.bufferedImage.getHeight() - 1);
        graphics.drawLine(0, axisHeight, this.bufferedImage.getWidth()-1, axisHeight);

        // Draw 2 axis
        int axisPos = (int) Math.min(Math.max(0,  -minX/(maxX - minX) * this.bufferedImage.getWidth()), this.bufferedImage.getWidth() - 1);
        graphics.drawLine(axisPos, 0, axisPos, this.bufferedImage.getHeight() - 1);
    }
*/

    public void compute() {
        List<Image> images = this.components.stream().map(RenderedComponent::render).collect(Collectors.toList());

        int maxX = 0;
        int maxY = 0;

        for (Image image : images) {
            if(image.getWidth(null) > maxX) maxX = image.getWidth(null);
            if(image.getHeight(null) > maxY) maxY = image.getHeight(null);
        }

        BufferedImage image =  new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setColor(Color.WHITE);

        for (int i = 0; i < images.size(); i++) {
            PositionedElement component = components.get(i);
            graphics.drawImage(images.get(i), component.getX(), component.getY(), null);
        }


        graphics.dispose();

        File file = new File("myimage.png");
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
