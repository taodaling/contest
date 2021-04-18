package on2021_03.on2021_03_26_Google_Coding_Competitions___Qualification_Round_2021___Code_Jam_2021.Median_Sort0;





import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.Randomized;

import java.util.stream.IntStream;

public class MedianSort {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int t = in.ri();
        int n = in.ri();
        int q = in.ri();
        for (int i = 1; i <= t; i++) {
            solveOne(n, i, in, out);
        }
    }

    public void solveOne(int n, int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int[] perm = IntStream.range(0, n).toArray();
        Randomized.shuffle(perm);
        IntegerArrayList list = new IntegerArrayList(perm);
        sort(list, -1, -1);
        for (int x : list.toArray()) {
            out.append(x + 1).append(' ');
        }
        out.println().flush();
        int ans = in.ri();
        if (ans == -1) {
            System.exit(1);
        }
    }

    FastInput in;
    FastOutput out;

    public int query(int a, int b, int c) {
        out.append(a + 1).append(' ').append(b + 1).append(' ').append(c + 1).println().flush();
        int ans = in.ri();
        if (ans == -1) {
            System.exit(1);
        }
        return ans - 1;
    }

    public void sort(IntegerArrayList list, int lbound, int rbound) {
        if (list.size() <= 1) {
            return;
        }
        IntegerArrayList left = new IntegerArrayList();
        IntegerArrayList mid = new IntegerArrayList();
        IntegerArrayList right = new IntegerArrayList();
        int first = list.pop();
        int next = list.pop();
        if (lbound != -1) {
            if (query(lbound, first, next) == next) {
                int tmp = first;
                first = next;
                next = tmp;
            }
        } else if (rbound != -1) {
            if (query(first, next, rbound) == first) {
                int tmp = first;
                first = next;
                next = tmp;
            }
        }
        for (int x : list.toArray()) {
            int ans = query(first, x, next);
            if (ans == first) {
                left.add(x);
            } else if (ans == next) {
                right.add(x);
            } else {
                assert ans == x;
                mid.add(x);
            }
        }
        sort(left, lbound, first);
        sort(mid, first, next);
        sort(right, next, rbound);
        list.clear();
        list.addAll(left);
        list.add(first);
        list.addAll(mid);
        list.add(next);
        list.addAll(right);
    }
}
