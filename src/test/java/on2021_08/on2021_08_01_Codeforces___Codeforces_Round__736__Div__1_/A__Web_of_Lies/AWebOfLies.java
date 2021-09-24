package on2021_08.on2021_08_01_Codeforces___Codeforces_Round__736__Div__1_.A__Web_of_Lies;



import template.io.FastInput;
import template.io.FastOutput;

public class AWebOfLies {
    int[] over;
    int del = 0;

    public void add(int i, int j, int v) {
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        if (over[i] > 0) {
            del--;
        }
        over[i] += v;
        if (over[i] > 0) {
            del++;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        over = new int[n];
        for (int i = 0; i < m; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            add(u, v, 1);
        }
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1 || t == 2) {
                int u = in.ri() - 1;
                int v = in.ri() - 1;
                if (t == 1) {
                    add(u, v, 1);
                } else {
                    add(u, v, -1);
                }
            } else {
                out.println(n - del);
            }
        }
    }
}
