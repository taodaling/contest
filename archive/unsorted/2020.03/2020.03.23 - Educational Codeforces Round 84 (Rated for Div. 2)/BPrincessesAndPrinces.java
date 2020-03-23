package contest;

import dp.Lis;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;

import java.util.ArrayList;
import java.util.List;

public class BPrincessesAndPrinces {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<Integer>[] lists = new List[n];
        for (int i = 0; i < n; i++) {
            int k = in.readInt();
            lists[i] = new ArrayList<>(k);
            for (int j = 0; j < k; j++) {
                lists[i].add(in.readInt() - 1);
            }
        }

        boolean[] married = new boolean[n];
        boolean[] girl = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (Integer cand : lists[i]) {
                if (married[cand]) {
                    continue;
                }
                married[cand] = true;
                girl[i] = true;
                break;
            }
        }

        int indexOfBoy = -1;
        int indexOfGirl = -1;
        for (int i = 0; i < n; i++) {
            if (!married[i]) {
                indexOfBoy = i;
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            if (!girl[i]) {
                indexOfGirl = i;
                break;
            }
        }

        if (indexOfBoy == -1) {
            out.println("OPTIMAL");
            return;
        }
        out.println("IMPROVE");
        out.append(indexOfGirl + 1).append(' ').append(indexOfBoy + 1).println();
    }
}
