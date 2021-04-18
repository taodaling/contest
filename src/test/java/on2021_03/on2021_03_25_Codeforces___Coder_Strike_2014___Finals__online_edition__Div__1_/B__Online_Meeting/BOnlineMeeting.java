package on2021_03.on2021_03_25_Codeforces___Coder_Strike_2014___Finals__online_edition__Div__1_.B__Online_Meeting;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BOnlineMeeting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        boolean[] occur = new boolean[n];
        int[] firstSign = new int[n];
        int[][] records = new int[m][2];
        for (int i = 0; i < m; i++) {
            records[i][0] = in.rc();
            records[i][1] = in.ri() - 1;
            if (firstSign[records[i][1]] == 0) {
                firstSign[records[i][1]] = records[i][0];
            }
            occur[records[i][1]] = true;
        }
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (firstSign[i] == '-') {
                cnt++;
            }
        }
        Set<Integer> possible = new HashSet<>(n);
        Set<Integer> impossible = new HashSet<>(n);
        for (int[] r : records) {
            if (r[0] == '-') {
                cnt--;
                if (cnt != 0) {
                    impossible.add(r[1]);
                } else {
                    possible.add(r[1]);
                }
            } else {
                cnt++;
                if (cnt != 1) {
                    impossible.add(r[1]);
                } else {
                    possible.add(r[1]);
                }
            }
        }
        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            if (!occur[i]) {
                list.add(i);
            }
        }
        if (possible.size() == 1) {
            possible.removeAll(impossible);
            if(!possible.isEmpty()) {
                list.add(possible.iterator().next());
            }
        }
        out.println(list.size());
        list.sort();
        for(int x : list.toArray()){
            out.append(x + 1).append(' ');
        }
    }
}
