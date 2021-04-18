package on2021_03.on2021_03_31_Single_Round_Match_803.MarriageAndCirclingChallenge;



import template.datastructure.BitSet;

public class MarriageAndCirclingChallenge {
    long state;

    int rnd() {
        state = (state * 1103515245 + 12345) % (1L << 31);
        return (int) (state % 100);
    }


    public long solve(int N, int threshold, int state) {
        this.state = state;
        BitSet[] out = new BitSet[N];
        BitSet[] in = new BitSet[N];
        for (int i = 0; i < N; i++) {
            out[i] = new BitSet(N);
            in[i] = new BitSet(N);
        }
        for (int i = 0; i <= N - 1; i++) {
            for (int j = i + 1; j <= N - 1; j++) {
                if (rnd() < threshold) {
                    out[i].set(j);
                    in[j].set(i);
                } else {
                    out[j].set(i);
                    in[i].set(j);
                }
            }
        }
        BitSet tmp = new BitSet(N);
        long ans = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tmp.copy(out[i]);
                tmp.and(in[j]);
                int s1 = tmp.size();
                tmp.copy(out[j]);
                tmp.and(in[i]);
                int s2 = tmp.size();
                ans += (long) s1 * s2;
            }
        }
        return ans / 4;
    }
}
