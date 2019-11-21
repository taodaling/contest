package template.datastructure;

public class IntVersionArray {
    int[] data;
    int[] version;
    int now;
    int def;

    public IntVersionArray(int cap) {
        this(cap, 0);
    }

    public IntVersionArray(int cap, int def) {
        data = new int[cap];
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

    public void set(int i, int v) {
        version[i] = now;
        data[i] = v;
    }

    public int get(int i) {
        visit(i);
        return data[i];
    }

    public int inc(int i) {
        visit(i);
        return ++data[i];
    }

    public long dec(int i) {
        visit(i);
        return --data[i];
    }
}