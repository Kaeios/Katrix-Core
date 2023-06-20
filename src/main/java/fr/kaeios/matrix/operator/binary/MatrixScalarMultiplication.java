package fr.kaeios.matrix.operator.binary;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.scalar.operators.ScalarOperations;

public class MatrixScalarMultiplication implements BinaryOperator<Matrix, Matrix, Double> {

    @Override
    public Matrix compute(Matrix matrix, Double scalar) {
        return matrix.dotApply(x -> ScalarOperations.MUL.compute(x, scalar));
    }

}
