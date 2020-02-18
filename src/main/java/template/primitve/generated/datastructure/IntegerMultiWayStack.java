package template.primitve.generated.datastructure;


import java.util.Arrays;

public class IntegerMultiWayStack {
    private int[] values;
    private int[] next;
    private int[] heads;
    private int alloc;
    private int stackNum;

    public IntegerIterator iterator(final int queue) {
        return new IntegerIterator() {
            int ele = heads[queue];

            @Override
            public boolean hasNext() {
                return ele != 0;
            }

            @Override
            public int next() {
                int ans = values[ele];
                ele = next[ele];
                return ans;
            }
        };
    }

    public IntegerStack getStack(int qId){
        return new IntegerStack() {
            @Override
            public void addLast(int x) {
                IntegerMultiWayStack.this.addLast(qId, x);
            }

            @Override
            public int removeLast() {
                return IntegerMultiWayStack.this.removeLast(qId);
            }

            @Override
            public int peekLast() {
                return IntegerMultiWayStack.this.peekLast(qId);
            }

            @Override
            public IntegerIterator iterator() {
                return IntegerMultiWayStack.this.iterator(qId);
            }

            @Override
            public boolean isEmpty() {
                return IntegerMultiWayStack.this.isEmpty(qId);
            }

            @Override
            public void clear() {
                heads[qId] = 0;
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

    public IntegerMultiWayStack(int qNum, int totalCapacity) {
        values = new int[totalCapacity + 1];
        next = new int[totalCapacity + 1];
        heads = new int[qNum];
        stackNum = qNum;
    }

    public void addLast(int qId, int x) {
        alloc();
        values[alloc] = x;
        next[alloc] = heads[qId];
        heads[qId] = alloc;
    }


    public int peekLast(int qId) {
        return values[heads[qId]];
    }


    public int removeLast(int qId) {
        int ans = values[heads[qId]];
        heads[qId] = next[heads[qId]];
        return ans;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < stackNum; i++){
            builder.append(i).append(": ");
            for(IntegerIterator iterator = iterator(i); iterator.hasNext(); ){
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
