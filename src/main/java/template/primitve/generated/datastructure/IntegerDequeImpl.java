package template.primitve.generated.datastructure;


public class IntegerDequeImpl implements IntegerDeque {
    private int[] data;
    private int bpos;
    private int epos;
    private static final int[] EMPTY = new int[0];
    private int n;

    public IntegerDequeImpl(int cap) {
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new int[cap];
        }
        bpos = 0;
        epos = 0;
        n = cap;
    }

    private void expandSpace(int len) {
        while (n < len) {
            n = Math.max(n + 10, n * 2);
        }
        int[] newData = new int[n];
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

    public IntegerIterator iterator() {
        return new IntegerIterator() {
            int index = bpos;

            @Override
            public boolean hasNext() {
                return index != epos;
            }

            @Override
            public int next() {
                int ans = data[index];
                index = IntegerDequeImpl.this.next(index);
                return ans;
            }
        };
    }

    public IntegerIterator reverseIterator() {
        return new IntegerIterator() {
            int index = epos;

            @Override
            public boolean hasNext() {
                return index != bpos;
            }

            @Override
            public int next() {
                index = last(index);
                return data[index];
            }
        };
    }


    public int removeFirst(){
        int ans = data[bpos];
        bpos = next(bpos);
        return ans;
    }

    public int removeLast(){
        int ans = data[last(epos)];
        epos = last(epos);
        return ans;
    }

    public void addLast(int x) {
        ensureMore();
        data[epos] = x;
        epos = next(epos);
    }

    public void addFirst(int x) {
        ensureMore();
        bpos = last(bpos);
        data[bpos] = x;
    }

    public int peekFirst() {
        return data[bpos];
    }

    public int peekLast() {
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
        for (IntegerIterator iterator = iterator(); iterator.hasNext(); ) {
            builder.append(iterator.next()).append(' ');
        }
        return builder.toString();
    }
}
