package template;

public class VersionArray {
    int[] data;
    int[] version;
    int now;

    public VersionArray(int cap) {
        data = new int[cap];
        version = new int[cap];
        now = 0;
    }

    public void clear() {
        now++;
    }

    public void visit(int i) {
        if (version[i] < now) {
            version[i] = now;
            data[i] = 0;
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
}