package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class BEquivalentStrings {
    static char[] a, b;

    public static boolean equal(int l, int r, int L, int R) {
        if ((r - l + 1) % 2 == 0) {
            int m1 = (l + r) / 2;
            int m2 = (L + R) / 2;
            Cond sol = new OrCond(
                    new AndCand(new EqualCand(l, m1, L, m2),
                            new EqualCand(m1 + 1, r, m2 + 1, R)),
                    new AndCand(new EqualCand(m1 + 1, r, L, m2),
                            new EqualCand(l, m1, m2 + 1, R))
            );
            return sol.apply();
        } else {
            return SequenceUtils.equal(a, l, r, b, L, R);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        a = in.rs().toCharArray();
        b = in.rs().toCharArray();
        out.println(equal(0, a.length - 1,
                0, b.length - 1) ? "YES" : "NO");
    }
}

interface Cond {
    boolean apply();
}

class OrCond implements Cond {
    Cond a, b;

    public OrCond(Cond a, Cond b) {
        if (RandomWrapper.INSTANCE.nextInt(0, 1) == 0) {
            Cond tmp = a;
            a = b;
            b = tmp;
        }
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean apply() {
        return a.apply() || b.apply();
    }
}

class AndCand implements Cond {
    Cond a, b;

    public AndCand(Cond a, Cond b) {
        if (RandomWrapper.INSTANCE.nextInt(0, 1) == 0) {
            Cond tmp = a;
            a = b;
            b = tmp;
        }
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean apply() {
        return a.apply() && b.apply();
    }
}

class EqualCand implements Cond {
    int l, r, L, R;

    public EqualCand(int l, int r, int l1, int r1) {
        this.l = l;
        this.r = r;
        L = l1;
        R = r1;
    }

    @Override
    public boolean apply() {
        return BEquivalentStrings.equal(l, r, L, R);
    }
}

