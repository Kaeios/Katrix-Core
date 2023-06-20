package fr.kaeios.scalar.operators;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.scalar.operators.binary.*;

public class ScalarOperations {

    public static final BinaryOperator<Double, Double, Double> ADD = new ScalarAddition();
    public static final BinaryOperator<Double, Double, Double> SUB = new ScalarSubtraction();
    public static final BinaryOperator<Double, Double, Double> MUL = new ScalarMultiplication();
    public static final BinaryOperator<Double, Double, Double> DIV = new ScalarDivision();
    public static final BinaryOperator<Double, Double, Double> POW = new ScalarPower();

}
