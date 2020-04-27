package contest;

import template.utils.Debug;

import java.util.Arrays;

public class MeanMedian {
//
//    Debug debug = new Debug(true);
    public int minEffort(int needMean, int needMedian, int[] d) {
        int n = d.length;
        int sum = needMean * n;
        Arrays.sort(d);
        int half = (n + 1) / 2;
        int[] scores = new int[n];
        int ans = 0;
        int curSum = 0;
        for (int i = 0; i < half; i++) {
            scores[i] = needMedian;
            curSum += needMedian;
            ans += needMedian * d[i];
        }

        for (int i = 0; i < n && curSum < sum; i++) {
            int add = Math.min(10 - scores[i], sum - curSum);
            scores[i] += add;
            curSum += add;
            ans += add * d[i];
        }

//
//        debug.debug("d", d);
//        debug.debug("scores", scores);
        return ans;
    }
}
