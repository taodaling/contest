package on2019_11.on2019_11_24_Technocup_2020___Elimination_Round_3.G___Not_Same;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            sum += a[i];
        }
        int[] index = new int[n];
        for (int i = 0; i < n; i++) {
            index[i] = i;
        }
        Randomized.randomizedArray(index, 0, n);
        CompareUtils.quickSort(index, (x, y) -> -(a[x] - a[y]), 0, n);
        int[] invIndex = new int[n];
        for (int i = 0; i < n; i++) {
            invIndex[index[i]] = i;
        }

        int[] b = a.clone();
        Randomized.randomizedArray(b, 0, n);
        Arrays.sort(b);
        SequenceUtils.reverse(b, 0, n - 1);

        int tail = 0;
        int head = n - 1;
        int pivot = -1;
        boolean firstTime = true;
        List<char[]> ans = new ArrayList<>();
        while (b[0] > 0) {
            while (tail + 1 < n && b[tail] == b[tail + 1]) {
                tail++;
            }
//            if (firstTime) {
//                firstTime = false;
//                char[] seg = new char[n];
//                Arrays.fill(seg, 0, tail + 1, '1');
//                Arrays.fill(seg, tail + 1, n, '0');
//                for (int i = 0; i <= tail; i++) {
//                    b[i]--;
//                }
//                ans.add(seg);
//                continue;
//            }
            if (head > tail) {
                char[] seg = new char[n];
                Arrays.fill(seg, 0, tail + 1, '1');
                Arrays.fill(seg, tail + 1, n, '0');
                seg[head] = '1';
                for (int i = 0; i <= tail; i++) {
                    b[i]--;
                }
                b[head]--;
                ans.add(seg);
                head--;
                continue;
            }
            pivot++;
            if(pivot >= n || b[pivot] == 0){
                char[] seg = new char[n];
                Arrays.fill(seg, 0, pivot, '1');
                Arrays.fill(seg, pivot, n, '0');
                for (int i = 0; i < pivot; i++) {
                    b[i]--;
                }
                ans.add(seg);
                continue;
            }

            char[] seg = new char[n];
            Arrays.fill(seg, 0, tail + 1, '1');
            Arrays.fill(seg, tail + 1, n, '0');
            seg[pivot] = '0';
            for (int i = 0; i <= tail; i++) {
                b[i]--;
            }
            b[pivot]++;
            ans.add(seg);
        }

        out.println(ans.size());
        for (char[] s : ans) {
            for (int i = 0; i < n; i++) {
                out.append(s[invIndex[i]]);
            }
            out.println();
        }
    }
}
