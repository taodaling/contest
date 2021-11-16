package platform.leetcode;


import java.util.Arrays;

public class Solution {
    public static void main(String[] args) {

    }

    int n;
    int m;
    char[] cs;

    public boolean[] friendRequests(int n, int[][] restrictions, int[][] requests) {
        boolean[] ans = new boolean[requests.length];
        UndoDSU dsu = new UndoDSU(n);
        dsu.init();
        for (int i = 0; i < requests.length; i++) {
            int u = requests[i][0];
            int v = requests[i][1];
            UndoOperation uo = dsu.merge(u, v);
            uo.apply();
            boolean ok = true;
            for(int[] r : restrictions){
                int a = r[0];
                int b = r[1];
                if(dsu.find(a) == dsu.find(b)){
                    ok = false;
                    break;
                }
            }
            ans[i] = ok;
            if(!ok){
                uo.undo();
            }
        }
        return ans;
    }

    public long encode(long a, long b) {
        if (a > b) {
            long tmp = a;
            a = b;
            b = tmp;
        }
        return (a << 32) | (b & ((1L << 32) - 1));
    }

}

interface UndoOperation {
    static final UndoOperation NIL = new UndoOperation() {
        @Override
        public void apply() {

        }

        @Override
        public void undo() {

        }
    };

    void apply();

    void undo();
}

abstract class CommutativeUndoOperation implements UndoOperation {
    boolean flag;
    public static CommutativeUndoOperation NIL = new CommutativeUndoOperation() {
        @Override
        public void apply() {
        }

        @Override
        public void undo() {
        }
    };
}


class UndoDSU {
    int[] rank;
    int[] p;

    public UndoDSU(int n) {
        rank = new int[n];
        p = new int[n];
    }

    public void init() {
        Arrays.fill(rank, 1);
        Arrays.fill(p, -1);
    }

    public int find(int x) {
        while (p[x] != -1) {
            x = p[x];
        }
        return x;
    }

    public int size(int x) {
        return rank[find(x)];
    }

    public CommutativeUndoOperation merge(int a, int b) {
        return new CommutativeUndoOperation() {
            int x, y;


            public void apply() {
                x = find(a);
                y = find(b);
                if (x == y) {
                    return;
                }
                if (rank[x] < rank[y]) {
                    int tmp = x;
                    x = y;
                    y = tmp;
                }
                p[y] = x;
                rank[x] += rank[y];
            }


            public void undo() {
                int cur = y;
                while (p[cur] != -1) {
                    cur = p[cur];
                    rank[cur] -= rank[y];
                }
                p[y] = -1;
            }
        };
    }
}