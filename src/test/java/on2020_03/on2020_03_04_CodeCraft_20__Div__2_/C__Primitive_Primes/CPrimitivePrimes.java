package on2020_03.on2020_03_04_CodeCraft_20__Div__2_.C__Primitive_Primes;



import template.io.FastInput;
import template.io.FastOutput;

public class CPrimitivePrimes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int p = in.readInt();
        int[] a = new int[n];
        int aI = -1;
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            if (a[i] % p != 0 && aI == -1) {
                aI = i;
            }
        }
        int[] b = new int[m];
        int bI = -1;
        for (int i = 0; i < m; i++) {
            b[i] = in.readInt();
            if (b[i] % p != 0 && bI == -1) {
                bI = i;
            }
        }

        out.println(aI + bI);
    }
}
