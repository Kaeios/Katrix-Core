package fr.kaeios.scalar.complex.operators;

import fr.kaeios.api.Complex;
import fr.kaeios.api.computation.UnaryOperator;

public class ComplexReal implements UnaryOperator<Double, Complex> {

    @Override
    public Double compute(Complex operand) {
        return operand.getReal();
    }

}
