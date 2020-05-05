package contest;

import template.primitve.generated.datastructure.LongPreSum;
import template.rand.Randomized;

import java.util.Arrays;

public class DeleteArrays {
    public long[] doDelete(int a, int b, int c, long x, long y, long z) {
        long[] A = new long[a];
        long[] B = new long[b];
        long[] C = new long[c];
        A[0] = 33;
        A[1] = 42;
        for (int i = 2; i <= a - 1; i++) {
            A[i] = (5 * A[i - 1] + 7 * A[i - 2]) % 1000000007 + 1;
        }
        B[0] = 13;
        for (int i = 1; i <= b - 1; i++) {
            B[i] = (11 * B[i - 1]) % 1000000007 + 1;
        }
        C[0] = 7;
        C[1] = 2;
        for (int i = 2; i <= c - 1; i++) {
            C[i] = (5 * C[i - 1] + 7 * C[i - 2]) % 1000000007 + 1;
        }

        Randomized.shuffle(A);
        Randomized.shuffle(B);
        Randomized.shuffle(C);
        Arrays.sort(A);
        Arrays.sort(B);
        Arrays.sort(C);
        LongPreSum psA = new LongPreSum(A);
        LongPreSum psB = new LongPreSum(B);
        LongPreSum psC = new LongPreSum(C);

        long sum = psA.post(0) + psB.post(0) + psC.post(0);
        if (a >= b + c) {
            long subtract = psA.prefix(a - (b + c) - 1);
            return new long[]{subtract, sum - subtract + x * b + z * c};
        }
        if (b >= a + c) {
            long subtract = psB.prefix(b - (a + c) - 1);
            return new long[]{subtract, sum - subtract + x * a + y * c};
        }
        if (c >= a + b) {
            long subtract = psC.prefix(c - (a + b - 1));
            return new long[]{subtract, sum - subtract + z * a + y * b};
        }

        if ((a + b + c) % 2 == 0) {
            return new long[]{0, sum + solve(a, b, c, x, y, z)};
        }

        long min = Math.min(Math.min(A[0], B[0]), C[0]);
        long cost = (long) 1e18;
        //odd
        if (psA.prefix(0) == min) {
            cost = Math.min(cost, solve(a - 1, b, c, x, y, z));
        }
        if (psB.prefix(0) == min) {
            cost = Math.min(cost, solve(a, b - 1, c, x, y, z));
        }
        if (psC.prefix(0) == min) {
            cost = Math.min(cost, solve(a, b, c - 1, x, y, z));
        }

        return new long[]{min, sum - min + cost};
    }

    public long solve(int a, int b, int c, long x, long y, long z) {
        long i = (a + b - c) / 2;
        long k = a - i;
        long j = b - i;
        return i * x + j * y + k * z;
    }
}
