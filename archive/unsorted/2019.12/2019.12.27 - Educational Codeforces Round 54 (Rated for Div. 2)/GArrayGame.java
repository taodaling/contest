package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerBIT;
import template.utils.SequenceUtils;

public class GArrayGame {
    private static int m;
    private static int limit;
    private static int mask;
    private static int[] bitCount;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        m = in.readInt();
        int q = in.readInt();
        limit = 1 << m;
        mask = limit - 1;
        bitCount = new int[limit];
        for (int i = 0; i < limit; i++) {
            bitCount[i] = Integer.bitCount(i);
        }
        Status.buf = new int[limit];
        Status.buf2 = new int[limit];
        Status.self = new int[limit];
        for (int i = 0; i < limit; i++) {
            Status.self[i] = i;
        }

        int[] data = new int[n + 1];
       // IntegerBIT bit = new IntegerBIT(n + 1);
        for (int i = 1; i <= n; i++) {
            data[i] = 1 - (int) (in.readLong() % 2);
         //   bit.update(i, 1 - data[i]);
           // bit.update(i + 1, 1 - data[i]);
        }


        Status[] evenOrOdd = new Status[2];
        for (int i = 0; i < 2; i++) {
            evenOrOdd[i] = new Status();
            evenOrOdd[i].initAsSingle(i % 2 == 1);
        }
        Segment seg = new Segment(1, n, data, evenOrOdd);
        Status query = new Status();
        for (int i = 0; i < q; i++) {
            if (in.readInt() == 1) {
                int l = in.readInt();
                int r = in.readInt();
                int d = in.readInt();
                if (d % 2 == 0) {
                    continue;
                }
                seg.update(l, r, 1, n);
            //    bit.update(l, 1);
              //  bit.update(r + 1, 1);
            } else {
                int l = in.readInt();
                int r = in.readInt();
                //boolean odd = bit.query(l) % 2 == 1;
                query.init();
                seg.query(l, r, 1, n, query);
                int ans = query.func[mask];
                if (ans % 2 == 1) {
                    out.println(1);
                } else {
                    out.println(2);
                }
            }
        }
    }

    static class Status {
        int size;
        int[] func = new int[limit];
        static int[] buf;
        static int[] buf2;
        static int[] self;

        void init() {
            size = 0;
        }

        public void initAsSingle(boolean odd) {
            if (odd) {
                for (int i = 0; i < limit; i++) {
                    func[i] = ((i << 1) | 1) & mask;
                }
            } else {
                for (int i = 0; i < limit; i++) {
                    func[i] = ((i << 1) | (bitCount[i] < m ? 1 : 0)) & mask;
                }
            }
            size = 1;
        }

        public void apply(int[] input, int[] output) {
            for (int i = 0; i < limit; i++) {
                output[i] = func[input[i]];
            }
            if (size < m) {
                int bufMask = (1 << size) - 1;
                for (int i = 0; i < limit; i++) {
                    output[i] = ((output[i] & bufMask) | (input[i] << size)) & mask;
                }
            }
        }

        public void merge(Status x) {
            x.apply(self, buf);
            apply(buf, buf2);
            for (int i = 0; i < limit; i++) {
                func[i] = buf2[i];
            }
            size += x.size;
        }

        public void merge(Status a, Status b) {
            size = a.size;
            for (int i = 0; i < limit; i++) {
                func[i] = a.func[i];
            }
            merge(b);
        }
    }

    static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private Status[] status = new Status[2];
        private boolean rev;

        public void pushUp() {
            for (int i = 0; i < 2; i++) {
                status[i].merge(left.status[i], right.status[i]);
            }
        }

        public void rev() {
            SequenceUtils.swap(status, 0, 1);
            rev = !rev;
        }

        public void pushDown() {
            if (rev) {
                left.rev();
                right.rev();
                rev = false;
            }
        }

        public Segment(int l, int r, int[] val, Status[] evenOrOdd) {
            if (l < r) {
                for (int i = 0; i < 2; i++) {
                    status[i] = new Status();
                }
                int m = (l + r) >> 1;
                left = new Segment(l, m, val, evenOrOdd);
                right = new Segment(m + 1, r, val, evenOrOdd);
                pushUp();
            } else {
                status[0] = evenOrOdd[val[l] % 2];
                status[1] = evenOrOdd[1 - val[l] % 2];
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                rev();
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m);
            right.update(ll, rr, m + 1, r);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, Status x) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                x.merge(status[0]);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.query(ll, rr, l, m, x);
            right.query(ll, rr, m + 1, r, x);
        }
    }
}

