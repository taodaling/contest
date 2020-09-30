package template.primitve.generated.datastructure;

public class IntegerVersionArray {
    int[] data;
    int[] version;
    int now;
    IntToIntegerFunction def;

    public IntegerVersionArray(int cap) {
        this(cap, i -> 0);
    }

    public IntegerVersionArray(int cap, IntToIntegerFunction def) {
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
            data[i] = def.apply(i);
        }
    }

    public void set(int i, int v) {
        version[i] = now;
        data[i] = v;
    }

    public int modify(int i, int x) {
        visit(i);
        return data[i] = data[i] + x;
    }

    public int get(int i) {
        visit(i);
        return data[i];
    }

    public int inc(int i) {
        visit(i);
        return ++data[i];
    }

    public int dec(int i) {
        visit(i);
        return --data[i];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (version[i] < now) {
                continue;
            }
            builder.append(i).append(':').append(data[i]).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}