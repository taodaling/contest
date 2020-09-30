package contest;

public class EllysBlood {
    public int getMax(int[] have, int[] need) {
        int ans = 0;
        int use0 = Math.min(have[0], need[0]);
        int cur = have[0] - use0;
        ans += use0;
        int use1 = Math.min(have[1], need[1]);
        int use2 = Math.min(have[2], need[2]);
        need[1] -= use1;
        have[1] -= use1;
        need[2] -= use2;
        have[2] -= use2;
        ans += use1 + use2;
        int t1 = Math.min(cur, need[1]);
        ans += t1;
        cur -= t1;
        int t2 = Math.min(cur, need[2]);
        ans += t2;
        cur -= t2;
        cur += have[1] + have[2];
        int use3 = Math.min(need[3], have[3] + cur);
        ans += use3;

        return ans;
    }
}
