package on2021_05.on2021_05_26_Single_Round_Match_806.MostSubstrings;



import template.string.AhoCorasick;
import template.string.KMPAutomaton;

import java.util.Arrays;

public class MostSubstrings {
    int mod = (int) 1e9 + 7;

    public int count(String S, int L) {
        int N = S.length();
        AhoCorasick ac = new AhoCorasick('a', 'z', S.length());
        for (char c : S.toCharArray()) {
            ac.build(c);
        }
        int end = ac.buildLast;
        int[] topo = ac.endBuild();
        int m = topo.length;
        int[][] next = ac.next;
        int[] bestPrev = new int[m];
        int[] bestNext = new int[m];
        long[] wayPrev = new long[m];
        long[] wayNext = new long[m];

        int inf = (int) 1e8;
        Arrays.fill(bestPrev, -inf);
        Arrays.fill(wayPrev, 0);
        wayPrev[0] = 1;
        bestPrev[0] = 0;

        int charset = 'z' - 'a' + 1;
        for (int i = 0; i < L; i++) {
            Arrays.fill(bestNext, -inf);
            Arrays.fill(wayNext, 0);

            for (int j = 0; j < m; j++) {
                wayPrev[j] %= mod;
                for (int c = 0; c < charset; c++) {
                    int to = next[c][j];
                    int v = bestPrev[j] + (to == end ? 1 : 0);
                    if (bestNext[to] < v) {
                        bestNext[to] = v;
                        wayNext[to] = 0;
                    }
                    if (bestNext[to] == v) {
                        wayNext[to] += wayPrev[j];
                    }
                }
            }

            {
                int[] tmp = bestPrev;
                bestPrev = bestNext;
                bestNext = tmp;
            }
            {
                long[] tmp = wayPrev;
                wayPrev = wayNext;
                wayNext = tmp;
            }
        }

        int best = -1;
        long sum = 0;
        for(int i = 0; i < m; i++){
            if(best < bestPrev[i]){
                best = bestPrev[i];
                sum = 0;
            }
            if(best == bestPrev[i]){
                sum += wayPrev[i] % mod;
            }
        }
        sum %= mod;

        return (int) sum;
    }
}
