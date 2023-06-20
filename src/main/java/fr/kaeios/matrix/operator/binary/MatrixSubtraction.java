package fr.kaeios.matrix.operator.binary;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.scalar.operators.ScalarOperations;

public class MatrixSubtraction implements BinaryOperator<Matrix, Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix matrix, Matrix matrix2) {
        return matrix.dotApply(matrix2, ScalarOperations.SUB);
    }

}
