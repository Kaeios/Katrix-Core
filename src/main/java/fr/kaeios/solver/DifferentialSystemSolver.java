package fr.kaeios.solver;

import fr.kaeios.api.computation.Function;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.api.solving.SystemSolver;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.matrix.suppliers.SingleValueCoefficientSupplier;

public class DifferentialSystemSolver implements SystemSolver<Function[]> {

    private final Matrix matrix;

    private Matrix exponents;
    private final Matrix zero;

    public DifferentialSystemSolver(Matrix matrix, Matrix zero) {
        this.matrix = matrix;
        this.zero = zero;
    }

    public Function[] solve() {
        TransformedMatrix diag = matrix.apply(MatrixOperators.DIAGONALIZE);

        this.exponents = new MatrixImpl(1, matrix.getColumnsCount(), new SingleValueCoefficientSupplier(1.0D))
                .apply(diag.getTransformation()[1], MatrixOperators.MUL);

        Matrix constants = diag.getTransformation()[2].apply(zero, MatrixOperators.MUL);

        Function[] result = new Function[this.matrix.getRowsCount()];

        for (int i = 0; i < this.matrix.getRowsCount(); i++) {
            Double[] pRow = diag.getTransformation()[0].getValues()[i];
            result[i] = input -> {
                double sum = 0.0D;

                for (int j = 0; j < this.matrix.getRowsCount(); j++) {
                    sum += pRow[j] * constants.getValues()[j][0] * Math.exp(exponents.getValues()[0][j] * input);
                }

                return sum;
            };
        }

        return result;
    }

}
