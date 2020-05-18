package on2020_05.on2020_05_18_TopCoder_SRM__786.SwapTheString;



import template.primitve.generated.datastructure.IntegerBIT;

import java.util.Arrays;

public class SwapTheString {
    public long findNumberOfSwaps(String P, int A0, int X, int Y, int N, int K) {
        int[] A = new int[N];
        A[0] = A0;
        for (int i = 1; i <= N - 1; i++) {
            A[i] = (int) (((long) A[i - 1] * X + Y) % 1812447359);
        }
        char[] S = Arrays.copyOf(P.toCharArray(), N);
        for (int i = P.length(); i <= N - 1; i++) {
            S[i] = (char) (A[i] % 26 + 'a');
        }

        IntegerBIT bit = new IntegerBIT(128);

        long ans = 0;
        for (int i = 0; i < K; i++) {
            for (int j = i; j < N; j += K) {
                ans += bit.query(S[j] - 1);
                bit.update(S[j], 1);
            }
            for (int j = i; j < N; j += K) {
                bit.update(S[j], -1);
            }
        }

        return ans;
    }
}
