package fr.kaeios;

import fr.kaeios.api.computation.Function;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.matrix.operator.transformers.MatrixRowEchelonDecomposition;
import fr.kaeios.plotter.FunctionPlotter;
import fr.kaeios.solver.DifferentialSystemSolver;

public class KatrixMain {

    /*
        Sample to solve differential system of equation

        x(t) = -x'(t)
        y(t) = x'(t) - 2 * y'(t)
        z(t) = 2 * y'(t)

        And

        x(0) = 1
        y(0) = 0
        z(0) = 0

     */

    public static void main(String[] args) {

        // Coefficient of the system
        Matrix A = new MatrixImpl(
                new Double[][]{
                        {-1.0D, 0.0D, 0.0D},
                        {1.0D, -2.0D, 0.0D},
                        {0.0D, 2.0D, 0.0D},
                });

        // Initial conditions
        Matrix B = new MatrixImpl(
                new Double[][] {
                        {1.0D},
                        {0.0D},
                        {0.0D}
                }
        );

        // Solve system
        DifferentialSystemSolver solver = new DifferentialSystemSolver(A, B);
        Function[] solutions = solver.solve();

        // Plot system on a png named diff_solutions.png
        FunctionPlotter plotter = new FunctionPlotter(solutions, 0, 2, 1920, 1080);
        plotter.compute("diff_solutions");
    }

    public static void printMatrix(Matrix m) {
        System.out.println("============");
        for (int row = 0; row < m.getRowsCount(); row++) {
            for (int col = 0; col < m.getColumnsCount(); col++) {
                System.out.printf("%+.4f\t", m.getValues()[row][col]);
            }
            System.out.print("\n");
        }
    }

}
