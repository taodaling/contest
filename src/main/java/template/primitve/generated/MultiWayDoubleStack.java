package template.primitve.generated;

import java.util.Arrays;

public class MultiWayDoubleStack {
    private double[] values;
    private int[] next;
    private int[] heads;
    private int alloc;
    private int stackNum;

    public DoubleIterator iterator(final int queue) {
        return new DoubleIterator() {
            int ele = heads[queue];

            @Override
            public boolean hasNext() {
                return ele != 0;
            }

            @Override
            public double next() {
                double ans = values[ele];
                ele = next[ele];
                return ans;
            }
        };
    }

    public DoubleStack getStack(int qId){
        return new DoubleStack() {
            @Override
            public void addLast(double x) {
                MultiWayDoubleStack.this.addLast(qId, x);
            }

            @Override
            public double removeLast() {
                return MultiWayDoubleStack.this.removeLast(qId);
            }

            @Override
            public double peekLast() {
                return MultiWayDoubleStack.this.peekLast(qId);
            }

            @Override
            public DoubleIterator iterator() {
                return MultiWayDoubleStack.this.iterator(qId);
            }

            @Override
            public boolean isEmpty() {
                return MultiWayDoubleStack.this.isEmpty(qId);
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

    public int stackNumber() {
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

    public MultiWayDoubleStack(int qNum, int totalCapacity) {
        values = new double[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        heads = new int[qNum];
        stackNum = qNum;
    }

    public void addLast(int qId, double x) {
        alloc();
        values[alloc] = x;
        next[alloc] = heads[qId];
        heads[qId] = alloc;
    }


    public double peekLast(int qId) {
        return values[heads[qId]];
    }


    public double removeLast(int qId) {
        double ans = values[heads[qId]];
        heads[qId] = next[heads[qId]];
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < stackNum; i++){
            builder.append(i).append(": ");
            for(DoubleIterator iterator = iterator(i); iterator.hasNext(); ){
                builder.append(iterator.next()).append(",");
            }
            if(builder.charAt(builder.length() - 1) == ','){
                builder.setLength(builder.length() - 1);
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
