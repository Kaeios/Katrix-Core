package fr.kaeios.matrix.operator.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.operator.MatrixOperators;

public class MatrixConditionNumber implements UnaryOperator<Double, Matrix> {

    @Override
    public Double compute(Matrix operand) {
        return
                operand.apply(MatrixOperators.L2_NORM)
                        * operand.apply(MatrixOperators.INV).apply(MatrixOperators.L2_NORM);
    }

}
