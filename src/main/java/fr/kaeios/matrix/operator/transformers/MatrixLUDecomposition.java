package fr.kaeios.matrix.operator.transformers;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.matrix.suppliers.DefaultSuppliers;

public class MatrixLUDecomposition implements UnaryOperator<TransformedMatrix, Matrix> {
    @Override
    public TransformedMatrix compute(Matrix operand) {

        int size = operand.getRowsCount();

        Matrix lower = new MatrixImpl(size, size, DefaultSuppliers.IDENTITY);
        Matrix upper = new MatrixImpl(operand.getValues());

        Matrix permutations = new MatrixImpl(size, size, DefaultSuppliers.IDENTITY);

        for (int i = 0; i < size; i++) {

            if(Math.abs(upper.getValues()[i][i]) < 0.00001D) {
                for(int row = i + 1; row < size; row++) {
                    if(Math.abs(upper.getValues()[row][row]) < 0.00001D) continue;

                    Matrix P = new MatrixImpl(size, size, DefaultSuppliers.IDENTITY);

                    P.getValues()[i][i] = 0.0D;
                    P.getValues()[row][row] = 0.0D;

                    P.getValues()[i][row] = 1.0D;
                    P.getValues()[row][i] = 1.0D;

                    upper = P.apply(upper, MatrixOperators.MUL);
                    permutations = P.apply(permutations, MatrixOperators.MUL);

                    break;
                }
            }

            for (int row = i + 1; row < size; row++) {

                double ratio = (upper.getValues()[row][i] / upper.getValues()[i][i]);

                for (int col = row - 1; col < size; col++) {
                    upper.getValues()[row][col] = upper.getValues()[row][col] - (ratio * upper.getValues()[i][col]);
                    lower.getValues()[row][col] = lower.getValues()[row][col] + (ratio * lower.getValues()[i][col]);
                }

            }
        }

        Matrix[] result = new Matrix[]{ permutations, lower, upper };

        return () -> result;
    }

}
