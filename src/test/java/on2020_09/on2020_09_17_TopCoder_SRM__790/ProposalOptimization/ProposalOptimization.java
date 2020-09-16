package on2020_09.on2020_09_17_TopCoder_SRM__790.ProposalOptimization;



import template.algo.DoubleBinarySearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProposalOptimization {
    public double bestPath(int R, int C, int K, int[] roses, int[] tulips, int[] costs) {
        weight = new double[R][C];
        cost = new int[R][C];
        front = new List[R][C];
        back = new List[R][C];
        frontVisit = new boolean[R][C];
        backVisit = new boolean[R][C];
        this.R = R;
        this.C = C;
        this.K = K;
        this.roses = roses;
        this.tulips = tulips;
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                cost[i][j] = costs[i * C + j];
                front[i][j] = new ArrayList<>();
                back[i][j] = new ArrayList<>();
            }
        }

        DoubleBinarySearch dbs = new DoubleBinarySearch(1e-10, 1e-10) {
            @Override
            public boolean check(double mid) {
                return best(mid) <= 0;
            }
        };

        double ans = dbs.binarySearch(0, 1e9);
        if (-1e50 == best(ans)) {
            return -1;
        }
        return ans;
    }

    public double best(double mid) {
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                weight[i][j] = roses[i * C + j] -
                        tulips[i * C + j] * mid;
                frontVisit[i][j] = false;
                backVisit[i][j] = false;
            }
        }

        int dist = R + C - 2;
        int half = dist / 2;
        double ans = -1e50;
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                int d = i + j;
                if (d != half) {
                    continue;
                }
                ans = Math.max(ans, optimize(front(i, j), back(i + 1, j)));
                ans = Math.max(ans, optimize(front(i, j), back(i, j + 1)));
            }
        }

        return ans;
    }

    public double optimize(List<State> a, List<State> b) {
        int r = b.size() - 1;
        double ans = -1e50;
        for (State state : a) {
            while (r >= 0 && state.cost + b.get(r).cost > K) {
                r--;
            }
            if (r < 0) {
                break;
            }
            ans = Math.max(ans, state.w + b.get(r).w);
        }
        return ans;
    }

    int[] roses;
    int[] tulips;
    double[][] weight;
    int[][] cost;
    List<State>[][] front;
    boolean[][] frontVisit;
    List<State>[][] back;
    boolean[][] backVisit;
    int R;
    int C;
    int K;

    public boolean inRange(int i, int j) {
        return i >= 0 && i < R && j >= 0 && j < C;
    }

    private int compare(State a, State b) {
        return a.cost == b.cost ? -Double.compare(a.w, b.w) : Integer.compare(a.cost, b.cost);
    }

    private void merge(List<State> a, List<State> b, List<State> c, int i, int j) {
        int al = 0;
        int bl = 0;
        int ar = a.size() - 1;
        int br = b.size() - 1;
        while (al <= ar || bl <= br) {
            State cand;
            if (bl > br || al <= ar && compare(a.get(al), b.get(bl)) < 0) {
                cand = a.get(al++);
            } else {
                cand = b.get(bl++);
            }
            if (cand.cost + cost[i][j] > K) {
                break;
            }
            if (!c.isEmpty() && c.get(c.size() - 1).w >= cand.w) {
                continue;
            }
            c.add(cand.clone());
        }
        for (State state : c) {
            state.cost += cost[i][j];
            state.w += weight[i][j];
        }
    }

    public List<State> front(int i, int j) {
        if (!inRange(i, j)) {
            return Collections.emptyList();
        }
        if (!frontVisit[i][j]) {
            frontVisit[i][j] = true;
            front[i][j].clear();
            if (i == 0 && j == 0) {
                State unique = new State();
                front[i][j].add(unique);
                return front[i][j];
            }
            merge(front(i - 1, j), front(i, j - 1),
                    front[i][j], i, j);
        }
        return front[i][j];
    }

    public List<State> back(int i, int j) {
        if (!inRange(i, j)) {
            return Collections.emptyList();
        }
        if (!backVisit[i][j]) {
            backVisit[i][j] = true;
            back[i][j].clear();
            if (i == R - 1 && j == C - 1) {
                State unique = new State();
                back[i][j].add(unique);
                return back[i][j];
            }
            merge(back(i + 1, j), back(i, j + 1),
                    back[i][j], i, j);
        }
        return back[i][j];
    }
}

class State implements Cloneable {
    double w;
    int cost;

    @Override
    public State clone() {
        try {
            return (State) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
