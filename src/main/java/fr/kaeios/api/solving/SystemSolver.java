package fr.kaeios.api.solving;

public interface SystemSolver<T> {

    /**
     * Compute solution of the system
     *
     * @return solution of the system
     */
    T solve();

}
