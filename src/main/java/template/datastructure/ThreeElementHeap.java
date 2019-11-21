package template.datastructure;

import java.util.Arrays;

public class ThreeElementHeap {
    long[] abc = new long[3];

    public void add(long x) {
        if (abc[0] < x) {
            abc[0] = x;
        }
        if (abc[1] < x) {
            abc[0] = abc[1];
            abc[1] = x;
        }
        if (abc[2] < x) {
            abc[1] = abc[2];
            abc[2] = x;
        }
    }

    public long max(long x) {
        if (abc[2] != x) {
            return abc[2];
        }
        return abc[1];
    }

    public long max() {
        return abc[2];
    }

    public long min() {
        return abc[0];
    }

    public long second() {
        return abc[1];
    }

    public long min(long x) {
        if (abc[0] != x) {
            return abc[0];
        }
        return abc[1];
    }

    public long second(long x) {
        int emit = 0;
        boolean find = false;
        for (int i = 2; ; i--) {
            if (abc[i] == x && !find) {
                find = true;
                continue;
            }
            if (emit == 0) {
                emit++;
                continue;
            }
            return abc[i];
        }
    }

    public void clear(){
        Arrays.fill(abc, Long.MIN_VALUE);
    }

    @Override
    public String toString() {
        return Arrays.toString(abc);
    }
}