package on2020_09.on2020_09_21_Codeforces___Codeforces_Round__356__Div__1_.B__Bear_and_Tower_of_Cubes;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;
import template.utils.Debug;

public class BBearAndTowerOfCubes {
    public long pow3(long x) {
        return x * x * x;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long m = in.readLong();

        int maxSize = 0;
        long ps = 0;
        IntegerArrayList list = new IntegerArrayList();
        for (int i = 1; i <= 1e5; i++) {
            while (ps + pow3(i) < Math.min(m + 1, pow3(i + 1))) {
                ps += pow3(i);
                maxSize++;
                list.add(i);
            }
        }

        long tailSum = 0;
        long lastTail = (int) 1e6;
        long threshold = m;
        LongArrayList used = new LongArrayList();
        while (!list.isEmpty()) {
            long tail = list.pop();
            ps -= pow3(tail);
            while (tail < lastTail && ps + pow3(tail + 1) < Math.min(pow3(tail + 2), threshold + 1)) {
                if (tail + 1 == lastTail) {
                    if (ps + tailSum + pow3(tail + 1) < pow3(tail + 2)) {
                        tail++;
                    }
                    break;
                } else {
                    tail++;
                }
            }
            threshold -= pow3(tail);
            if (tail != lastTail) {
                lastTail = tail;
                tailSum = 0;
            }

            tailSum += pow3(tail);
            used.add(tail);
        }

        long maxSum = m - threshold;
        //for(int i = )

        debug.debug("maxSum", parse(maxSum));
        debug.debug("used", used);
        out.println(maxSize).println(maxSum);
    }

    Debug debug = new Debug(true);
    public LongArrayList parse(long x) {
        LongArrayList list = new LongArrayList();
        for (int i = (int) 1e5; i >= 1; i--) {
            long i3 = pow3(i);
            while (x >= i3) {
                x -= i3;
                list.add(i);
            }
        }
        return list;
    }

}
