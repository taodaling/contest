package on2021_03.on2021_03_23_Codeforces___Codeforces_Round__239__Div__1_.A__Triangle;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class ATriangle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        int[] sqrt = new int[(int) 1e6 + 1];
        for (int i = 0; i < sqrt.length; i++) {
            sqrt[i] = IntMath.floorSqrt(i);
            if (sqrt[i] * sqrt[i] != i) {
                sqrt[i] = -1;
            }
        }
        for (int x = 1; x < a; x++) {
            int z = sqrt[a * a - x * x];
            if (z < 0) {
                continue;
            }
            if (x * b % a != 0) {
                continue;
            }
            if (z * b % a != 0) {
                continue;
            }
            int t = x * b / a;
            int y = z * b / a;
            if (x == y) {
                continue;
            }
            out.println("YES");
            out.printf("%d %d", x, 0).println();
            out.printf("%d %d", 0, z).println();
            out.printf("%d %d", y, z + t).println();
            return;
        }
        out.println("NO");
    }
}
