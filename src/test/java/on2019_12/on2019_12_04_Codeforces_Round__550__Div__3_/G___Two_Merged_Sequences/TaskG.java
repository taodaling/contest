package on2019_12.on2019_12_04_Codeforces_Round__550__Div__3_.G___Two_Merged_Sequences;



import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        IntList inc = new IntList();
        IntList dec = new IntList();
        for (int i = 0; i < n; i++) {
            boolean putInc = inc.isEmpty() ||
                    a[inc.tail()] < a[i];
            boolean putDec = dec.isEmpty() ||
                    a[dec.tail()] > a[i];
            if (putInc && putDec) {
                if (i + 1 < n && a[i] < a[i + 1]) {
                    inc.add(i);
                } else {
                    dec.add(i);
                }
            } else if (putInc) {
                inc.add(i);
            } else if (putDec) {
                dec.add(i);
            } else {
                out.println("NO");
                return;
            }
        }

        out.println("YES");
        inc.reverse();
        dec.reverse();
        for (int i = 0; i < n; i++) {
            if (dec.isEmpty() || (!inc.isEmpty() && inc.tail() == i)) {
                inc.pop();
                out.append(0).append(' ');
            }else{
                dec.pop();
                out.append(1).append(' ');
            }
        }
    }
}
