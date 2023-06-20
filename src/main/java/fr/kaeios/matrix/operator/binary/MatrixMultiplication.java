package fr.kaeios.matrix.operator.binary;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.suppliers.DefaultSuppliers;

public class MatrixMultiplication implements BinaryOperator<Matrix, Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix matrix, Matrix matrix2) {
        Matrix result = new MatrixImpl(matrix.getRowCount(), matrix2.getColumnsCount(), DefaultSuppliers.NULL);

        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnsCount(); j++) {
                for(int k = 0; k < matrix2.getRowCount(); k++) {
                    result.getValues()[i][j] += matrix.getValues()[i][k] * matrix2.getValues()[k][j];
                }
            }
        }

        return result;
    }

}
