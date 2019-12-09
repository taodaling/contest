package contest;



import template.datastructure.IntervalBooleanMap;
import template.io.FastInput;
import template.io.FastOutput;

public class IntervalBooleanMapTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        IntervalBooleanMap map = new IntervalBooleanMap();
        for (int i = 0; i < q; i++) {
            int cmd = in.readInt();
            if (cmd == 0) {
                int l = in.readInt();
                int r = in.readInt();
                map.setTrue(l, r);
            } else if (cmd == 1) {
                int l = in.readInt();
                int r = in.readInt();
                map.setFalse(l, r);
            } else {
                out.println(map.countTrue());
            }
        }
    }
}
