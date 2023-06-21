package fr.kaeios.solver;

import fr.kaeios.api.computation.Sequence;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.api.solving.SystemSolver;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.matrix.suppliers.SingleValueCoefficientSupplier;

public class SequenceSystemSolver implements SystemSolver<Sequence[]> {

    private final Matrix matrix;

    private Matrix bases;
    private final Matrix zero;

    public SequenceSystemSolver(Matrix matrix, Matrix zero) {
        this.matrix = matrix;
        this.zero = zero;
    }

    public Sequence[] solve() {
        TransformedMatrix diag = matrix.apply(MatrixOperators.DIAGONALIZE);

        this.bases = new MatrixImpl(1, matrix.getColumnsCount(), new SingleValueCoefficientSupplier(1.0D))
                .apply(diag.getTransformation()[1], MatrixOperators.MUL);

        Matrix constants = diag.getTransformation()[2].apply(zero, MatrixOperators.MUL);

        Sequence[] result = new Sequence[this.matrix.getRowCount()];

        for (int i = 0; i < this.matrix.getRowCount(); i++) {
            Double[] pRow = diag.getTransformation()[0].getValues()[i];
            result[i] = input -> {
                double sum = 0.0D;

                for (int j = 0; j < this.matrix.getRowCount(); j++) {
                    sum += pRow[j] * constants.getValues()[j][0] * Math.pow(bases.getValues()[0][j], input);
                }

                return sum;
            };
        }

        return result;
    }

}
