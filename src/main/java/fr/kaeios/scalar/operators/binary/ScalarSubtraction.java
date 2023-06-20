package fr.kaeios.scalar.operators.binary;

import fr.kaeios.api.computation.BinaryOperator;

public class ScalarSubtraction implements BinaryOperator<Double, Double, Double> {

    @Override
    public Double compute(Double x, Double y) {
        return x - y;
    }

}
