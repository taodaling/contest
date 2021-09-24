package on2021_07.on2021_07_21_DMOJ___Wesley_s_Anger_Contest_3.Problem_6___Zeyu_s_Sadness_Contest_1;



import template.datastructure.Splay;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Problem6ZeyusSadnessContest1 {
    Event[][] ac;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        List<Event>[] events = new List[m];
        for (int i = 0; i < m; i++) {
            events[i] = new ArrayList<>(k);
        }
        List<Event> all = new ArrayList<>(k);
        ac = new Event[m][n];
        Event[][] solveStatus = new Event[m][n];
        int[] occur = new int[n];
        for (int i = 0; i < k; i++) {
            Event e = new Event();
            e.c = in.ri() - 1;
            e.p = in.ri() - 1;
            e.r = in.ri();
            e.occur = i + 1;
            e.time = e.occur;
            solveStatus[e.p][e.c] = e;
            e.solved = occur[e.c];
            ac[e.solved][e.c] = e;
            events[e.solved].add(e);
            if (e.solved > 0) {
                events[e.solved - 1].add(e);
                e.time += ac[e.solved - 1][e.c].time;
            }
            occur[e.c]++;
            all.add(e);
        }

        stack = new IntegerMultiWayStack(n, 2 * k);
        Splay[] snodes = new Splay[n];
        for (int i = 0; i < n; i++) {
            snodes[i] = new Splay();
        }
        for (int r = 0; r < m; r++) {
            for (int i = 0; i < n; i++) {
                snodes[i].init(i);
            }
            stack.clear();
            int up = 0;
            Splay root = Splay.NIL;
            for (Event e : events[r]) {
                if (e.solved == r) {
                    int rank = e.r - up;
                    //add
                    if (rank > 1) {
                        root = Splay.selectKthAsRoot(root, rank - 1);
                        stack.addLast(e.c, root.key);
                    }
                    if (rank <= root.size) {
                        root = Splay.selectKthAsRoot(root, rank);
                        stack.addLast(root.key, e.c);
                    }
                    root = Splay.addAsKth(root, snodes[e.c], rank);
                } else {
                    //delete
                    root = snodes[e.c];
                    Splay.splay(root);
                    root = Splay.deleteRoot(root);
                    up++;
                }
            }
            for (int i = 0; i < n; i++) {
                calc(r, i);
            }
        }

        Contestant[] contestants = new Contestant[n];
        for (int i = 0; i < n; i++) {
            contestants[i] = new Contestant();
            contestants[i].id = i;
        }
        for (Event e : all) {
            contestants[e.c].solved++;
            contestants[e.c].penalty = e.time + e.cost * 10;
        }

        for (int i = m - 1; i >= 1; i--) {
            for (int j = 0; j < n; j++) {
                if (ac[i][j] != null) {
                    ac[i][j].cost -= ac[i - 1][j].cost;
                }
            }
        }
        Contestant[] sortByRank = contestants.clone();
        Arrays.sort(sortByRank, Comparator.<Contestant>comparingInt(x -> -x.solved).thenComparingLong(x -> x.penalty));
        for (Contestant c : sortByRank) {
            out.append(c.id + 1).append(' ');
            for (int i = 0; i < m; i++) {
                if (solveStatus[i][c.id] == null) {
                    out.append("-/-");
                } else {
                    out.append(solveStatus[i][c.id].occur).append('/').append(solveStatus[i][c.id].cost);
                }
                out.append(' ');
            }
            out.println();
        }
    }

    IntegerMultiWayStack stack;

    long calc(int i, int c) {
        if (ac[i][c] == null) {
            return 0;
        }
        if (ac[i][c].cost == -1) {
            ac[i][c].cost = 0;
            if (i > 0) {
                ac[i][c].cost = ac[i - 1][c].cost;
            }
            for (IntegerIterator iterator = stack.iterator(c); iterator.hasNext(); ) {
                int prev = iterator.next();
                long time = calc(i, prev) + 1;
                ac[i][c].cost = Math.max(ac[i][c].cost, DigitUtils.ceilDiv(Math.max(0, time - ac[i][c].time), 10));
            }
        }
        return ac[i][c].cost * 10 + ac[i][c].time;
    }
}

class Event {
    int c;
    int p;
    int r;
    int solved;

    int occur;
    long time;
    long cost = -1;

    @Override
    public String toString() {
        return "Event{" +
                "c=" + c +
                ", p=" + p +
                ", r=" + r +
                '}';
    }
}

class Contestant {
    int solved;
    long penalty;
    int id;
}