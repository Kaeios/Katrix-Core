package fr.kaeios;

import fr.kaeios.api.computation.Function;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.plotting.FunctionPlotter;
import fr.kaeios.solver.DifferentialSystemSolver;

public class KatrixMain {

    public static void main(String[] args) {

        Matrix A = new MatrixImpl(
                new Double[][]{
                        {-1.0D, 0.0D, 0.0D},
                        {1.0D, -2.0D, 0.0D},
                        {0.0D, 2.0D, 0.0D}
                });

        Matrix I = new MatrixImpl(
                new Double[][]{
                        {1.0D},
                        {0.0D},
                        {0.0D}
                });

        Function[] functions = new DifferentialSystemSolver(A, I).solve();

        FunctionPlotter plot = new FunctionPlotter(functions, 0, 5, 1500, 1000);

        plot.plot();
        plot.showGrid();
        plot.save();

    }

    static void printMatrix(Matrix m) {
        for (int row = 0; row < m.getRowCount(); row++) {
            for (int col = 0; col < m.getColumnsCount(); col++) {
                System.out.printf("%+.4f\t", m.getValues()[row][col]);
            }
            System.out.print("\n");
        }
    }

}
