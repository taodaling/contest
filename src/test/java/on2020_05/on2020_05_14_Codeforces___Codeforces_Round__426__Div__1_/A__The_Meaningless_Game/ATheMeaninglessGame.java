package on2020_05.on2020_05_14_Codeforces___Codeforces_Round__426__Div__1_.A__The_Meaningless_Game;



import com.sun.org.apache.bcel.internal.generic.FADD;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

public class ATheMeaninglessGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        String yes = "Yes";
        String no = "No";
        IntegerMultiWayStack stack = Factorization.factorizeRangePrime((int) 1e6);
        for (int i = 0; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();

            int m = find((long) a * b);
            if (m == -1) {
                out.println(no);
                continue;
            }

            boolean valid = true;
            for (IntegerIterator iterator = stack.iterator(m); iterator.hasNext() && valid; ) {
                int p = iterator.next();
                int c = log(p, a);
                int d = log(p, b);

                if (2 * c - d < 0 || (2 * c - d) % 3 != 0) {
                    valid = false;
                }
                if(2 * d - c < 0 || (2 * d - c) % 3 != 0){
                    valid = false;
                }
            }

            out.println(valid ? yes : no);
        }
    }

    public int log(int x, int y) {
        int ans = 0;
        while (y % x == 0) {
            ans++;
            y /= x;
        }
        return ans;
    }

    public int find(long n) {
        int l = 1;
        int r = (int) 1e6;
        while (l < r) {
            long m = (l + r + 1) >> 1;
            if (m * m * m <= n) {
                l = (int) m;
            } else {
                r = (int) (m - 1);
            }
        }
        return (long)l * l * l == n ? l : -1;
    }
}
