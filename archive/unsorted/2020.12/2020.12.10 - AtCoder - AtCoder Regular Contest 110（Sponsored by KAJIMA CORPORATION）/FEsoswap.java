package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FEsoswap {
    int[] p;
    List<Integer> op = new ArrayList<>();

    public void apply(int i) {
        op.add(i);
        SequenceUtils.swap(p, i, (i + p[i]) % p.length);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        p = new int[n];
        in.populate(p);

        boolean[] visited = new boolean[n];
        while (true) {
            Arrays.fill(visited, false);
            int round = 0;
            for (int i = 0; i < n; i++) {
                if (visited[i]) {
                    continue;
                }
                round++;
                int now = i;
                while (!visited[now]) {
                    visited[now] = true;
                    now = p[now];
                }
            }
            if (round == 1) {
                break;
            }
            int pos = RandomWrapper.INSTANCE.nextInt(0, n - 1);
            apply(pos);
        }
        for (int i = 0; i < n - 1; i++) {
            apply(0);
        }
        assert SortUtils.strictAscending(p, 0, p.length - 1);
        out.println(op.size());
        for(int x : op){
            out.println(x);
        }
    }
}
