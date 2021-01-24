package template.utils;

import template.primitve.generated.datastructure.IntegerArrayList;

/**
 * <pre>
 * 双流水线问题。
 * 考虑一个工厂，共需要生产$n$个零件。
 * 每个零件需要经过两个流水线打磨。
 * 第一个流水线处理第$i$个零件的时间为$a_i$，第二个流水线处理第$i$个零件的时间为$b_i$。
 * 每个流水线只有一台机器，因此一次性只能同时处理一个零件，且每个零件必须依次经过流水线一和流水线二的加工。
 * 并且如果某个零件比另外一个零件先经过流水线一的处理，那么前者必须比后者先经过流水线二的处理。
 * 现在允许任意排列零件的处理顺序，要求计算最小花费的时间。
 * </pre>
 */
public class DoubleWorkStreamProblem {
    /**
     * O(n log n)
     * @param a
     * @param b
     * @return
     */
    public static Pair<int[], Long> solve(long[] a, long[] b) {
        int n = a.length;
        IntegerArrayList l1 = new IntegerArrayList(n);
        IntegerArrayList l2 = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            if (a[i] < b[i]) {
                l1.add(i);
            } else {
                l2.add(i);
            }
        }
        l1.sort((i, j) -> Long.compare(a[i], a[j]));
        l2.sort((i, j) -> -Long.compare(b[i], b[j]));
        int[] ans = new int[n];
        System.arraycopy(l1.getData(), 0, ans, 0, l1.size());
        System.arraycopy(l2.getData(), 0, ans, l1.size(), l2.size());
        long A = 0;
        long B = 0;
        for (int i = 0; i < n; i++) {
            int index = ans[i];
            A += a[index];
            B = Math.max(A, B) + b[index];
        }
        return new Pair<>(ans, B);
    }
}