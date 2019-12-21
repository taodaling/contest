package template.primitve.generated;

public class DoubleDequeImpl implements DoubleDeque{
    private double[] data;
    private int bpos;
    private int epos;
    private static final double[] EMPTY = new double[0];
    private int n;

    public DoubleDequeImpl(int cap) {
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new double[cap];
        }
        bpos = 0;
        epos = 0;
        n = cap;
    }

    private void expandSpace(int len) {
        while (n < len) {
            n = Math.max(n + 10, n * 2);
        }
        double[] newData = new double[n];
        if (bpos <= epos) {
            if (bpos < epos) {
                System.arraycopy(data, bpos, newData, 0, epos - bpos);
            }
        } else {
            System.arraycopy(data, bpos, newData, 0, data.length - bpos);
            System.arraycopy(data, 0, newData, data.length - bpos, epos);
        }
        epos = size();
        bpos = 0;
        data = newData;
    }

    public DoubleIterator iterator() {
        return new DoubleIterator() {
            int index = bpos;

            @Override
            public boolean hasNext() {
                return index != epos;
            }

            @Override
            public double next() {
                double ans = data[index];
                index = DoubleDequeImpl.this.next(index);
                return ans;
            }
        };
    }

    public double removeFirst(){
        double ans = data[bpos];
        bpos = next(bpos);
        return ans;
    }

    public double removeLast(){
        double ans = data[last(epos)];
        epos = last(epos);
        return ans;
    }

    public void addLast(double x) {
        ensureMore();
        data[epos] = x;
        epos = next(epos);
    }

    public void addFirst(double x) {
        ensureMore();
        bpos = last(bpos);
        data[bpos] = x;
    }

    public double peekFirst() {
        return data[bpos];
    }

    public double peekLast() {
        return data[last(epos)];
    }

    public void clear() {
        bpos = epos = 0;
    }

    private int last(int x) {
        return (x == 0 ? n : x) - 1;
    }

    private int next(int x) {
        return x + 1 >= n ? 0 : x + 1;
    }

    private void ensureMore() {
        if (next(epos) == bpos) {
            expandSpace(n + 1);
        }
    }

    public int size() {
        int ans = epos - bpos;
        if (ans < 0) {
            ans += data.length;
        }
        return ans;
    }

    public boolean isEmpty() {
        return bpos == epos;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (DoubleIterator iterator = iterator(); iterator.hasNext(); ) {
            builder.append(iterator.next()).append(' ');
        }
        return builder.toString();
    }
}
