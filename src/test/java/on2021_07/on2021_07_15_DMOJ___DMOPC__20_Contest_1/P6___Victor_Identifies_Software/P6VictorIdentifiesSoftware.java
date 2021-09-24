package on2021_07.on2021_07_15_DMOJ___DMOPC__20_Contest_1.P6___Victor_Identifies_Software;



import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.UpdatableSum;
import template.utils.Update;

public class P6VictorIdentifiesSoftware {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        SumImpl zero = new SumImpl();
        SumImpl sum = new SumImpl();
        UpdateImpl upd = new UpdateImpl();
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.size = 1;
                    ans.containA = true;
                    upd.as(true, false, a[i]);
                    ans.update(upd);
                    return ans;
                });
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            sum.copy(zero);
            if (t == 1) {
                upd.as(true, false, UpdateImpl.NOT_SET);
            } else if (t == 2) {
                upd.as(false, true, UpdateImpl.NOT_SET);
            } else if (t == 3) {
                upd.as(false, false, in.ri());
            } else if (t == 4) {
                st.query(l, r, 0, n - 1, sum);
                if (!sum.containA) {
                    out.println("breaks galore");
                } else {
                    if (sum.rangeAMax < 0) {
                        out.println(sum.rangeAMax);
                    } else {
                        out.println(sum.maxA);
                    }
                }
            } else if (t == 5) {
                st.query(l, r, 0, n - 1, sum);
                if (!sum.containB) {
                    out.println("breaks galore");
                } else {
                    if (sum.rangeBMax < 0) {
                        out.println(sum.rangeBMax);
                    } else {
                        out.println(sum.maxB);
                    }
                }
            }
            if (t <= 3) {
                st.update(l, r, 0, n - 1, upd);
            }
        }
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    static final int NOT_SET = Integer.MIN_VALUE;
    boolean setA;
    boolean setB;
    long set;

    public void as(boolean setA, boolean setB, long set) {
        this.setA = setA;
        this.setB = setB;
        this.set = set;
    }

    @Override
    public void update(UpdateImpl update) {
        if (update.setA) {
            setA = true;
            setB = false;
        }
        if (update.setB) {
            setA = false;
            setB = true;
        }
        if (update.set != NOT_SET) {
            set = update.set;
        }
    }

    @Override
    public void clear() {
        setA = setB = false;
        set = NOT_SET;
    }

    @Override
    public boolean ofBoolean() {
        return setA || setB || set != NOT_SET;
    }
}

class SumImpl implements UpdatableSum<SumImpl, UpdateImpl> {
    long onlyAPrefix;
    long onlyBPrefix;
    long onlyASuffix;
    long onlyBSuffix;
    long maxA;
    long maxB;
    long total;
    boolean containA;
    boolean containB;
    long maxPref;
    long maxSuf;
    long max;
    int size;
    int onlyASize;
    int onlyBSize;
    int onlyAPrefixSize;
    int onlyASuffixSize;
    int onlyBPrefixSize;
    int onlyBSuffixSize;
    int rangeAMax = UpdateImpl.NOT_SET;
    int rangeBMax = UpdateImpl.NOT_SET;

    @Override
    public void add(SumImpl right) {
        rangeAMax = Math.max(rangeAMax, right.rangeAMax);
        rangeBMax = Math.max(rangeBMax, right.rangeBMax);

        onlyASize = Math.max(onlyASize, right.onlyASize);
        onlyASize = Math.max(onlyASize, onlyASuffixSize + right.onlyAPrefixSize);
        onlyBSize = Math.max(onlyBSize, right.onlyBSize);
        onlyBSize = Math.max(onlyBSize, onlyBSuffixSize + right.onlyBPrefixSize);

        max = Math.max(max, right.max);
        max = Math.max(max, maxSuf + right.maxPref);
        maxPref = Math.max(maxPref, total + right.maxPref);
        maxSuf = Math.max(right.maxSuf, maxSuf + right.total);

        maxA = Math.max(maxA, right.maxA);
        maxA = Math.max(maxA, onlyASuffix + right.onlyAPrefix);
        maxB = Math.max(maxB, right.maxB);
        maxB = Math.max(maxB, onlyBSuffix + right.onlyBPrefix);
        if (!containB) {
            onlyAPrefix = Math.max(onlyAPrefix, total + right.onlyAPrefix);
            onlyAPrefixSize = size + right.onlyAPrefixSize;
        }
        if (!containA) {
            onlyBPrefix = Math.max(onlyBPrefix, total + right.onlyBPrefix);
            onlyBPrefixSize = size + right.onlyBPrefixSize;
        }
        if (!right.containB) {
            onlyASuffix = Math.max(right.onlyASuffix, right.total + onlyASuffix);
            onlyASuffixSize = Math.max(right.onlyASuffixSize, right.size + onlyASuffixSize);
        } else {
            onlyASuffix = right.onlyASuffix;
            onlyASuffixSize = right.onlyASuffixSize;
        }
        if (!right.containA) {
            onlyBSuffix = Math.max(right.onlyBSuffix, right.total + onlyBSuffix);
            onlyBSuffixSize = Math.max(right.onlyBSuffixSize, right.size + onlyBSuffixSize);
        } else {
            onlyBSuffix = right.onlyBSuffix;
            onlyBSuffixSize = right.onlyBSuffixSize;
        }
        total += right.total;
        size += right.size;
        containA = containA || right.containA;
        containB = containB || right.containB;
    }

    @Override
    public void copy(SumImpl right) {
        this.maxPref = right.maxPref;
        this.max = right.max;
        this.maxSuf = right.maxSuf;
        this.onlyASuffix = right.onlyASuffix;
        this.onlyAPrefix = right.onlyAPrefix;
        this.onlyBPrefix = right.onlyBPrefix;
        this.onlyBSuffix = right.onlyBSuffix;
        this.containA = right.containA;
        this.containB = right.containB;
        this.maxA = right.maxA;
        this.maxB = right.maxB;
        this.total = right.total;
        this.size = right.size;
        this.onlyASize = right.onlyASize;
        this.onlyBSize = right.onlyBSize;
        this.onlyAPrefixSize = right.onlyAPrefixSize;
        this.onlyASuffixSize = right.onlyASuffixSize;
        this.onlyBPrefixSize = right.onlyBPrefixSize;
        this.onlyBSuffixSize = right.onlyBSuffixSize;
        this.rangeBMax = right.rangeBMax;
        this.rangeAMax = right.rangeAMax;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public void update(UpdateImpl update) {
        if (update.set != UpdateImpl.NOT_SET) {
            this.total = update.set * size;
            this.rangeAMax = containA ? (int) update.set : UpdateImpl.NOT_SET;
            this.rangeBMax = containB ? (int) update.set : UpdateImpl.NOT_SET;
            if (update.set < 0) {
                this.maxPref = 0;
                this.max = 0;
                this.maxSuf = 0;
                this.onlyASuffix = 0;
                this.onlyAPrefix = 0;
                this.onlyBPrefix = 0;
                this.onlyBSuffix = 0;
                this.maxA = 0;
                this.maxB = 0;
            } else {
                this.maxPref = this.max = this.maxSuf = this.total;
                this.onlyASuffix = onlyASuffixSize * update.set;
                this.onlyAPrefix = onlyAPrefixSize * update.set;
                this.onlyBPrefix = onlyBPrefixSize * update.set;
                this.onlyBSuffix = onlyBSuffixSize * update.set;
                this.maxA = onlyASize * update.set;
                this.maxB = onlyBSize * update.set;
            }
        }
        if (update.setA) {
            this.onlyASuffix = maxSuf;
            this.onlyAPrefix = maxPref;
            this.onlyBPrefix = 0;
            this.onlyBSuffix = 0;
            this.containA = true;
            this.containB = false;
            this.maxA = max;
            this.maxB = 0;
            this.onlyASize = size;
            this.onlyBSize = 0;
            this.onlyAPrefixSize = size;
            this.onlyASuffixSize = size;
            this.onlyBPrefixSize = 0;
            this.onlyBSuffixSize = 0;
            this.rangeAMax = Math.max(this.rangeAMax, this.rangeBMax);
            this.rangeBMax = UpdateImpl.NOT_SET;
        }
        if (update.setB) {
            this.onlyBSuffix = maxSuf;
            this.onlyBPrefix = maxPref;
            this.onlyAPrefix = 0;
            this.onlyASuffix = 0;
            this.containB = true;
            this.containA = false;
            this.maxB = max;
            this.maxA = 0;
            this.onlyBSize = size;
            this.onlyASize = 0;
            this.onlyBPrefixSize = size;
            this.onlyBSuffixSize = size;
            this.onlyAPrefixSize = 0;
            this.onlyASuffixSize = 0;
            this.rangeBMax = Math.max(this.rangeAMax, this.rangeBMax);
            this.rangeAMax = UpdateImpl.NOT_SET;
        }
    }
}