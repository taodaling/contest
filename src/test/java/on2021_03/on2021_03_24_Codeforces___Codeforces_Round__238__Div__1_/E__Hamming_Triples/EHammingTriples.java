package on2021_03.on2021_03_24_Codeforces___Codeforces_Round__238__Div__1_.E__Hamming_Triples;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class EHammingTriples {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        IntegerArrayList[] lists = new IntegerArrayList[2];
        for (int i = 0; i < 2; i++) {
            lists[i] = new IntegerArrayList(m);
        }
        for (int i = 0; i < m; i++) {
            lists[in.ri()].add(in.ri());
        }

        for (int i = 0; i < 2; i++) {
            lists[i].sort();
        }
        long[] best = new long[2];
        for (int i = 0; i < 2; i++) {
            best = merge(best, maxCount000(lists[0]));
            best = merge(best, maxCount011ABC(n, lists[0], lists[1]));
            best = merge(best, maxCount011BAC(n, lists[0], lists[1]));
            best = merge(best, maxCount011BCA(n, lists[0], lists[1]));
            SequenceUtils.swap(lists, 0, 1);
        }
        debug.debugArray("best", best);
        if(best[1] == 0){
            best[0] = 0;
        }
        //out.println(best[0]);
        out.println(best[1]);
    }

    public long[] merge(long[] a, long[] b) {
        if (a[1] == 0) {
            return b;
        }
        if (b[1] == 0) {
            return a;
        }
        if (a[0] > b[0]) {
            return a;
        }
        if (a[0] < b[0]) {
            return b;
        }
        long[] ans = new long[]{a[0], a[1] + b[1]};
        return ans;
    }

    public long comb(long n, long m) {
        if (m > n) {
            return 0;
        }
        if (m == 0) {
            return 1;
        }
        return comb(n - 1, m - 1) * n / m;
    }


    /**
     * a<=b<=c
     *
     * @param zero
     * @param one
     * @return
     */
    public long[] maxCount011ABC(long n, IntegerArrayList zero, IntegerArrayList one) {
        //2(n + a - b)
        if (zero.isEmpty() || one.isEmpty()) {
            return new long[]{0, 0};
        }
        long[] best = new long[2];
        for (int av : zero.toArray()) {
            int bi = one.lowerBound(av);
            if (bi >= one.size()) {
                continue;
            }
            int bv = one.get(bi);
            int bj = one.upperBound(bv);
            int b = bj - bi;
            int c = one.size() - bj;
            long[] cand = new long[]{2 * (n + av - bv), comb(b, 2) + (long) b * c};
            best = merge(best, cand);
        }
        return best;
    }

    /**
     * b<a<=c
     *
     * @param n
     * @param zero
     * @param one
     * @return
     */
    public long[] maxCount011BAC(long n, IntegerArrayList zero, IntegerArrayList one) {
        //2(n + c - b)
        if (zero.isEmpty() || one.isEmpty()) {
            return new long[]{0, 0};
        }
        long cnt = 0;
        for (int av : zero.toArray()) {
            int bi = one.lowerBound(av) - 1;
            int bj = bi + 1;
            int b = bi + 1;
            int c = one.size() - bj;
            cnt += (long) b * c;
        }
        return new long[]{2 * n, cnt};
    }

    /**
     * b<=c<a
     *
     * @param n
     * @param zero
     * @param one
     * @return
     */
    public long[] maxCount011BCA(long n, IntegerArrayList zero, IntegerArrayList one) {
        //2(n + c - a)
        if (zero.isEmpty() || one.isEmpty()) {
            return new long[]{0, 0};
        }
        long[] best = new long[2];
        for (int av : zero.toArray()) {
            int bj = one.lowerBound(av) - 1;
            if (bj < 0) {
                continue;
            }
            int cv = one.get(bj);
            int bi = one.lowerBound(cv) - 1;
            int b = bi + 1;
            int c = bj + 1 - b;

            long[] cand = new long[]{2 * (n + cv - av), comb(c, 2) + (long) b * c};
            best = merge(best, cand);
        }
        return best;
    }

    public int firstCnt(IntegerArrayList list) {
        int i = 0;
        while (i + 1 < list.size() && list.get(i + 1) == list.first()) {
            i++;
        }
        return i + 1;
    }

    public int tailCnt(IntegerArrayList list) {
        int i = list.size() - 1;
        while (i - 1 >= 0 && list.get(i - 1) == list.tail()) {
            i--;
        }
        return list.size() - i;
    }

    public long[] maxCount000(IntegerArrayList list) {
        if (list.size() < 3) {
            return new long[]{0, 0};
        }
        if (list.first() == list.tail()) {
            return new long[]{0, comb(list.size(), 3)};
        }
        long best = 2L * (list.tail() - list.first());
        long cnt = 0;
        int firstCnt = firstCnt(list);
        int tailCnt = tailCnt(list);
        int n = list.size();
        cnt += comb(firstCnt, 2) * comb(tailCnt, 1);
        cnt += comb(firstCnt, 1) * comb(tailCnt, 2);
        cnt += comb(firstCnt, 1) * comb(tailCnt, 1) * (n - firstCnt - tailCnt);
        return new long[]{best, cnt};
    }
}

