package contest;

import template.FastInput;
import template.FastOutput;
import template.Randomized;
import template.SequenceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.readInt();
        }

        int failCnt = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] > b[i]) {
                failCnt++;
            }
        }

        int[] oldA = a.clone();
        int[] oldB = b.clone();

        Randomized.randomizedArray(a, 0, n);
        Randomized.randomizedArray(b, 0, n);

        Arrays.sort(a);
        Arrays.sort(b);

        for (int i = 0; i < n; i++) {
            if (a[i] > b[i]) {
                out.println("No");
                return;
            }
        }

        boolean flexible = false;
        for (int i = 1; i < n; i++) {
            if (a[i] <= b[i - 1]) {
                out.println("Yes");
                return;
            }
        }

        Map<Integer, Integer> bIndex = new HashMap<>();
        for (int i = 0; i < n; i++) {
            bIndex.put(oldB[i], i);
        }

        int timeNeed = 0;
        for (int i = 0; i < n; i++) {
            int aRank = SequenceUtils.floorIndex(a, oldA[i], 0, n - 1);
            int bPos = bIndex.get(b[aRank]);
            if (bPos == i) {
                continue;
            }
            timeNeed++;
            SequenceUtils.swap(oldB, i, bPos);
            bIndex.put(oldB[i], i);
            bIndex.put(oldB[bPos], bPos);
        }
        if (timeNeed > n - 2) {
            out.println("No");
        } else {
            out.println("Yes");
        }
    }
}
