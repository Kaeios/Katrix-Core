package fr.kaeios.scalar.complex.operators;

import fr.kaeios.api.Complex;
import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.scalar.complex.ComplexImpl;

public class ComplexAddition implements BinaryOperator<Complex, Complex, Complex> {

    @Override
    public Complex compute(Complex complex, Complex complex2) {
        return new ComplexImpl(complex.getReal() + complex2.getReal(), complex.getImaginary() + complex2.getImaginary());
    }

}
