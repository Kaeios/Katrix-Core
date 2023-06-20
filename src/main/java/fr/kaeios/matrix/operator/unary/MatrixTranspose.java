package fr.kaeios.matrix.operator.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;

public class MatrixTranspose implements UnaryOperator<Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix operand) {
        return new MatrixImpl(operand.getColumnsCount(), operand.getRowCount(), (x, y) -> operand.getValues()[y][x]);
    }

}
