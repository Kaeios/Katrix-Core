package fr.kaeios.matrix.operator.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class MatrixCheckSymmetry implements UnaryOperator<Boolean, Matrix> {

    @Override
    public Boolean compute(Matrix operand) {

        int size = operand.getRowsCount();
        double relativeTolerance = 10 * size * size * Double.longBitsToDouble((1023L - 53L) << 52);

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {

                final double coeffA = operand.getValues()[i][j];
                final double coeffB = operand.getValues()[j][i];

                if (Math.abs(coeffA - coeffB) > Math.max(Math.abs(coeffA), Math.abs(coeffB)) * relativeTolerance)
                    return false;
            }
        }

        return true;
    }


}
