package on2019_12.on2019_12_29_Good_Bye_2019.D__Strange_Device;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerEntryIterator;
import template.primitve.generated.IntegerHashMap;

public class DStrangeDevice {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        IntegerHashMap map = new IntegerHashMap(n, true);
        for (int i = 1; i <= k + 1; i++) {
            out.append("? ");
            for (int j = 1; j <= k + 1; j++) {
                if (j == i) {
                    continue;
                }
                out.append(j).append(' ');
            }
            out.println();
            out.flush();
            int index = in.readInt();
            int val = in.readInt();
            map.put(val, map.getOrDefault(val, 0) + 1);
        }

        int max = -1;
        int ans = 0;
        for (IntegerEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            if (iterator.getEntryKey() > max) {
                max = iterator.getEntryKey();
                ans = iterator.getEntryValue();
            }
        }

        out.append("! ").println(ans);
        out.flush();
    }
}
