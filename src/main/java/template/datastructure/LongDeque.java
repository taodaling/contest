package template.datastructure;

public class LongDeque {
    long[] data;
    int bpos;
    int epos;
    int cap;

    public int getCapacity() {
        return cap;
    }

    public LongDeque(int cap) {
        this.cap = cap + 1;
        this.data = new long[this.cap];
    }

    public int size() {
        int s = epos - bpos;
        if (s < 0) {
            s += cap;
        }
        return s;
    }

    public boolean isEmpty() {
        return epos == bpos;
    }

    public long peekFirst() {
        return data[bpos];
    }

    private int last(int i) {
        return (i == 0 ? cap : i) - 1;
    }

    private int next(int i) {
        int n = i + 1;
        return n == cap ? 0 : n;
    }

    public long peekLast() {
        return data[last(epos)];
    }

    public long removeFirst() {
        int t = bpos;
        bpos = next(bpos);
        return data[t];
    }

    public long removeLast() {
        return data[epos = last(epos)];
    }

    public void addLast(long val) {
        data[epos] = val;
        epos = next(epos);
    }

    public void addFirst(long val) {
        data[bpos = last(bpos)] = val;
    }

    public void clear() {
        bpos = epos = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = bpos; i != epos; i = next(i)) {
            builder.append(data[i]).append(' ');
        }
        return builder.toString();
    }
}