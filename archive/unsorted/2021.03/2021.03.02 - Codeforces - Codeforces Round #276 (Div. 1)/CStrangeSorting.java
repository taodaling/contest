package contest;

import template.datastructure.DiscreteMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.Pair;

import java.util.Arrays;

public class CStrangeSorting {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.rs(s);
        s = Arrays.copyOf(s, n);
        int q = in.ri();
        to = new int[n];
        char[] res = new char[n];
        visited = new boolean[n];
        for (int i = 0; i < q; i++) {
            int k = in.ri();
            int d = in.ri();
//            if(d == 1){
//                out.println(s);
//                continue;
//            }
            Arrays.fill(visited, false);
            Arrays.fill(res, (char) 0);
            int wpos = 0;
            for (int j = 0; j < d; j++) {
                for (int z = j; z < k; z += d) {
                    to[z] = wpos++;
                }
            }
            for (int j = 0; j < k; j++) {
                to[j]--;
            }
            circle.clear();
            dfs(k - 1);
            int round = n - k + 1;
            int head = n - (k - 1);
            for (int j = 0; j < circle.size(); j++) {
                if (round >= circle.size() - j) {
                    res[circle.size() - j - 1] = s[circle.get(j)];
                } else {
                    res[circle.get(j + round) + head] = s[circle.get(j)];
                }
            }
            for (int j = k; j < n; j++) {
                int remainRound = n - j;
                if (remainRound >= circle.size()) {
                    int whichRound = j - (k - 1);
                    res[whichRound + circle.size() - 1] = s[j];
                }else{
                    res[head + circle.get(remainRound)] = s[j];
                }
            }
            for (int j = 0; j < k - 1; j++) {
                if (visited[j]) {
                    continue;
                }
                circle.clear();
                boolean c = dfs(j);
                assert c;
                for (int z = 0; z < circle.size(); z++) {
                    int y = (z + round) % circle.size();
                    res[n - k + (circle.get(y) + 1)] = s[circle.get(z)];
                }
            }
//            debug.debug("res", new String(res));
//            for (int j = 0; j < n; j++) {
//                assert res[j] > 0;
//            }
            char[] tmp = res;
            res = s;
            s = tmp;
            out.append(s).println();
        }
    }

    int[] to;
    boolean[] visited;
    IntegerArrayList circle = new IntegerArrayList((int) 1e6);
    int inf = (int) 1e9;

    boolean dfs(int root) {
        if (root == -1) {
            return false;
        }
        if (visited[root]) {
            return true;
        }
        visited[root] = true;
        circle.add(root);
        return dfs(to[root]);
    }
}
