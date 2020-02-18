package template.primitve.datastructure;

public class LongVersionArray {
    long[] data;
    int[] version;
    int now;
    long def;

    public LongVersionArray(int cap) {
        this(cap, 0);
    }

    public LongVersionArray(int cap, long def) {
        data = new long[cap];
        version = new int[cap];
        now = 0;
        this.def = def;
    }

    public void clear() {
        now++;
    }

    public void visit(int i) {
        if (version[i] < now) {
            version[i] = now;
            data[i] = def;
        }
    }

    public void set(int i, long v) {
        version[i] = now;
        data[i] = v;
    }

    public long modify(int i, long x) {
        visit(i);
        return data[i] = data[i] + x;
    }

    public long get(int i) {
        visit(i);
        return data[i];
    }

    public long inc(int i) {
        visit(i);
        return ++data[i];
    }

    public long dec(int i) {
        visit(i);
        return --data[i];
    }
}