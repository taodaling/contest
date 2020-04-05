package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class ESAbATAd {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int t = in.readInt();
        int n = in.readInt();
        this.in = in;
        this.out = out;
        while (t-- > 0) {
            debug.debug("t", t);
            solve(n);
            out.flush();
            if (in.readChar() != 'Y') {
                throw new RuntimeException();
            }
        }
    }

    FastInput in;
    FastOutput out;

    public int ask(int x) {
        out.println(x + 1);
        out.flush();
        cnt++;
        return in.readInt();
    }

    public void prepare() {
        if (cnt % 10 == 0) {
            int sameVal = same != -1 ? vals[same] : -1;
            int differVal = differ != -1 ? vals[differ] : -1;

            if (same != -1) {
                if (sameVal != ask(same)) {
                    for (int i = 0; i < vals.length; i++) {
                        vals[i] = 1 - vals[i];
                    }
                    differVal = 1 - differVal;
                }
            }
            if (differ != -1) {
                if (differVal != ask(differ)) {
                    for (int i = 0; i < vals.length; i++) {
                        int j = mirror(i, vals.length);
                        if (j <= i) {
                            continue;
                        }
                        SequenceUtils.swap(vals, i, j);
                        SequenceUtils.swap(know, i, j);
                    }
                }
            }
        }
    }

    public int mirror(int i, int n) {
        return n - 1 - i;
    }

    int[] vals;
    boolean[] know;
    int cnt;
    int differ;
    int same;

    public void solve(int n) {
        vals = new int[n];
        know = new boolean[n];
        cnt = 0;
        differ = -1;
        same = -1;
        while (true) {
            prepare();
            int first = -1;
            int last = -1;
            for (int j = 0; j < n; j++) {
                if (first == -1 && !know[j]) {
                    first = j;
                }
                if (!know[j]) {
                    last = j;
                }
            }

            if (first == -1) {
                break;
            }

            int index = first <= (n - 1 - last) ? first : last;
            vals[index] = ask(index);
            know[index] = true;
            int mi = mirror(index, n);
            if (know[mi]) {
                if (vals[index] == vals[mi]) {
                    same = index;
                } else {
                    differ = index;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            out.append(vals[i]);
        }
        out.println();
    }
}
