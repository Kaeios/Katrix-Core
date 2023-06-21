package fr.kaeios.matrix.suppliers;

import fr.kaeios.api.matrix.CoefficientSupplier;

import java.util.Random;

public class RandomSupplier implements CoefficientSupplier {

    private static final Random RNG = new Random();

    @Override
    public Double compute(Integer row, Integer column) {
        return RNG.nextDouble() - 0.5D;
    }

}
