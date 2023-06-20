package fr.kaeios.scalar.complex.operators;

import fr.kaeios.api.Complex;
import fr.kaeios.api.computation.BinaryOperator;

public class ComplexOperators {

    public static final BinaryOperator<Complex, Complex, Complex> DIV = new ComplexDivision();

}
