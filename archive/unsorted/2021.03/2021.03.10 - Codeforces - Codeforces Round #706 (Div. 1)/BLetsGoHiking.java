package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BLetsGoHiking {
    int[] p;

    boolean isTop(int i) {
        boolean ans = true;
        if (i - 1 >= 0 && p[i - 1] > p[i]) {
            ans = false;
        }
        if (i + 1 < p.length && p[i + 1] > p[i]) {
            ans = false;
        }
        return ans;
    }

    boolean isBot(int i) {
        boolean ans = true;
        if (i - 1 >= 0 && p[i - 1] < p[i]) {
            ans = false;
        }
        if (i + 1 < p.length && p[i + 1] < p[i]) {
            ans = false;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        p = in.ri(n);
        int[] leftTop = new int[n];
        int[] leftBot = new int[n];
        int[] rightTop = new int[n];
        int[] rightBot = new int[n];
        leftTop[0] = leftBot[0] = 0;
        for (int i = 1; i < n; i++) {
            leftTop[i] = isTop(i - 1) ? i - 1 : leftTop[i - 1];
            leftBot[i] = isBot(i - 1) ? i - 1 : leftBot[i - 1];
        }
        rightTop[n - 1] = rightBot[n - 1] = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            rightTop[i] = isTop(i + 1) ? i + 1 : rightTop[i + 1];
            rightBot[i] = isBot(i + 1) ? i + 1 : rightBot[i + 1];
        }
        int[] leftDist = new int[n];
        int[] leftMaxDist = new int[n];
        int[] rightDist = new int[n];
        int[] rightMaxDist = new int[n];
        for (int i = 0; i < n; i++) {
            leftDist[i] = i - Math.max(leftBot[i], leftTop[i]);
            leftMaxDist[i] = leftDist[i];
            if (i > 0) {
                leftMaxDist[i] = Math.max(leftMaxDist[i], leftMaxDist[i - 1]);
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            rightDist[i] = Math.min(rightBot[i], rightTop[i]) - i;
            rightMaxDist[i] = rightDist[i];
            if (i + 1 < n) {
                rightMaxDist[i] = Math.max(rightMaxDist[i], rightMaxDist[i + 1]);
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (!isTop(i)) {
                continue;
            }
            if (leftDist[i] != rightDist[i]) {
                continue;
            }
            if (leftDist[i] % 2 == 1) {
                continue;
            }
            int leftPartner = leftBot[i];
            int rightPartner = rightBot[i];
            if (leftPartner >= 0 && leftMaxDist[leftPartner] >= leftDist[i]) {
                continue;
            }
            if(rightPartner < n && rightMaxDist[rightPartner] >= leftDist[i]){
                continue;
            }
            ans++;
        }
        out.println(ans);
    }
}
