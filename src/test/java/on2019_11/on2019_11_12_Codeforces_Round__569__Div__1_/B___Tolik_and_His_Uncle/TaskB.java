package on2019_11.on2019_11_12_Codeforces_Round__569__Div__1_.B___Tolik_and_His_Uncle;



import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int t = n;
        int b = 1;
        int tl = 1;
        int tr = m;
        int bl = 1;
        int br = m;

        int x = 1;
        int y = 1;
        boolean left = true;

        while (t != b) {
            output(out, y, x);
            if (y == t) {
                y = b;
                if (left) {
                    x = br;
                    tl++;
                } else {
                    x = bl;
                    tr--;
                }
            } else {
                y = t;
                if (left) {
                    x = tr;
                    bl++;
                } else {
                    x = tl;
                    br--;
                }
            }

            left = !left;
            if (tl > tr) {
                t--;
                tl = 1;
                tr = m;
            }
            if (bl > br) {
                b++;
                bl = 1;
                br = m;
            }
        }

        int l = Math.max(tl, bl);
        int r = Math.min(tr, br);

        while (l <= r) {
            output(out, y, x);
            if (left) {
                x = r;
                l++;
            } else {
                x = l;
                r--;
            }
            left = !left;
        }
    }

    public void output(FastOutput out, int x, int y) {
        out.append(x).append(' ').append(y).append('\n');
    }
}
