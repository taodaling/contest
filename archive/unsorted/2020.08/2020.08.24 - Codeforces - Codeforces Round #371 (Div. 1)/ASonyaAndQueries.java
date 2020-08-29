package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;

public class ASonyaAndQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int t = in.readInt();
        LongHashMap hm = new LongHashMap(t, false);
        char[] buf = new char[20];
        while (t-- > 0) {
            char c = in.readChar();
            if (c == '+') {
                long pattern = getPattern(in.readLong());
                hm.put(pattern, hm.get(pattern) + 1);
            }else if(c == '-'){
                long pattern = getPattern(in.readLong());
                hm.put(pattern, hm.get(pattern) - 1);
            }else{
                long pattern = in.readLong();
                out.println(hm.get(pattern));
            }
        }
    }

    public long getPattern(long x) {
        if (x == 0) {
            return 0;
        }
        return getPattern(x / 10) * 10 + (x % 10 % 2);
    }
}
