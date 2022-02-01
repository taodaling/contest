package template.math;

import template.polynomial.IntPoly;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerIterator;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

import java.util.Arrays;

/**
 * https://codeforces.com/blog/entry/96199?#comment-852652
 */
public class KthTermOfLinearRecurrence {
    int[] Q;
    int[] P;
    IntPoly poly;
    int mod;

    public KthTermOfLinearRecurrence(int[] lr, int[] prefix, int mod, IntPoly poly) {
        this.poly = poly;
        this.mod = mod;

        int rank = Polynomials.rankOf(lr);
        Q = Arrays.copyOf(lr, rank + 1);
        int[] F = Arrays.copyOf(prefix, rank);
        P = poly.module(poly.convolution(Q, F), rank);
    }

    public int kth(long n) {
        return rec(PrimitiveBuffers.allocIntPow2(P), PrimitiveBuffers.allocIntPow2(Q), n);
    }

    int rec(int[] P, int[] Q, long n) {
        if (n == 0) {
            int ans =  (int) (P[0] * DigitUtils.modInverse(Q[0], mod) % mod);
            PrimitiveBuffers.release(P, Q);
            return ans;
        }
        int bit = (int) (n & 1);
        int[] negQ = PrimitiveBuffers.allocIntPow2(Q);
        for(int i = 1; i < negQ.length; i += 2) {
            negQ[i] = DigitUtils.negate(negQ[i], mod);
        }
        int[] AB = poly.convolution(P, negQ);
        int[] QQ = poly.convolution(Q, negQ);
        PrimitiveBuffers.release(P, Q, negQ);
        int[] A = PrimitiveBuffers.allocIntPow2(AB.length / 2);
        for(int i = bit ; i < AB.length; i += 2){
            A[i >> 1] = AB[i];
        }
        PrimitiveBuffers.release(AB);
        int[] C = PrimitiveBuffers.allocIntPow2(QQ.length / 2);
        for(int i = 0; i < QQ.length; i += 2) {
            C[i >> 1] = QQ[i];
        }
        PrimitiveBuffers.release(QQ);
        int ans = rec(A, C, n >>> 1);
        return ans;
    }
}
