package fr.kaeios.matrix.operator.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.suppliers.DefaultSuppliers;

public class MatrixDeterminant implements UnaryOperator<Double, Matrix> {

    @Override
    public Double compute(Matrix operand) {
        double det = 0.0D;

        int size = operand.getRowsCount();

        if(size == 1) return operand.getValues()[0][0];

        Matrix minor = new MatrixImpl(size - 1, size - 1, DefaultSuppliers.NULL);

        for(int column = 0; column < size; column++) {

            int pos = 0;

            for(int i = 1; i < size; i++) {
                for(int j = 0; j < size; j++) {
                    if(j == column) continue;

                    minor.getValues()[pos % (size-1)][pos / (size - 1)] = operand.getValues()[i][j];

                    pos++;
                }
            }

            // Sign of the factor
            int sign = (column & 1) == 0 ? 1 : -1;

            det += (sign * operand.getValues()[0][column] * compute(minor));
        }

        return det;
    }

}
