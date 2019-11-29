package on2019_11.on2019_11_29_Educational_Codeforces_Round_16.LUOGU3378;



import template.datastructure.PairingHeap;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;

public class LUOGU3378 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        PairingHeap<Integer> heap = PairingHeap.NIL;
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            int t = in.readInt();
            if (t == 1) {
                heap = PairingHeap.merge(heap, new PairingHeap<>(in.readInt()), Comparator.naturalOrder());
            } else if (t == 2) {
                out.println(PairingHeap.peek(heap));
            } else {
                heap = PairingHeap.pop(heap, Comparator.naturalOrder());
            }
        }
    }

}
