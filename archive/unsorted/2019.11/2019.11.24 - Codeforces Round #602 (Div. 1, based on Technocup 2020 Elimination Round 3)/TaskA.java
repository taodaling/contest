package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        char[] seq = new char[n];
        char[] state = new char[n];
        Arrays.fill(state, ')');
        for(int i = 0; i < 2 * (k - 1); i++){
            state[i] = i % 2 == 0 ? '(' : ')';
        }
        for(int i = 0; i < (n - 2 * (k - 1)) / 2; i++){
            state[i + 2 * (k - 1)] = '(';
        }
        in.readString(seq, 0);
        List<int[]> ans = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            if (seq[i] == state[i]) {
                continue;
            }
            for (int j = i + 1; j < n; j++) {
                if (state[i] == seq[j]) {
                    ans.add(SequenceUtils.wrapArray(i, j));
                    SequenceUtils.reverse(seq, i, j);
                    break;
                }
            }
        }

        out.println(ans.size());
        for(int[] pair : ans){
            out.append(pair[0] + 1).append(' ').append(pair[1] + 1).append('\n');
        }
    }
}
