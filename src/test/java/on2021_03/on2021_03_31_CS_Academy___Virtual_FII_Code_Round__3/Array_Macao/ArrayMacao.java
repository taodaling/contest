package on2021_03.on2021_03_31_CS_Academy___Virtual_FII_Code_Round__3.Array_Macao;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ArrayMacao {
    int parse(int x) {
        int ans = 0;
        while (x != 0) {
            ans |= 1 << x % 10;
            x /= 10;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = parse(in.ri());
        }
        for (int i = 0; i < n; i++) {
            b[i] = parse(in.ri());
        }
        int[] A = new int[n];
        int[] B = new int[n];
        int[] aNext = new int[1 << 10];
        int[] bNext = new int[1 << 10];
        Arrays.fill(aNext, n);
        Arrays.fill(bNext, n);
        for (int i = n - 1; i >= 0; i--) {
            //b first then a
            for (int j = 0; j < aNext.length; j++) {
                if ((j & b[i]) != 0 && aNext[j] < n) {
                    B[i] = Math.max(B[i], A[aNext[j]]);
                }
            }

            B[i]++;
            for (int j = 0; j < bNext.length; j++) {
                if ((j & a[i]) != 0 && bNext[j] < n) {
                    A[i] = Math.max(A[i], B[bNext[j]]);
                }
            }

            A[i]++;
            bNext[b[i]] = i;
            aNext[a[i]] = i;
        }
        int ans = Arrays.stream(A).max().orElse(-1);
        out.println(ans);
    }
}
