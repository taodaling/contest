package template.graph;

import template.utils.SequenceUtils;

import java.util.Arrays;

public class HungrayAlgoMatrixStyle {
    boolean[][] edges;
    int[][] visited;
    int[][] partner;
    int now;
    int n;
    int m;

    public HungrayAlgoMatrixStyle(int n, int m) {
        this.n = n;
        this.m = m;
        edges = new boolean[n][m];
        visited = new int[][]{new int[n], new int[m]};
        partner = new int[][]{new int[n], new int[m]};
    }

    public void reset() {
        SequenceUtils.deepFill(edges, false);
        SequenceUtils.deepFill(partner, -1);
    }

    public void addEdge(int l, int r, boolean greedy) {
        edges[l][r] = true;
        if (greedy && partner[0][l] == -1 && partner[1][r] == -1) {
            partner[0][l] = r;
            partner[1][r] = l;
        }
    }

    public boolean matchLeft(int l) {
        if (partner[0][l] != -1) {
            return true;
        }
        now++;
        return bind(l);
    }

    public int leftPartner(int l) {
        return partner[0][l];
    }

    public int rightPartner(int r) {
        return partner[1][r];
    }

    private boolean bind(int l) {
        if (visited[0][l] == now) {
            return false;
        }
        visited[0][l] = now;
        for (int i = 0; i < m; i++) {
            if (!edges[l][i]) {
                continue;
            }
            if (release(i)) {
                partner[0][l] = i;
                partner[1][i] = l;
                return true;
            }
        }
        return false;
    }

    private boolean release(int r) {
        if (visited[1][r] == now) {
            return false;
        }
        visited[1][r] = now;
        if (partner[1][r] == -1) {
            return true;
        }
        return bind(partner[1][r]);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(partner);
    }
}
