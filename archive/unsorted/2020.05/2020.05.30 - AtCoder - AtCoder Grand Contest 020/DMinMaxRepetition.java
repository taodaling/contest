package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class DMinMaxRepetition {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int d = in.readInt();

        Machine mac = new Machine(a, b);
        for (int i = c; i <= d; i++) {
            char ch = (char) mac.get(i);
            out.append(ch);
        }
        out.println();
    }
}


class Machine {
    int k;
    int p1;
    int t;
    int t2;

    private int atLeast(int a, int b) {
        if (b > a) {
            return DigitUtils.ceilDiv(b, a + 1);
        } else {
            return DigitUtils.ceilDiv(a, b + 1);
        }
    }

    private int atLeastBFirst(int a, int b) {
        if (b == 0) {
            return a;
        }
        if (b > a) {
            return atLeast(a, b);
        }
        return atLeastBFirst(a, b - 1);
    }

    public Machine(int a, int b) {
        //k * (a + 1) >= b
        k = atLeast(a, b);

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                mid = -mid;
                if (mid == 0) {
                    return true;
                }
                int ar = a - mid * k;
                int br = b - mid + 1;
                if (ar < 0 || br < 0) {
                    return false;
                }
                if (ar >= br) {
                    return true;
                }
                if (atLeastBFirst(ar, br) > k) {
                    return false;
                }
                return true;
            }
        };

        p1 = -ibs.binarySearch(-(a / k), 0);
        ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                mid = -mid;
                if (mid == 0) {
                    return true;
                }
                if (mid > k) {
                    return false;
                }
                int ar = a - p1 * k - mid;
                int br = b - p1;
                if (atLeastBFirst(ar, br) > k) {
                    return false;
                }
                return true;
            }
        };
        t = -ibs.binarySearch(-(a - p1 * k), 0);
        t2 = Math.max(0, b - p1 - k * (a - p1 * k - t));
    }

    public int get(int i) {
        if (i <= (k + 1) * p1) {
            return (i - 1) % (k + 1) < k ? 'A' : 'B';
        }
        i -= (k + 1) * p1;
        if (i <= t) {
            return 'A';
        }
        i -= t;
        if (i <= t2) {
            return 'B';
        }
        i -= t2;
        return (i - 1) % (k + 1) == 0 ? 'A' : 'B';
    }
}
