package contest;

import template.algo.LongBinarySearch;
import template.math.DigitUtils;

public class FightMonsterDiv1 {
    public long fightTime(long hp, long attack, int level, long duration) {
        //not use magic
        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                //not use
                if (damage(mid, attack, level) >= hp) {
                    return true;
                }

                long since = Math.max(0, mid - duration - 1);
                return damage(since, attack, level) +
                        damage(mid - since - 1, attack + level * since, level) * 5 >= hp;
            }
        };
        long ans = lbs.binarySearch(1, hp);
        return ans;
    }

    long inf = (long) 1e15;

    public long damage(long n, long attack, int level) {
        return DigitUtils.mul(n, attack, inf) + DigitUtils.mul(DigitUtils.mul(n - 1, n, inf) / 2, level, inf);
    }
}
