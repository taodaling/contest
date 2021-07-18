package template.datastructure.summary;

import template.utils.UpdatableSum;

/**
 * support range update range sum
 * <br>
 * A cell is active only when its value greater than 0
 */
public class ActiveSum implements UpdatableSum<ActiveSum, ActiveUpdate> {
    public long minVal;
    public long total;
    public long minWeight;

    @Override
    public void add(ActiveSum activeSum) {
        long min = Math.min(minVal, activeSum.minVal);
        minWeight = (min == minVal ? minWeight : 0) + (min == activeSum.minVal ? activeSum.minWeight : 0);
        total += activeSum.total;
        minVal = min;
    }

    @Override
    public void copy(ActiveSum activeSum) {
        minVal = activeSum.minVal;
        total = activeSum.total;
        minWeight = activeSum.minWeight;
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

    public long sumOfActiveCell() {
        return total - sumOfUnactiveCell();
    }

    public void init() {
        minWeight = minVal = total = 0;
    }

    public void asUnactive(long total) {
        minVal = 0;
        minWeight = this.total = total;
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

