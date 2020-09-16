package on2020_09.on2020_09_13_Codeforces___Codeforces_Round__360__Div__1_.C__The_Values_You_Can_Make;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CTheValuesYouCanMake {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] c = new int[n];
        in.populate(c);
        boolean[] possible = new boolean[k + 1];
        possible[0] = possible[k] = true;
        long end = System.currentTimeMillis() + 1800;
        int[] pref = new int[k + 1];
        int[] post = new int[k + 1];
        BitSet last = new BitSet(k + 1);
        BitSet buf = new BitSet(k + 1);
        BitSet next = new BitSet(k + 1);
        while (System.currentTimeMillis() < end) {

            Randomized.shuffle(c);
            Arrays.fill(pref, n);
            Arrays.fill(post, -1);

            last.clear(0, k);
            last.set(0);
            for (int i = 0; i < n; i++) {
                int x = c[i];
                buf.copy(last);
                next.copy(last);
                last.rightShift(x);
                next.or(last);

                BitSet tmp = last;
                last = next;
                next = tmp;

                buf.xor(last);
                for (int j = buf.nextSetBit(0); j < buf.capacity(); j = buf.nextSetBit(j + 1)) {
                    pref[j] = Math.min(i, pref[j]);
                }
            }

            last.clear(0, k);
            last.set(0);
            for (int i = n - 1; i >= 0; i--) {
                int x = c[i];
                buf.copy(last);
                next.copy(last);
                last.rightShift(x);
                next.or(last);

                BitSet tmp = last;
                last = next;
                next = tmp;

                buf.xor(next);
                for (int j = buf.nextSetBit(0); j < buf.capacity(); j = buf.nextSetBit(j + 1)) {
                    post[j] = Math.max(post[j], i);
                }
            }

            for (int i = 1; i < k; i++) {
                if (pref[i] < post[k - i]) {
                    possible[i] = possible[k - i] = true;
                }
            }
        }

        IntegerArrayList list = new IntegerArrayList();
        for(int i = 0; i <= k; i++){
            if(possible[i]){
                list.add(i);
            }
        }

        out.println(list.size());
        for(int i = 0; i < list.size(); i++){
            out.append(list.get(i)).append(' ');
        }
    }
}
