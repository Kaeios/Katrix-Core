package fr.kaeios.scalar.complex.operators;

import fr.kaeios.api.Complex;
import fr.kaeios.api.computation.UnaryOperator;

public class ComplexNorm implements UnaryOperator<Double, Complex> {

    @Override
    public Double compute(Complex operand) {
        return Math.sqrt(operand.getImaginary() * operand.getImaginary() + operand.getReal() * operand.getReal());
    }

}
