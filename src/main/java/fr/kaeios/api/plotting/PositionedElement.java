package fr.kaeios.api.plotting;

// TODO Refactor, this is stupid
public interface PositionedElement extends RenderedComponent {

    /**
     * Get X position of the element in the layout
     * @return
     */
    int getX();

    /**
     * Get Y position of the element in the layout
     * @return
     */
    int getY();

}
