package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class BDynamicProblemScoring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] submissions = new int[n][5];
        for (int i = 0; i < n; i++) {
            in.populate(submissions[i]);
        }
        SequenceUtils.swap(submissions, 0, 1);

        int[] cnts = new int[5];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 5; j++) {
                if (submissions[i][j] == -1) {
                    continue;
                }
                cnts[j]++;
            }
        }

        for (int i = 0; i < n; i++) {
            boolean notSubmitted = true;
            for (int t : submissions[i]) {
                if (t != -1) {
                    notSubmitted = false;
                }
            }

            if (notSubmitted) {
                n--;
            }
        }

        int finalN = n;
        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                int total = mid + finalN;
                int[] localCnts = cnts.clone();
                for (int i = 0; i < 5; i++) {
                    if (submissions[0][i] != -1 && submissions[1][i] != -1 &&
                            (submissions[1][i] > submissions[0][i])) {
                        localCnts[i] += mid;
                    }
                }

                long[] scores = new long[2];
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < 5; i++) {
                        if (submissions[j][i] == -1) {
                            continue;
                        }
                        scores[j] += score(localCnts[i], total) * (250 - submissions[j][i]);
                    }
                }

                return scores[0] < scores[1];
            }
        };

        for (int i = 0; i < 10000; i++) {
            if (ibs.check(i)) {
                out.println(i);
                return;
            }
        }

        int ans = ibs.binarySearch(10000, (int) (1e9 + 7));
        if (!ibs.check(ans)) {
            out.println(-1);
            return;
        }
        out.println(ans);
    }

    public long score(long solved, long total) {
        long ans = 500;
        if (solved * 2 > total) {
            return ans;
        }
        ans += 500;
        if (solved * 4 > total) {
            return ans;
        }
        ans += 500;
        if (solved * 8 > total) {
            return ans;
        }
        ans += 500;
        if (solved * 16 > total) {
            return ans;
        }
        ans += 500;
        if (solved * 32 > total) {
            return ans;
        }
        ans += 500;
        return ans;
    }
}
