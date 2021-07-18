package template.datastructure.summary;

import template.utils.UpdatableSum;

/**
 * Support single point update and range query
 */
public class BracketSum implements UpdatableSum<BracketSum, BracketUpdate> {
    int maxSize;
    int worst;
    int sum;

    @Override
    public void add(BracketSum bracketSum) {
        maxSize = Math.max(maxSize, bracketSum.maxSize);
        worst = Math.min(worst, bracketSum.worst + sum);
        sum += bracketSum.sum;
    }

    @Override
    public void copy(BracketSum bracketSum) {
        maxSize = bracketSum.maxSize;
        worst = bracketSum.worst;
        sum = bracketSum.sum;
    }

    @Override
    public BracketSum clone() {
        BracketSum ans = new BracketSum();
        ans.copy(this);
        return ans;
    }

    @Override
    public void update(BracketUpdate bracketUpdate) {
        maxSize += bracketUpdate.size;
        worst += bracketUpdate.x;
        sum += bracketUpdate.x;
    }

    public boolean isEmpty() {
        return maxSize == 0;
    }

    public boolean isValid() {
        return maxSize <= 1 && worst >= 0 && sum == 0;
    }

    public void init(){
        maxSize = worst = sum = 0;
    }

    @Override
    public String toString() {
        return "" + sum;
    }
}
