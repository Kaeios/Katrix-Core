package fr.kaeios.api.matrix;

import fr.kaeios.api.computation.BinaryOperator;

public interface CoefficientSupplier extends BinaryOperator<Double, Integer, Integer> {

    /**
     * Define coefficient value at x, y
     *
     * @param row row index of the coefficient to set
     * @param column column index of the coefficient to set
     *
     * @return the value of the coefficient at x, y
     */
    @Override
    Double compute(Integer row, Integer column);

}
