package on2019_11.on2019_11_26_AtCoder_Beginner_Contest_111.D___Robot_Arms;



import template.datastructure.CharList;
import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[][] xy = new long[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                xy[i][j] = in.readInt();
            }
        }
        for (int i = 1; i < n; i++) {
            if (parity(xy[i]) != parity(xy[i - 1])) {
                out.println(-1);
                return;
            }
        }

        boolean odd = parity(xy[0]) == 1;
        if (!odd) {
            out.println(35 + 1);
        } else {
            out.println(35);
        }
        for (int i = 35 - 1; i >= 0; i--) {
            out.append((1L << i)).append(' ');
        }
        if (!odd) {
            out.append(1);
        }
        out.println();
        for (int i = 0; i < n; i++) {
            CharList trace = new CharList(36);
            long[] pos = xy[i].clone();
            if (!odd) {
                pos[0]++;
            }
            solve(34, pos, trace);
            if (!odd) {
                trace.add('L');
            }
            for (int j = 0; j < trace.size(); j++) {
                out.append(trace.get(j));
            }
            out.println();
        }
    }

    public void solve(int k, long[] pos, CharList trace) {
        if (k == -1) {
            return;
        }
        long jump = 1L << k;
        long[] up = pos.clone();
        up[1] += jump;
        long[] left = pos.clone();
        left[0] -= jump;
        long[] right = pos.clone();
        right[0] += jump;
        long[] down = pos.clone();
        down[1] -= jump;

        if (distToSrc(up) < jump) {
            trace.add('D');
            solve(k - 1, up, trace);
            return;
        }
        if (distToSrc(down) < jump) {
            trace.add('U');
            solve(k - 1, down, trace);
            return;
        }
        if (distToSrc(left) < jump) {
            trace.add('R');
            solve(k - 1, left, trace);
            return;
        }
        if (distToSrc(right) < jump) {
            trace.add('L');
            solve(k - 1, right, trace);
            return;
        }
    }

    public long distToSrc(long[] xy) {
        return Math.abs(xy[0]) + Math.abs(xy[1]);
    }

    public long parity(long[] xy) {
        return (Math.abs(xy[0]) +
                Math.abs(xy[1])) % 2;
    }
}
