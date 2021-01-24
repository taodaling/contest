package template.math;

public interface IntCombination {
    int combination(int m, int n);

    public static IntCombination combinationModPrime(int mod, int max) {
        if (max < mod) {
            return new Combination(max, mod);
        } else {
            return new Lucas(new Combination(mod - 1, mod), mod);
        }
    }
}
