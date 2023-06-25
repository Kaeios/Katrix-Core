package fr.kaeios.matrix.operator.transformers;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.suppliers.DefaultSuppliers;

public class MatrixLUDecomposition implements UnaryOperator<TransformedMatrix, Matrix> {
    @Override
    public TransformedMatrix compute(Matrix operand) {

        int size = operand.getRowsCount();

        Matrix lower = new MatrixImpl(size, size, DefaultSuppliers.IDENTITY);
        Matrix upper = new MatrixImpl(size, size, DefaultSuppliers.NULL);

        // Decomposing matrix into Upper and Lower
        // triangular matrix
        for (int i = 0; i < size; i++) {
            // Upper Triangular
            for (int k = i; k < size; k++) {
                // Summation of L(i, j) * U(j, k)
                int sum = 0;
                for (int j = 0; j < i; j++)
                    sum += (lower.getValues()[i][j] * upper.getValues()[j][k]);

                // Evaluating U(i, k)
                upper.getValues()[i][k] = operand.getValues()[i][k] - sum;
            }

            // Lower Triangular
            for (int k = i + 1; k < size; k++)
            {
                // Summation of L(k, j) * U(j, i)
                int sum = 0;
                for (int j = 0; j < i; j++)
                    sum += (lower.getValues()[k][j] * upper.getValues()[j][i]);

                // Evaluating L(k, i)
                lower.getValues()[k][i]
                        = (operand.getValues()[k][i] - sum) / upper.getValues()[i][i];
            }
        }

        return () -> new Matrix[]{lower, upper};
    }

}
