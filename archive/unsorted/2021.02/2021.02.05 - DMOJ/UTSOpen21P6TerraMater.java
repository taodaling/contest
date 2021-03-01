package contest;

import structures.SegmentTree;
import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntPredicate;

public class UTSOpen21P6TerraMater {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        h = in.ri(n);
        map.clear();
        for (int x : h) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }
        int max = 0;
        for (IntegerEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            max = Math.max(max, iterator.getEntryValue());
        }
        if (max >= n - k) {
            out.println(0);
            return;
        }
        list = new LongArrayList(n);
        bit = new IntegerGenericBIT(n, Math::min, (int)1e9);
        dp = new DPState[n];
        for (int i = 0; i < n; i++) {
            dp[i] = new DPState();
            dp[i].index = i;
        }
        IntPredicate predicate = m -> minTime(m) <= k;
        int ans = BinarySearch.firstTrue(predicate, 1, (int) 1e9);
        out.println(ans);
    }

    IntegerHashMap map = new IntegerHashMap((int) 2e5, false);
    LongArrayList list;
    int[] h;
    DPState[] dp;
    IntegerGenericBIT bit;
    private void cast(DPState state) {
        long x = state.y - state.x;
        long y = state.y + state.x;
        state.x = x;
        state.y = y;
    }

    public int minTime(long d) {
        int n = h.length;
        list.clear();
        for (int i = 0; i < n; i++) {
            dp[i].index = i;
            dp[i].x = i * d;
            dp[i].y = h[i];
            cast(dp[i]);
            list.add(dp[i].y);
        }
        list.unique();
        Arrays.sort(dp, Comparator.<DPState>comparingLong(x -> -x.x).thenComparingLong(x -> x.y));
        bit.clear();
        int min = (int) 1e9;
        for (DPState state : dp) {
            int y = list.binarySearch(state.y);
            state.ans = bit.query(y) + state.index;
            state.ans = Math.min(state.ans, state.index);
            bit.update(y + 1, state.ans - state.index - 1);
            min = Math.min(min, state.ans + n - 1 - state.index);
        }
        return min;
    }
}

class DPState {
    long x;
    long y;
    int ans;
    int index;
}
