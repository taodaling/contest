package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.TreeSet;

public class AGlassCarving {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int w = in.ri();
        int h = in.ri();
        int n = in.ri();
        int[][] cut = new int[n][2];
        Largest horizontal = new Largest();
        Largest vertical = new Largest();
        horizontal.add(0);
        horizontal.add(w);
        vertical.add(0);
        vertical.add(h);

        for (int i = 0; i < n; i++) {
            cut[i][0] = in.rc();
            cut[i][1] = in.ri();
            if (cut[i][0] == 'H') {
                vertical.add(cut[i][1]);
            } else {
                horizontal.add(cut[i][1]);
            }
        }
        horizontal.calc();
        vertical.calc();
        long[] ans = new long[n];
        ans[n - 1] = area(horizontal, vertical);
        for(int i = n - 1; i >= 1; i--){
            if (cut[i][0] == 'H') {
                vertical.remove(cut[i][1]);
            } else {
                horizontal.remove(cut[i][1]);
            }
            ans[i - 1] = area(horizontal, vertical);
        }

        for(long a : ans){
            out.println(a);
        }
    }

    public long area(Largest a, Largest b){
        return (long)a.max * b.max;
    }
}

class Largest {
    int max;
    TreeSet<Integer> set = new TreeSet<>();

    public void add(int x) {
        set.add(x);
    }

    void calc() {
        max = 0;
        int last = set.first();
        for (int x : set) {
            max = Math.max(x - last, max);
            last = x;
        }
    }

    void remove(int x) {
        set.remove(x);
        max = Math.max(set.ceiling(x) - set.floor(x), max);
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
