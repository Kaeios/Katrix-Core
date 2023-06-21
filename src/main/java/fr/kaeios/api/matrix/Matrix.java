package fr.kaeios.api.matrix;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;

public interface Matrix {

    /**
     * Get row dimension of the matrix
     * @return number of rows
     */
    int getRowsCount();

    /**
     * Get column dimension of the matrix
     * @return number of columns
     */
    int getColumnsCount();

    /**
     * Get Matrix ad a 2D array
     * @return a Double array corresponding to matrix
     */
    Double[][] getValues();

    /**
     * Apply a function between two matrix term by term
     *
     * @param other right operand of the calculus
     * @param operator operation to apply
     *
     * @return result of the computation as a new Matrix
     */
    Matrix dotApply(Matrix other, BinaryOperator<Double, Double, Double> operator);

    /**
     * Apply a function to each coefficient of a matrix
     *
     * @param operator operation to apply
     *
     * @return result of the computation as a new matrix
     */
    Matrix dotApply(UnaryOperator<Double, Double> operator);

    /**
     * Apply an operation to the matrix
     *
     * @param operator operation to apply
     *
     * @return Result of the calculus with type R
     *
     * @param <R> Type of the return value
     */
    <R> R apply(UnaryOperator<R, Matrix> operator);

    /**
     * Apply an operation between the matrix and a right operand
     *
     * @param other right operand of the calculus
     * @param operator operation to apply between operands
     *
     * @return result of the calculus
     *
     * @param <R> type of the return value
     * @param <O> type of the right operand
     */
    <R, O> R apply(O other, BinaryOperator<R, Matrix, O> operator);

}
