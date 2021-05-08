package on2021_05.on2021_05_05_Codeforces___Codeforces_Global_Round_14.B__Phoenix_and_Puzzle;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class BPhoenixAndPuzzle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.ri();
        while (x % 2 == 0) {
            if (possible(x)) {
                out.println("YES");
                return;
            }
            x /= 2;
        }
        out.println("NO");
    }

    public boolean possible(int n) {
        if (n % 2 != 0) {
            return false;
        }
        n /= 2;
        int sqrt = IntMath.floorSqrt(n);
        return sqrt * sqrt == n;
    }
}
