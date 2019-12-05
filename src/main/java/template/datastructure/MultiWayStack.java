package template.datastructure;

import template.utils.RevokeIterator;

import java.util.Arrays;
import java.util.Iterator;

public class MultiWayStack<T> {
    private Object[] values;
    private int[] next;
    private int[] heads;
    private int alloc;
    private int stackNum;

    public RevokeIterator<T> iterator(final int queue) {
        return new RevokeIterator<T>() {
            int ele = heads[queue];
            int pre = 0;

            @Override
            public boolean hasNext() {
                return ele != 0;
            }

            @Override
            public T next() {
                T ans = (T) values[ele];
                pre = ele;
                ele = next[ele];
                return ans;
            }

            @Override
            public void revoke() {
                ele = pre;
                pre = 0;
            }
        };
    }

    public SimplifiedStack<T> getStack(int qId) {
        return new SimplifiedStack<T>() {
            @Override
            public boolean isEmpty() {
                return MultiWayStack.this.isEmpty(qId);
            }

            @Override
            public T peekLast() {
                return MultiWayStack.this.peekLast(qId);
            }

            @Override
            public void addLast(T x) {
                MultiWayStack.this.addLast(qId, x);
            }

            @Override
            public T removeLast() {
                return MultiWayStack.this.removeLast(qId);
            }

            @Override
            public Iterator<T> iterator() {
                return MultiWayStack.this.iterator(qId);
            }
        };
    }

    private void doubleCapacity() {
        int newSize = Math.max(next.length + 10, next.length * 2);
        next = Arrays.copyOf(next, newSize);
        values = Arrays.copyOf(values, newSize);
    }

    public void alloc() {
        alloc++;
        if (alloc >= next.length) {
            doubleCapacity();
        }
        next[alloc] = 0;
    }

    public int numberOfQueue() {
        return stackNum;
    }

    public void clear() {
        alloc = 0;
        Arrays.fill(heads, 0, stackNum, 0);
    }

    public boolean isEmpty(int qId) {
        return heads[qId] == 0;
    }

    public void expandStackNum(int qNum) {
        if (qNum <= stackNum) {
        } else if (qNum <= heads.length) {
            Arrays.fill(heads, stackNum, qNum, 0);
        } else {
            Arrays.fill(heads, stackNum, heads.length, 0);
            heads = Arrays.copyOf(heads, qNum);
        }
        stackNum = qNum;
    }

    public MultiWayStack(int qNum, int totalCapacity) {
        values = new Object[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        heads = new int[qNum];
        stackNum = qNum;
    }

    public void addLast(int qId, T x) {
        alloc();
        values[alloc] = x;
        next[alloc] = heads[qId];
        heads[qId] = alloc;
    }


    public T peekLast(int qId) {
        return (T) values[heads[qId]];
    }


    public T removeLast(int qId) {
        T ans = (T) values[heads[qId]];
        heads[qId] = next[heads[qId]];
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stackNum; i++) {
            builder.append(i).append(": ");
            for (Iterator iterator = iterator(i); iterator.hasNext(); ) {
                builder.append(iterator.next()).append(",");
            }
            if (builder.charAt(builder.length() - 1) == ',') {
                builder.setLength(builder.length() - 1);
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
