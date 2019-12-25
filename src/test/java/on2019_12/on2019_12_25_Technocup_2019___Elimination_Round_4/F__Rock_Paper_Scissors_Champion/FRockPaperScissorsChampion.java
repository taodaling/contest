package on2019_12.on2019_12_25_Technocup_2019___Elimination_Round_4.F__Rock_Paper_Scissors_Champion;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;

import java.util.Arrays;
import java.util.TreeSet;

public class FRockPaperScissorsChampion {
    int n;
    Segment[] segs;
    TreeSet<Integer>[] sets;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();

        char[] s = new char[n + 1];
        in.readString(s, 1);

        segs = new Segment[3];
        sets = new TreeSet[3];
        for (int i = 0; i < 3; i++) {
            segs[i] = new Segment(1, n);
            sets[i] = new TreeSet<>();
        }

        for (int i = 1; i <= n; i++) {
            int t = typeOf(s[i]);
            sets[t].add(i);
            segs[t].updateReplace(i, i, 1, n, 0, 1);
        }

        for (int i = 0; i < 3; i++) {
            if (sets[i].isEmpty()) {
                continue;
            }
            int first = sets[i].first();
            int last = sets[i].last();

            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    continue;
                }
                segs[j].updateSetBit(first, n, 1, n, maskFor(i, j, true));
                segs[j].updateSetBit(1, last, 1, n, maskFor(i, j, false));
            }
        }

        out.println(query());
        for (int i = 1; i <= q; i++) {
            int index = in.readInt();
            char sign = in.readChar();
            remove(index, typeOf(s[index]));
            s[index] = sign;
            add(index, typeOf(s[index]));
            out.println(query());
        }
    }

    public void add(int i, int t) {
        sets[t].add(i);

        int mask = 0;
        for (int j = 0; j < 3; j++) {
            if (j == t || sets[j].isEmpty()) {
                continue;
            }
            int first = sets[j].first();
            int last = sets[j].last();
            if (first < i) {
                mask |= maskFor(j, t, true);
            }
            if (last > i) {
                mask |= maskFor(j, t, false);
            }
        }

        segs[t].updateReplace(i, i, 1, n, mask, 1);

        if (sets[t].first() == i) {
            Integer next = sets[t].ceiling(i + 1);
            if (next == null) {
                next = n + 1;
            }
            for (int j = 0; j < 3; j++) {
                if (j == t) {
                    continue;
                }
                segs[j].updateSetBit(i, next - 1, 1, n, maskFor(t, j, true));
            }
        }
        if (sets[t].last() == i) {
            Integer last = sets[t].floor(i - 1);
            if (last == null) {
                last = 0;
            }
            for (int j = 0; j < 3; j++) {
                if (j == t) {
                    continue;
                }
                segs[j].updateSetBit(last + 1, i, 1, n, maskFor(t, j, false));
            }
        }
    }

    public void remove(int i, int t) {
        sets[t].remove(i);
        segs[t].updateReplace(i, i, 1, n, 0, 0);
        if (sets[t].isEmpty() || sets[t].first() > i) {
            int first = sets[t].isEmpty() ? n + 1 : sets[t].first();
            for (int j = 0; j < 3; j++) {
                if (j == t) {
                    continue;
                }
                segs[j].updateRemoveBit(i, first - 1, 1, n, maskFor(t, j, true));
            }
        }

        if (sets[t].isEmpty() || sets[t].last() < i) {
            int last = sets[t].isEmpty() ? 0 : sets[t].last();
            for (int j = 0; j < 3; j++) {
                if (j == t) {
                    continue;
                }
                segs[j].updateRemoveBit(last + 1, i, 1, n, maskFor(t, j, false));
            }
        }
    }

    public int query() {
        int ans = 0;
        for (int i = 0; i < 16; i++) {
            if ((i & maskLeftWin) > 0 && (i & maskLeftLose) == 0) {
                continue;
            }
            if ((i & maskRightWin) > 0 && (i & maskRightLose) == 0) {
                continue;
            }
            for (int j = 0; j < 3; j++) {
                ans += segs[j].cnts[i];
            }
        }
        return ans;
    }

    public int typeOf(char c) {
        return c == 'R' ? 0 : c == 'P' ? 1 : 2;
    }

    //can a win b
    public boolean win(int a, int b) {
        return (b + 1) % 3 == a;
    }


    int maskLeftWin = 1 << 0;
    int maskLeftLose = 1 << 1;
    int maskRightWin = 1 << 2;
    int maskRightLose = 1 << 3;

    //mask of a for b
    public int maskFor(int a, int b, boolean left) {
        if (a == b) {
            return 0;
        }
        if (win(a, b)) {
            return left ? maskLeftWin : maskRightWin;
        }
        return left ? maskLeftLose : maskRightLose;
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static int statusNum = 1 << 4;
    private static int[] buf = new int[statusNum];
    int[] cnts = new int[statusNum];
    int setBit;
    int removeBit;

    public void swap() {
        int[] tmp = buf;
        buf = cnts;
        cnts = tmp;
    }

    public void replace(int s, int c) {
        Arrays.fill(cnts, 0);
        cnts[s] = c;
    }

    public void setBit(int s) {
        setBit = Bits.merge(setBit, s);
        removeBit = Bits.differ(removeBit, s);
        Arrays.fill(buf, 0);
        for (int j = 0; j < statusNum; j++) {
            buf[Bits.merge(j, s)] += cnts[j];
        }
        swap();
    }

    public void removeBit(int s) {
        setBit = Bits.differ(setBit, s);
        removeBit = Bits.merge(removeBit, s);
        Arrays.fill(buf, 0);
        for (int j = 0; j < statusNum; j++) {
            buf[Bits.differ(j, s)] += cnts[j];
        }
        swap();
    }

    public void pushUp() {
        for (int i = 0; i < statusNum; i++) {
            cnts[i] = left.cnts[i] + right.cnts[i];
        }
    }

    public void pushDown() {
        if (setBit != 0) {
            left.setBit(setBit);
            right.setBit(setBit);
            setBit = 0;
        }
        if (removeBit != 0) {
            left.removeBit(removeBit);
            right.removeBit(removeBit);
            removeBit = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updateReplace(int ll, int rr, int l, int r, int s, int c) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            replace(s, c);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateReplace(ll, rr, l, m, s, c);
        right.updateReplace(ll, rr, m + 1, r, s, c);
        pushUp();
    }

    public void updateSetBit(int ll, int rr, int l, int r, int s) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            setBit(s);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateSetBit(ll, rr, l, m, s);
        right.updateSetBit(ll, rr, m + 1, r, s);
        pushUp();
    }

    public void updateRemoveBit(int ll, int rr, int l, int r, int s) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            removeBit(s);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateRemoveBit(ll, rr, l, m, s);
        right.updateRemoveBit(ll, rr, m + 1, r, s);
        pushUp();
    }
}
