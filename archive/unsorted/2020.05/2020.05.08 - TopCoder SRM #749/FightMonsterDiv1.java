package contest;

import template.algo.LongBinarySearch;
import template.math.DigitUtils;
import template.math.MathCalculation;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class FightMonsterDiv1 {

    public long fightTime(long hp, long attack, int level, long duration) {
        long improve = level * attack / 100;
        //not use magic
        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                //not use
                if (damage(mid, attack, improve) >= hp) {
                    return true;
                }

                long since = Math.max(1, mid - duration);
                return damage(since - 1, attack, improve) + damage(mid - since, attack + improve * (since - 1), improve) * 5 >= hp;
            }
        };
        long ans = lbs.binarySearch(1, hp);
        return ans;
    }


    long inf = (long) 1e15;

    public long damage(long n, long attack, long improve) {
        return DigitUtils.mul(n, attack, inf) + DigitUtils.mul(DigitUtils.mul(n, n - 1, inf) / 2, improve, inf);
    }
}
