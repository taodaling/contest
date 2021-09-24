package template.datastructure.summary;

import template.utils.UpdatableSum;

/**
 * support range update range sum
 * <br>
 * A cell is active only when its value greater than 0
 */
public class ActiveSum implements UpdatableSum<ActiveSum, ActiveUpdate> {
    public long minVal;
    public long minWeight;

    @Override
    public void add(ActiveSum right) {
        long min = Math.min(minVal, right.minVal);
        minWeight = (min == minVal ? minWeight : 0) + (min == right.minVal ? right.minWeight : 0);
        minVal = min;
    }

    @Override
    public void copy(ActiveSum right) {
        minVal = right.minVal;
        minWeight = right.minWeight;
    }

    @Override
    public ActiveSum clone() {
        ActiveSum sum = new ActiveSum();
        sum.copy(this);
        return sum;
    }

    @Override
    public void update(ActiveUpdate activeUpdate) {
        minVal += activeUpdate.delta;
    }

    public long sumOfUnactiveCell() {
        return minVal == 0 ? minWeight : 0;
    }


    public void init() {
        minWeight = minVal = 0;
    }

    public void asUnactive(long total) {
        minVal = 0;
        minWeight = total;
    }

    public static ActiveSum ofUnactive(long total) {
        ActiveSum ans = new ActiveSum();
        ans.asUnactive(total);
        return ans;
    }

    @Override
    public String toString() {
        return "" + minVal;
    }
}

