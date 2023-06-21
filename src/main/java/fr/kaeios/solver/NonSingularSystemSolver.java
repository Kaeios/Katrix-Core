package fr.kaeios.solver;

import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.solving.SystemSolver;
import fr.kaeios.matrix.operator.MatrixOperators;

public class NonSingularSystemSolver implements SystemSolver<Matrix> {

    private final Matrix matrix;
    private final Matrix zero;

    public NonSingularSystemSolver(Matrix matrix, Matrix zero) {
        this.matrix = matrix;
        this.zero = zero;
    }

    @Override
    public Matrix solve() {
        return matrix.apply(MatrixOperators.INV).apply(zero, MatrixOperators.MUL);
    }

}
