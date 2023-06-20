package fr.kaeios.scalar.complex;

import fr.kaeios.api.Complex;
import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;

public class ComplexImpl implements Complex {

    private final double real;
    private final double imaginary;

    public ComplexImpl(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    @Override
    public <R> R apply(UnaryOperator<R, Complex> operator) {
        return operator.compute(this);
    }

    @Override
    public <R, O> R apply(O operand, BinaryOperator<R, Complex, O> operator) {
        return operator.compute(this, operand);
    }

    @Override
    public double getImaginary() {
        return imaginary;
    }

    @Override
    public double getReal() {
        return real;
    }

}
