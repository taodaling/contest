package on2021_07.on2021_07_10_Kattis.Tarot_Sham_Boast;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.string.IntFunctionIntSequenceAdapter;
import template.string.IntSequence;
import template.string.KMPAutomaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TarotShamBoast {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        char[] s = new char[(int) 1e6];
        double[] invC = new double[(int) 1e6];
        invC[0] = 1;
        for (int i = 1; i < invC.length; i++) {
            invC[i] = invC[i - 1] / 3;
        }
        CountStringContainSubstring cs = new CountStringContainSubstring(n);
        Guess[] guesses = new Guess[k];
        for (int i = 0; i < k; i++) {
            int m = in.rs(s);
            Guess guess = new Guess();
            guess.seq = Arrays.copyOf(s, m);
            guess.prob = cs.solve(3, n, new IntFunctionIntSequenceAdapter(j -> s[j], 0, m - 1),
                    invC);
            guess.index = i;
            guesses[i] = guess;
        }
        Arrays.sort(guesses, Comparator.<Guess>comparingDouble(x -> x.prob).thenComparingInt(x -> x.index));
        for (Guess g : guesses) {
            out.println(g.seq);
        }
    }
}

class Guess {
    char[] seq;
    double prob;
    int index;
}

class CountStringContainSubstring {
    KMPAutomaton kam;
    double[][][] borderPresum;
    double[][] presum;
    int maxN;
    IntegerArrayList offset;
    List<int[]> seqInfoList = new ArrayList<>(60);

    public CountStringContainSubstring(int maxN) {
        this.maxN = maxN;
        borderPresum = new double[2][60][];
        presum = new double[2][maxN];
        kam = new KMPAutomaton(maxN);
        offset = new IntegerArrayList(maxN);
    }

    private void prepare(int B, int m) {
        for (int i = 0; i < B; i++) {
            for (int j = 0; j < 2; j++) {
                if (borderPresum[j][i] == null) {
                    borderPresum[j][i] = new double[maxN];
                } else {
                    Arrays.fill(borderPresum[j][i], 0, m, 0);
                }
            }
        }
    }

    public double solve(int C, int n, IntSequence T, double[] invPowC) {
        assert C >= 0;
        assert n <= maxN;
        int tl = T.length();
        if (tl == 0) {
            return 1;
        }
        if (tl > n) {
            return 0;
        }
        kam.init();
        for (int i = 0; i < tl; i++) {
            kam.build(T.get(i));
        }
        offset.clear();
        int cur = tl;
        while (cur != 0) {
            cur = kam.maxBorder(cur - 1);
            if (cur != 0) {
                offset.add(tl - cur);
            }
        }
        seqInfoList.clear();
        while (!offset.isEmpty()) {
            int r = offset.pop();
            int delta = 0;
            int l = r;
            while (!offset.isEmpty()) {
                int back = offset.tail();
                if (delta == 0) {
                    delta = r - back;
                }
                if (l - back != delta) {
                    break;
                }
                l = back;
                offset.pop();
            }
            seqInfoList.add(new int[]{l, r, delta});
        }
        int[][] blocks = seqInfoList.toArray(new int[0][]);
        int B = blocks.length;

        assert B <= 60;
        //prepare
        prepare(B, n);
        for (int i = 0; i < 2; i++) {
            Arrays.fill(presum[i], 0, n, 0);
        }

        //odd for plus, even for sub
        double ans = 0;
        presum[1][0] = 1;
        for (int i = 0; i < n; i++) {
            //push presum
            for (int j = 0; j < 2; j++) {
                double way = 0;
                if (i + 1 < n) {
                    presum[j][i + 1] += presum[j][i];
                }
                way += presum[j][i];
                for (int k = 0; k < B; k++) {
                    way += borderPresum[j][k][i];
                    double transfer = invPowC[blocks[k][2]] * borderPresum[j][k][i];
                    if (i + blocks[k][2] < n && blocks[k][2] > 0) {
                        borderPresum[j][k][i + blocks[k][2]] += transfer;
                    }
                }
                if (way == 0) {
                    continue;
                }
                //way 1, no overlap
                if (i + tl < n) {
                    presum[j ^ 1][i + tl] += way * invPowC[tl];
                }
                //have overlap, then only log boarders
                for (int k = 0; k < B; k++) {
                    int l = blocks[k][0];
                    int r = blocks[k][1] + blocks[k][2];
                    if (i + l < n) {
                        borderPresum[j ^ 1][k][i + l] += way * invPowC[l];
                        if (l < r && i + r < n) {
                            borderPresum[j ^ 1][k][i + r] -= way * invPowC[r];
                        }
                    }
                }

                //or no next occurrence, just count it
                if (i + tl <= n) {
                    //ok
                    double contrib = way * invPowC[tl];
                    if ((j & 1) == 0) {
                        contrib = -contrib;
                    }
                    ans += contrib;
                }
            }
        }

        return 1 - ans;
    }
}
