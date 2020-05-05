package contest;

import template.math.ExtLucas;
import template.math.Factorial;
import template.math.Modular;

public class SmoothPermutations {
    public long countPermutations(int T, int M, int MX, int seed, int[] prefN, int[] prefK, int[] prefX) {
        long[] A = new long[T * 3];
        A[0] = seed;
        for (int i = 1; i <= 3 * T - 1; i++) {
            A[i] = (A[i - 1] * 1103515245 + 12345) % 2147483648L;
        }

        int LEN = prefN.length;

        int[] N = new int[T];
        int[] K = new int[T];
        int[] X = new int[T];
        for (int i = 0; i <= LEN - 1; i++) {
            N[i] = prefN[i];
            K[i] = prefK[i];
            X[i] = prefX[i];
        }
        for (int i = LEN; i <= T - 1; i++) {
            N[i] = (int) ((A[i] % MX) + 1);
            K[i] = (int) ((A[T + i] % N[i]) + 1);
            X[i] = (int) ((A[2 * T + i] % N[i]) + 1);
        }

        Modular mod = new Modular(M);
        ExtLucas lucas = new ExtLucas(M, MX);
        Factorial fact = new Factorial(MX, mod);

        long sum = 0;
        for (int i = 0; i < T; i++) {
            int n = N[i];
            int k = K[i];
            int x = X[i];

            int ans = lucas.combination(x, k);
            ans = mod.mul(ans, fact.fact(k));
            ans = mod.mul(ans, fact.fact(n - k));

            sum += ans;
        }
        return sum;
    }
}
