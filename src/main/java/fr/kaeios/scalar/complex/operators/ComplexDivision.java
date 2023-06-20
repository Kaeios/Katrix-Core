package fr.kaeios.scalar.complex.operators;

import fr.kaeios.api.Complex;
import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.scalar.complex.ComplexImpl;

public class ComplexDivision implements BinaryOperator<Complex, Complex, Complex> {

    @Override
    public Complex compute(Complex complex, Complex complex2) {

        double divider = (complex2.getImaginary() * complex2.getImaginary() + complex2.getReal() * complex2.getReal());

        double realNumerator = (complex.getReal() * complex2.getReal()) + (complex.getImaginary() * complex2.getImaginary());

        double imaginaryNumerator = complex.getImaginary() * complex2.getReal() - complex.getReal() * complex2.getImaginary();

        return new ComplexImpl(realNumerator/divider, imaginaryNumerator/divider);
    }

}
