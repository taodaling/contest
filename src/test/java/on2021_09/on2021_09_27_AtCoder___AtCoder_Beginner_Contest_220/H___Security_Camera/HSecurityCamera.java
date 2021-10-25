package on2021_09.on2021_09_27_AtCoder___AtCoder_Beginner_Contest_220.H___Security_Camera;



import template.binary.Bits;
import template.binary.FastBitCount2;
import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;
import template.utils.Debug;
import template.utils.ToStringUtils;

public class HSecurityCamera {


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[] adj = new long[n];
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            adj[a] |= 1L << b;
            adj[b] |= 1L << a;
        }
        int lHalf = n / 2;
        int rHalf = n - lHalf;

        long[] A = new long[1 << lHalf + 2];
        for (int i = 0; i < 1 << rHalf; i++) {
            long e = 0;
            long xor = 0;
            for (int j = 0; j < rHalf; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                xor ^= adj[j + lHalf];
                long mask = (1L << j) - 1 & i;
                mask <<= lHalf;
                e ^= adj[j + lHalf] & mask;
            }
            e ^= xor;
            int mask = (FastBitCount2.count(e) & 1) << lHalf;
            mask |= 1 << lHalf + 1;
            mask |= xor & (1L << lHalf) - 1;
            A[mask]++;
        }


        long[] B = new long[1 << lHalf + 2];
        for (int i = 0; i < 1 << lHalf; i++) {
            long e = 0;
            long xor = 0;
            for (int j = 0; j < lHalf; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                xor ^= adj[j];
                long mask = (1L << j) - 1 & i;
                e ^= adj[j] & mask;
            }
            e ^= xor;
            int mask = (FastBitCount2.count(e) & 1) << lHalf + 1;
            mask |= 1 << lHalf;
            mask |= i;
            B[mask]++;
        }

        if (debug.enable()) {
            debug.debug("A", "A");
            for (int i = 0; i < A.length; i++) {
                debug.debug(ToStringUtils.toBinaryString(i, lHalf + 2), A[i]);
            }

            debug.debug("B", "B");
            for (int i = 0; i < B.length; i++) {
                debug.debug(ToStringUtils.toBinaryString(i, lHalf + 2), B[i]);
            }
        }

        long[] C = new long[1 << lHalf + 2];
        FastWalshHadamardTransform.andFWT(A, 0, A.length - 1);
        FastWalshHadamardTransform.andFWT(B, 0, A.length - 1);
        FastWalshHadamardTransform.dotMul(A, B, C, 0, A.length - 1);
        FastWalshHadamardTransform.andIFWT(C, 0, C.length - 1);

        if (debug.enable()) {
            debug.debug("C", "C");
            for (int i = 0; i < B.length; i++) {
                debug.debug(ToStringUtils.toBinaryString(i, lHalf + 2), C[i]);
            }
        }
        long ans = 0;
        for (int i = 0; i < C.length; i++) {
            if (FastBitCount2.count(i) % 2 == 0) {
                ans += C[i];
            }
        }
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
