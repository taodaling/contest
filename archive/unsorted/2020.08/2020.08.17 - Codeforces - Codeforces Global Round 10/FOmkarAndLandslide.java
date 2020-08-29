package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class FOmkarAndLandslide {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] h = new long[n];
        in.populate(h);

        Hill hill = new Hill(h[0]);
        for (int i = 1; i < n; i++) {
            hill.add(h[i]);
        }

        for (int i = 0; i < n; i++) {
            out.append(hill.getHeight(i)).append(' ');
        }
        out.println();
    }
}


class Hill {
    int width;
    long h0;
    int pend;

    public Hill(long h) {
        width = 1;
        h0 = h + 1;
        pend = 0;
    }

    public long last() {
        return getHeight(width - 1);
    }

    public int pendRemain() {
        return width - pend;
    }

    public void add(long x) {
        if (x - last() == 0) {
            width++;
            if (pend == 0) {
                h0--;
            }
            pend = width - 1;
            return;
        }
        if (x - last() == 1) {
            width++;
            return;
        }
        if (x - last() == 2) {
            if (pend == width - 1) {
                h0++;
            }
            pend = (pend + 1) % width;
            x--;
            add(x);
            return;
        }

        if (pend != 0) {
            if (pend == width - 1) {
                pend = 0;
                h0++;
                x--;
                add(x);
                return;
            }
            int remain = pendRemain() - 1;
            long able = x - last() - 1;
            int provide = (int) Math.min(remain, able);
            pend += provide;
            x -= provide;
            add(x);
            return;
        }

        //pend == 0
        //fast
        //last() + r < x - width * r
        //(width + 1) * r < x - last();
        long r = DigitUtils.maximumIntegerLessThanDiv(x - last(), width + 1);
        if (r > 0) {
            x -= width * r;
            h0 += r;
            add(x);
            return;
        }

        //r is zero
        int remain = pendRemain() - 1;
        long able = x - last() - 1;
        int provide = (int) Math.min(remain, able);
        pend += provide;
        x -= provide;
        add(x);
        return;
    }

    public long getHeight(int i) {
        return h0 + i + (pend > i ? 0 : -1);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < width; i++) {
            builder.append(getHeight(i)).append(", ");
        }
        return builder.toString();
    }
}