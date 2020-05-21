package on2020_05.on2020_05_21_Codeforces___Codeforces_Round__415__Div__1_.B__Glad_to_see_you_;



import template.io.FastInput;
import template.io.FastOutput;

public class BGladToSeeYou {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;
        this.in = in;
        int n = in.readInt();
        int k = in.readInt();

        int x = binarySearch(1, n);
        if (x > 1) {
            int y = binarySearch(1, x - 1);
            if (ask(y, x) == 0) {
                answer(x, y);
                return;
            }
        }
        int y = binarySearch(x + 1, n);
        answer(x, y);
    }

    public int binarySearch(int l, int r) {
        while (l < r) {
            int m = (l + r) / 2;
            if (ask(m, m + 1) == 0) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        return l;
    }

    FastOutput out;
    FastInput in;

    public int ask(int x, int y) {
        out.printf("1 %d %d", x, y).println().flush();
        return in.readString().equals("TAK") ? 0 : 1;
    }


    public void answer(int x, int y) {
        out.printf("2 %d %d", x, y).println().flush();
    }
}
