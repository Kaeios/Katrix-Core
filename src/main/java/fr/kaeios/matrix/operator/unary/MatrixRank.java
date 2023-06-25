package fr.kaeios.matrix.operator.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.operator.MatrixOperators;

public class MatrixRank implements UnaryOperator<Integer, Matrix> {

    @Override
    public Integer compute(Matrix operand) {

        Matrix reduced = operand.apply(MatrixOperators.LU).getTransformation()[1];

        for (int i = 0; i < reduced.getRowsCount(); i++) {
            if(Math.abs(reduced.getValues()[i][i]) < 0.00001D) return i;
        }

        return reduced.getRowsCount();
    }

}
