package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;
import java.util.Comparator;

public class Problem2SimplifyingTheFarm {
    int[] p;
    int[] size;

    public void init(int n) {
        p = new int[n];
        Arrays.fill(p, -1);
        size = new int[n];
        Arrays.fill(size, 1);
    }

    public int find(int x) {
        if (p[x] == -1) {
            return x;
        }
        return find(p[x]);
    }

    public int merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return -1;
        }
        if (size[a] < size[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        p[b] = a;
        size[a] += size[b];
        return b;
    }

    public void rollback(int x) {
        if (x == -1) {
            return;
        }
        for (int i = p[x]; i != -1; i = p[i]) {
            size[i] -= size[x];
        }
        p[x] = -1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] edges = new int[m][3];
        for (int i = 0; i < m; i++) {
            edges[i][0] = in.ri() - 1;
            edges[i][1] = in.ri() - 1;
            edges[i][2] = in.ri();
        }
        Arrays.sort(edges, Comparator.comparingInt(x -> x[2]));
        long mst = 0;
        long way = 1;
        int mod = (int) 1e9 + 7;
        IntegerDeque dq = new IntegerDequeImpl(3);
        init(n);
        for (int i = 0; i < m; i++) {
            int to = i;
            while (to + 1 < m && edges[to + 1][2] == edges[to][2]) {
                to++;
            }
            int len = to - i + 1;
            int possible = 0;
            int max = 0;
            int cnt = 0;
            for (int j = 0; j < 1 << len; j++) {
                dq.clear();
                boolean maybe = true;
                for (int k = 0; k < len; k++) {
                    if (Bits.get(j, k) == 0) {
                        continue;
                    }
                    int x = merge(edges[i + k][0], edges[i + k][1]);
                    maybe = maybe && x != -1;
                    dq.addLast(x);
                }
                if (maybe) {
                    possible = Bits.set(possible, j);
                    int cand = Integer.bitCount(j);
                    if(cand > max){
                        max = cand;
                        cnt = 0;
                    }
                    if(cand == max){
                        cnt++;
                    }
                }
                while (!dq.isEmpty()) {
                    rollback(dq.removeLast());
                }
            }
            for(int j = i; j <= to; j++){
                int res = merge(edges[j][0], edges[j][1]);
                if(res != -1){
                    mst += edges[j][2];
                }
            }
            way = way * cnt % mod;
            i = to;
        }
        out.append(mst).append(' ').append(way);
    }
}