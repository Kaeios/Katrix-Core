package fr.kaeios.api.matrix;

public interface TransformedMatrix {

    /**
     * Get an array of Matrix containing components of the decomposition
     *
     * @return a Matrix array with elements of the decomposition
     */
    Matrix[] getTransformation();

}
