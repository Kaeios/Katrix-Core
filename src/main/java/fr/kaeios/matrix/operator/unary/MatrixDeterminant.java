package fr.kaeios.matrix.operator.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.matrix.suppliers.DefaultSuppliers;

public class MatrixDeterminant implements UnaryOperator<Double, Matrix> {

    @Override
    public Double compute(Matrix operand) {

        Matrix reduced = operand.apply(MatrixOperators.LU).getTransformation()[2] ;

        double det = 1.0D;

        for (int i = 0; i < reduced.getRowsCount(); i++) {
            det *= reduced.getValues()[i][i];
        }

        return det;
    }

}
