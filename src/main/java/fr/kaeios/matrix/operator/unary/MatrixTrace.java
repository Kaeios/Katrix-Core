package fr.kaeios.matrix.operator.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class MatrixTrace implements UnaryOperator<Double, Matrix> {

    @Override
    public Double compute(Matrix operand) {
        double sum = 0.0D;

        for (int n = 0; n < operand.getRowCount(); n++) {
            sum += operand.getValues()[n][n];
        }

        return sum;
    }

}
