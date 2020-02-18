package template.primitve.generated.datastructure;

public class DoubleVersionArray {
    double[] data;
    int[] version;
    int now;
    double def;

    public DoubleVersionArray(int cap) {
        this(cap, 0);
    }

    public DoubleVersionArray(int cap, double def) {
        data = new double[cap];
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

    public void set(int i, double v) {
        version[i] = now;
        data[i] = v;
    }

    public double modify(int i, double x) {
        visit(i);
        return data[i] = data[i] + x;
    }

    public double get(int i) {
        visit(i);
        return data[i];
    }

    public double inc(int i) {
        visit(i);
        return ++data[i];
    }

    public double dec(int i) {
        visit(i);
        return --data[i];
    }
}