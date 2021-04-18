package on2021_03.on2021_03_27_Codeforces___Codeforces_Round__228__Div__1_.B__Fox_and_Minimal_path;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

public class BFoxAndMinimalPath {
    int ids = 2;

    public int alloc() {
        return ids++;
    }

    boolean[][] graph;
    int[] dist;

    void add(int i, int j) {
        graph[i][j] = graph[j][i] = true;
    }

    int getConstantId(int i) {
        return 1 + i;
    }

    public int[] build(int k) {
        if (k == 1) {
            return new int[]{1};
        }
        int[] res = build(k / 2);
        int a = alloc();
        int b = alloc();
        for (int x : res) {
            add(a, x);
            add(b, x);
        }
        dist[a] = dist[b] = dist[res[0]] + 1;
        IntegerArrayList ans = new IntegerArrayList(3);
        ans.add(a);
        ans.add(b);
        if (k % 2 == 1) {
            int c = alloc();
            ans.add(c);
            add(c, getConstantId(dist[a] - 1));
            dist[c] = dist[a];
        }
        return ans.toArray();
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        graph = new boolean[1000][1000];
        dist = new int[1000];
        dist[1] = 0;
        for (int i = 0, last = 1; i < 40; i++) {
            int id = alloc();
            add(last, id);
            dist[id] = dist[last] + 1;
            last = id;
        }

        int[] ans = build(k);
        for (int x : ans) {
            add(0, x);
        }
        dist[0] = dist[ans[0]] + 1;
        out.println(ids);
        for (int i = 0; i < ids; i++) {
            for (int j = 0; j < ids; j++) {
                if (graph[i][j]) {
                    out.append('Y');
                } else {
                    out.append('N');
                }
            }
            out.println();
        }
    }

//    public long way() {
//        int n = ids;
//        int[][] d = new int[n][n];
//        int inf = (int) 1e8;
//        SequenceUtils.deepFill(d, inf);
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                d[i][j] =
//            }
//        }
//        for (int k = 0; k < n; k++) {
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < n; j++) {
//                }
//            }
//        }
//    }
}
