package fr.kaeios.matrix.operator;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.operator.binary.MatrixAddition;
import fr.kaeios.matrix.operator.binary.MatrixMultiplication;
import fr.kaeios.matrix.operator.binary.MatrixScalarMultiplication;
import fr.kaeios.matrix.operator.transformers.*;
import fr.kaeios.matrix.operator.unary.MatrixInverse;
import fr.kaeios.matrix.operator.unary.MatrixTranspose;
import fr.kaeios.matrix.operator.unary.*;

public class MatrixOperators {

    /*
    Binary operators
     */
    public static final BinaryOperator<Matrix, Matrix, Matrix> ADD = new MatrixAddition();
    public static final BinaryOperator<Matrix, Matrix, Matrix> SUB = new MatrixAddition();
    public static final BinaryOperator<Matrix, Matrix, Matrix> MUL = new MatrixMultiplication();
    public static final BinaryOperator<Matrix, Matrix, Double> SCALAR_MUL = new MatrixScalarMultiplication();

    /*
    Unary operators
     */
    public static final UnaryOperator<Matrix, Matrix> INV = new MatrixInverse();
    public static final UnaryOperator<Matrix, Matrix> TRANSPOSE = new MatrixTranspose();
    public static final UnaryOperator<Double, Matrix> DET = new MatrixDeterminant();
    public static final UnaryOperator<Double, Matrix> TRACE = new MatrixTrace();
    public static final UnaryOperator<Double, Matrix> L1_NORM = new MatrixNormL1();
    public static final UnaryOperator<Double, Matrix> L2_NORM = new MatrixNormL2();
    public static final UnaryOperator<Double, Matrix> COND_NUMBER = new MatrixNormL2();
    public static final UnaryOperator<Boolean, Matrix> CHECK_SYM = new MatrixCheckSymmetry();

    /*
    Transformation operations
     */
    public static final UnaryOperator<TransformedMatrix, Matrix> TRIDIAGONALIZE = new MatrixTridiagonalize();
    public static final UnaryOperator<TransformedMatrix, Matrix> DIAGONALIZE = new MatrixDiagonalize();
    public static final UnaryOperator<TransformedMatrix, Matrix> HESSENBERG = new MatrixHessenbergTransform();
    public static final UnaryOperator<TransformedMatrix, Matrix> SCHUR = new MatrixSchurTransformation();
    public static final UnaryOperator<TransformedMatrix, Matrix> QR = new MatrixQRDecomposition();
    public static final UnaryOperator<TransformedMatrix, Matrix> LU = new MatrixLUDecomposition();
    public static final UnaryOperator<TransformedMatrix, Matrix> ROW_ECHELON = new MatrixRowEchelonDecomposition();

}
