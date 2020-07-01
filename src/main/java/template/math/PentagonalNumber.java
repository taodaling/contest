package template.math;

import template.primitve.generated.datastructure.IntegerArrayList;

/**
 * 五边形数
 */
public class PentagonalNumber {
    public static void getPolynomial(int[] data, int n) {
        if (n == 0) {
            return;
        }
        data[0] = 1;
        for (int k = 1; k * (3 * k + 1) / 2 < n; k++) {
            int val = k % 2 == 1 ? -1 : 1;
            data[k * (3 * k + 1) / 2] += val;
        }
        for (int k = 1; k * (3 * k - 1) / 2 < n; k++) {
            int val = k % 2 == 1 ? -1 : 1;
            data[k * (3 * k - 1) / 2] += val;
        }
    }

    /**
     * \prod_{i=1}^\infin (1-x^i) \mod x^n
     */
    public static void getPolynomial(IntegerArrayList list, int n) {
        list.clear();
        list.expandWith(0, n);
        getPolynomial(list.getData(), n);
    }

    public static void getPolynomial(int[] data, int n, Modular mod) {
        if (n == 0) {
            return;
        }
        int one = mod.valueOf(1);
        int neg = mod.valueOf(-1);
        data[0] = one;
        for (int k = 1; k * (3 * k + 1) / 2 < n; k++) {
            int val = k % 2 == 1 ? neg : one;
            data[k * (3 * k + 1) / 2] += val;
        }
        for (int k = 1; k * (3 * k - 1) / 2 < n; k++) {
            int val = k % 2 == 1 ? neg : one;
            data[k * (3 * k - 1) / 2] += val;
        }
    }

    /**
     * \prod_{i=1}^\infin (1-x^i) \mod x^n
     */
    public static void getPolynomial(IntegerArrayList list, int n, Modular mod) {
        list.clear();
        list.expandWith(0, n);
        getPolynomial(list.getData(), n, mod);
    }
}
