package fr.kaeios.api;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;

public interface Complex {

    /**
     * Get imaginary part of the number
     *
     * @return imaginary value
     */
    double getImaginary();

    /**
     * Get real part of the number
     * @return real value
     */
    double getReal();

    /**
     * Apply an operation to this number
     *
     * @param operator operation to apply
     *
     * @return result of the operation
     *
     * @param <R> type of the return value
     */
    <R> R apply(UnaryOperator<R, Complex> operator);

    /**
     * Apply an operation between this number and another
     *
     * @param operand right operand
     * @param operator operation to apply
     *
     * @return result of the operation
     *
     * @param <R> type of the return value
     * @param <O> type of the right operand
     */
    <R, O> R apply(O operand, BinaryOperator<R, Complex, O> operator);

}
