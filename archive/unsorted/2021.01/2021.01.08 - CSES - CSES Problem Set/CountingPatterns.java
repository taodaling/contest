package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.AhoCorasick;

public class CountingPatterns {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s, 0);
        int k = in.ri();
        int[] end = new int[k];
        char[] buf = new char[(int) 5e5];
        AhoCorasick ac = new AhoCorasick('a', 'z', (int) 5e5);
        for (int i = 0; i < k; i++) {
            int m = in.rs(buf, 0);
            ac.prepareBuild();
            for (int j = 0; j < m; j++) {
                ac.build(buf[j]);
            }
            end[i] = ac.buildLast;
        }
        int[] topo = ac.endBuild();
        int[] visit = new int[topo.length];
        ac.prepareMatch();
        for(int i = 0; i < n; i++){
            ac.match(s[i]);
            visit[ac.matchLast]++;
        }
        for(int i = topo.length - 1; i >= 1; i--){
            int node = topo[i];
            int p = ac.fails[node];
            visit[p] += visit[node];
        }
        for(int i = 0; i < k; i++){
            out.println(visit[end[i]]);
        }
    }
}
