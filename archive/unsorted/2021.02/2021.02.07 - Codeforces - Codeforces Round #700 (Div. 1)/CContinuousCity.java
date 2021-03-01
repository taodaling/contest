package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CContinuousCity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int l = in.ri();
        int r = in.ri();
        r -= (l - 1);
        int[][] range = new int[32][2];
        range[0][0] = 0;
        range[0][1] = 0;
        int wpos = 1;
        List<int[]> edge = new ArrayList<>();
        while (range[wpos - 1][1] < r) {
            int size = 0;
            edge.add(new int[]{0, wpos, 1});
            size++;
            for (int i = wpos - 1; i >= 1; i--) {
                if (size + len(range[i]) <= r) {
                    edge.add(new int[]{i, wpos, size});
                    size += len(range[i]);
                }
            }
            range[wpos][0] = 1;
            range[wpos][1] = size;
            wpos++;
        }
        for (int[] e : edge) {
            if (e[0] == wpos - 1 || e[1] == wpos - 1) {
                e[2] += l - 1;
            }
        }
        out.println("YES");
        out.append(wpos).append(' ').append(edge.size()).println();
        for (int[] e : edge) {
            out.append(e[0] + 1).append(' ').append(e[1] + 1).append(' ').append(e[2]).println();
        }
    }

    public int len(int[] r) {
        return r[1] - r[0] + 1;
    }
}
