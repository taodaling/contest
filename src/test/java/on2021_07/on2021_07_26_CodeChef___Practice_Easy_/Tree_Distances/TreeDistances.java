package on2021_07.on2021_07_26_CodeChef___Practice_Easy_.Tree_Distances;



import template.io.FastInput;
import template.io.FastOutput;

public class TreeDistances {
    public boolean test(int x, int y, int a) {
        int b = y / 2 / a;
        return (long) a * a + (long) b * b == x;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.ri();
        int y = in.ri();
        if (y % 2 != 0) {
            out.println("NO");
            return;
        }
        int halfY = y / 2;
        int a = -1;
        int b = -1;
        for (int i = 1; i * i <= halfY; i++) {
            if (halfY % i != 0) {
                continue;
            }

            if (test(x, y, i)) {
                a = i;
                b = halfY / a;
                break;
            }
        }
        if (a == -1 || b == 0 && a > 1) {
            out.println("NO");
            return;
        }
        out.println("YES");
        out.println(a + b);
        int id = 2;
        for (int i = 0; i < b; i++) {
            out.append(1).append(' ').append(id++).println();
        }
        for (int i = 1; i < a; i++) {
            out.append(2).append(' ').append(id++).println();
        }
    }
}
