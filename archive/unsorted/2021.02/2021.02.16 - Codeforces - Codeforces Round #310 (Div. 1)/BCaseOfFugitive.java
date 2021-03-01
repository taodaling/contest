package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BCaseOfFugitive {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[][] islands = new long[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                islands[i][j] = in.rl();
            }
        }
        Require[] reqs = new Require[n - 1];
        for (int i = 0; i < n - 1; i++) {
            reqs[i] = new Require(i, islands[i + 1][0] - islands[i][1], islands[i + 1][1] - islands[i][0]);
        }
        Bridge[] bridges = new Bridge[m];
        for (int i = 0; i < m; i++) {
            bridges[i] = new Bridge();
            bridges[i].id = i;
            bridges[i].len = in.rl();
        }
        Require[] sortedByL = reqs.clone();
        Arrays.sort(sortedByL, Comparator.comparingLong(x -> x.l));
        PriorityQueue<Require> pqByR = new PriorityQueue<>(n, Comparator.comparingLong(x -> x.r));
        Arrays.sort(bridges, Comparator.comparingLong(x -> x.len));
        SimplifiedDeque<Require> dq = new Range2DequeAdapter<>(i -> sortedByL[i], 0, sortedByL.length - 1);
        for (Bridge b : bridges) {
            while (!dq.isEmpty() && dq.peekFirst().l <= b.len) {
                pqByR.add(dq.removeFirst());
            }
            if (pqByR.isEmpty()) {
                continue;
            }
            if (pqByR.peek().r < b.len) {
                out.println("No");
                return;
            }
            pqByR.remove().match = b.id;
        }
        if (!pqByR.isEmpty() || !dq.isEmpty()) {
            out.println("No");
            return;
        }
        out.println("Yes");
        for (Require r : reqs) {
            out.append(r.match + 1).append(' ');
        }
    }
}

class Bridge {
    long len;
    int id;
}

class Require {
    int match;
    int leftId;
    long l;
    long r;

    public Require(int leftId, long l, long r) {
        this.leftId = leftId;
        this.l = l;
        this.r = r;
    }
}