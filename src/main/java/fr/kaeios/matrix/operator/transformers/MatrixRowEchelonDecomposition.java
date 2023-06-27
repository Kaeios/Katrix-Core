package fr.kaeios.matrix.operator.transformers;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.operator.MatrixOperators;

public class MatrixRowEchelonDecomposition implements UnaryOperator<TransformedMatrix, Matrix> {

    @Override
    public TransformedMatrix compute(Matrix operand) {

        TransformedMatrix LU = operand.apply(MatrixOperators.LU);

        Matrix L = LU.getTransformation()[1];
        Matrix U = LU.getTransformation()[2];

        for (int i = 0; i < U.getRowsCount(); i++) {
            double coeff = U.getValues()[i][i];
            if(Math.abs(coeff) < 0.00001D) break;


            for (int k = i; k < U.getRowsCount(); k++) {
                U.getValues()[i][k] = U.getValues()[i][k] / coeff;
                L.getValues()[k][i] = L.getValues()[k][i] * coeff;
            }
        }

        return () -> new Matrix[] { LU.getTransformation()[0], L, U };
    }

}
