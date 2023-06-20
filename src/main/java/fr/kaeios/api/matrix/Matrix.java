package fr.kaeios.api.matrix;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;

public interface Matrix {

    int getRowCount();
    int getColumnsCount();

    Double[][] getValues();

    Matrix dotApply(Matrix other, BinaryOperator<Double, Double, Double> operator);
    Matrix dotApply(UnaryOperator<Double, Double> operator);

    <R> R apply(UnaryOperator<R, Matrix> operator);
    <R, O> R apply(O other, BinaryOperator<R, Matrix, O> operator);

}
