package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        IntegerList list = new IntegerList();
        while (n > 0) {
            int i;
            for (i = 0; i < list.size(); i++) {
                if (n < (1L << (i + 1))) {
                    break;
                }
            }
            n -= 1L << i;
            list.insert(i, list.size() + 1);
        }

        out.println(list.size() * 2);
        for (int i = 1; i <= list.size(); i++) {
            out.append(i).append(' ');
        }
        for(int i = 0; i < list.size(); i++){
            out.append(list.get(i)).append(' ');
        }
    }
}
