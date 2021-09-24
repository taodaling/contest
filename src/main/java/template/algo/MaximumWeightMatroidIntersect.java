package template.algo;

import java.util.Arrays;

/**
 * O(r) invoke computeAdj and extend. O(r^2n^2)
 * maximum first, then maximum weight
 */
public class MaximumWeightMatroidIntersect extends MatroidIntersect {
    protected long[] weight;
    protected long[] pathWeight;
    protected static final long weightInf = (long) 1e18;
    protected boolean[] inq;
    protected long[] fixWeight;

    public MaximumWeightMatroidIntersect(int n, long[] weight) {
        super(n);
        this.weight = weight;
        pathWeight = new long[n];
        inq = new boolean[n];
        fixWeight = new long[n];
    }

    @Override
    protected boolean expand(MatroidIndependentSet a, MatroidIndependentSet b, int round) {
        a.prepare(added);
        b.prepare(added);
        Arrays.fill(x1, false);
        Arrays.fill(x2, false);
        a.extend(added, x1);
        b.extend(added, x2);

        for (int i = 0; i < n; i++) {
            if (added[i]) {
                Arrays.fill(adj1[i], false);
                Arrays.fill(adj2[i], false);
                fixWeight[i] = weight[i];
            } else {
                fixWeight[i] = -weight[i];
            }
        }

        a.computeAdj(added, adj1);
        b.computeAdj(added, adj2);
        Arrays.fill(dists, distInf);
        Arrays.fill(pathWeight, weightInf);
        Arrays.fill(pre, -1);
        dq.clear();
        for (int i = 0; i < n; i++) {
            if (added[i]) {
                continue;
            }
            //right
            if (x1[i]) {
                dists[i] = 0;
                pathWeight[i] = fixWeight[i];
                dq.addLast(i);
                inq[i] = true;
            }
        }

        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            inq[head] = false;

            for (int j = 0; j < n; j++) {
                if (added[head] != added[j] && adj(head, j)) {
                    int comp = Long.compare(pathWeight[j], pathWeight[head] + fixWeight[j]);
                    if (comp == 0) {
                        comp = Integer.compare(dists[j], dists[head] + 1);
                    }
                    if (comp <= 0) {
                        continue;
                    }
                    dists[j] = dists[head] + 1;
                    pathWeight[j] = pathWeight[head] + fixWeight[j];
                    pre[j] = head;
                    if (!inq[j]) {
                        inq[j] = true;
                        dq.addLast(j);
                    }
                }
            }
        }

        int tail = -1;
        for (int i = 0; i < n; i++) {
            if (!x2[i] || !x1[i] && pre[i] == -1) {
                continue;
            }
            if (tail == -1 || pathWeight[i] < pathWeight[tail] || pathWeight[i] == pathWeight[tail] &&
                    dists[i] < dists[tail]) {
                tail = i;
            }
        }
        if (tail == -1) {
            return false;
        }

        xorPath(tail);
        return true;
    }
}
