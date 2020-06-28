package on2020_06.on2020_06_28_Codeforces___Codeforces_Round__405__rated__Div__1__based_on_VK_Cup_2017_Round_1_.C__Bear_and_Company0;



import groovy.lang.IntRange;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRadix;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.ArrayIndex;

import java.util.Arrays;
import java.util.stream.IntStream;

public class CBearAndCompany {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = in.readString().toCharArray();
        int[] cnts = new int[3];
        IntegerList[] indices = IntStream.range(0, 3).mapToObj(i -> new IntegerList()).toArray(i -> new IntegerList[i]);
        for (int i = 0; i < n; i++) {
            s[i] = (char) (s[i] == 'V' ? 0 : s[i] == 'K' ? 1 : 2);
            cnts[s[i]]++;
            indices[s[i]].add(i);
        }

        int[][] prevCnt = new int[n][3];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                prevCnt[i][j] = prevCnt[i - 1][j];
            }
            prevCnt[i][s[i - 1]]++;
        }

        ArrayIndex ai = new ArrayIndex(cnts[0] + 1, cnts[1] + 1, cnts[2] + 1, 2);
        int[] dp = new int[ai.totalSize()];
        int[] next = new int[ai.totalSize()];
        int inf = (int) 1e9;
        Arrays.fill(dp, inf);
        dp[ai.indexOf(0, 0, 0, 0)] = 0;
        for (int i = 0; i < n; i++) {
            Arrays.fill(next, inf);
            for (int a = 0; a <= cnts[0]; a++) {
                for (int b = 0; b <= cnts[1]; b++) {
                    for (int c = 0; c <= cnts[2]; c++) {
                        for (int t = 0; t < 2; t++) {
                            int val = dp[ai.indexOf(a, b, c, t)];
                            //put 0
                            if (a + 1 <= cnts[0]) {
                                int[] prev = prevCnt[indices[0].get(a)];
                                next[ai.indexOf(a + 1, b, c, 1)] = Math.min(next[ai.indexOf(a + 1, b, c, 1)], val + remain(prev[0], prev[1], prev[2], a, b, c));
                            }
                            //put 1
                            if (b + 1 <= cnts[1] && t == 0) {
                                int[] prev = prevCnt[indices[1].get(b)];
                                next[ai.indexOf(a, b + 1, c, 0)] = Math.min(next[ai.indexOf(a, b + 1, c, 0)], val + remain(prev[0], prev[1], prev[2], a, b, c));
                            }
                            //put 2
                            if (c + 1 <= cnts[2]) {
                                int[] prev = prevCnt[indices[2].get(c)];
                                next[ai.indexOf(a, b, c + 1, 0)] = Math.min(next[ai.indexOf(a, b, c + 1, 0)], val + remain(prev[0], prev[1], prev[2], a, b, c));
                            }
                        }
                    }
                }
            }

            int[] tmp = next;
            next = dp;
            dp = tmp;
        }

        int ans = Math.min(dp[ai.indexOf(cnts[0], cnts[1], cnts[2], 0)], dp[ai.indexOf(cnts[0], cnts[1], cnts[2], 1)]);
        out.println(ans);
    }

    public int remain(int a, int b, int c, int usedA, int usedB, int usedC) {
        return Math.max(a - usedA, 0) + Math.max(b - usedB, 0) + Math.max(c - usedC, 0);
    }
}
