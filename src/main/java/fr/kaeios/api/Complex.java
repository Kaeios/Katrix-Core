package fr.kaeios.api;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;

public interface Complex {

    double getImaginary();
    double getReal();

    <R> R apply(UnaryOperator<R, Complex> operator);
    <R, O> R apply(O operand, BinaryOperator<R, Complex, O> operator);

}
