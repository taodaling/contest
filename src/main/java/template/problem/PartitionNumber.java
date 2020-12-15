package template.problem;

import template.math.PentagonalNumber;
import template.polynomial.IntPoly;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

public class PartitionNumber {
    int[] way;

    public PartitionNumber(int n, int mod, IntPoly poly) {
        int[] f = PrimitiveBuffers.allocIntPow2(n + 1);
        PentagonalNumber.getPolynomial(f, n + 1, mod);
        int[] inv = PrimitiveBuffers.replace(poly.inverse(f, n + 1), f);
        way = PrimitiveBuffers.replace(Arrays.copyOf(inv, n + 1), inv);
    }

    public int query(int n) {
        return way[n];
    }
}
