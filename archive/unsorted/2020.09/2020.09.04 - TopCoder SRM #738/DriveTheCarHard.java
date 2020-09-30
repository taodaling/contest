package contest;


import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DriveTheCarHard {
    Debug debug = new Debug(true);

    public int findMinimumFuel(int T, int D) {
       // int tt = check(30000, 30000);
        int threshold = 2;
        if (T >= 400) {
            int atMost = 100;
            long[] last = new long[atMost];
            long[] next = new long[atMost];
            for (int i = 1; i <= T; i++) {
                Arrays.fill(next, 0);
                for (int j = 0; j < atMost; j++) {
                    for (int t = 0; j + t * t < atMost; t++) {
                        next[j + t * t] = Math.max(next[j + t * t], last[j] + i * t);
                    }
                }
                long[] tmp = last;
                last = next;
                next = tmp;
            }
            for (int i = 0; i < atMost; i++) {
                if (last[i] >= D) {
                    return i;
                }
            }
            throw new AssertionError();
        }

        LongPreSum lps = new LongPreSum(i -> i * i, T + 1);
        long[] lastBest = new long[D + 1];
        long[] nextBest = new long[D + 1];
        Arrays.fill(lastBest, (int) 1e18);
        lastBest[0] = 0;
        for (int i = 1; i <= T; i++) {
            Arrays.fill(nextBest, (int) 1e18);
            for (int j = 0; j <= D; j++) {
                int l = (int) Math.floor(i * j / (double) lps.prefix(i)) - threshold;
                int r = (int) Math.ceil(i * j / (double) lps.prefix(i)) + threshold;
                for (int k = Math.max(l, 0); k <= r; k++) {
                    if (j - k * i < 0) {
                        continue;
                    }
                    nextBest[j] = Math.min(nextBest[j], lastBest[j - k * i] + k * k);
                }
            }
            long[] tmp = lastBest;
            lastBest = nextBest;
            nextBest = tmp;
            //debug.debug("last", Arrays.toString(lastBest));
        }

        return (int)lastBest[D];
    }

    public int check(int T, int D) {
        LongPreSum lps = new LongPreSum(i -> i * i, T + 1);

        int[] opt = new int[D + 1];
        int[] lastBest = new int[D + 1];
        int[] nextBest = new int[D + 1];
        Arrays.fill(lastBest, (int) 1e9);
        lastBest[0] = 0;


        double max = 0;
        for (int i = 1; i <= T; i++) {
            Arrays.fill(nextBest, (int) 1e9);
            for (int j = 0; j <= D; j++) {
                for (int k = 0; j + k * i <= D; k++) {
                    if (nextBest[j + k * i] > lastBest[j] + k * k) {
                        nextBest[j + k * i] = lastBest[j] + k * k;
                        opt[j + k * i] = k;
                    }
                }
            }
            int[] tmp = lastBest;
            lastBest = nextBest;
            nextBest = tmp;

            for (int j = 0; j <= D; j++) {
                max = Math.max(max, Math.abs(opt[j] - i * j / (double) lps.prefix(i)));
            }
        }

        return (int) Math.ceil(max);
    }
}
