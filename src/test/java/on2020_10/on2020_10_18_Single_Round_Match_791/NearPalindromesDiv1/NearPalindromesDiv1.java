package on2020_10.on2020_10_18_Single_Round_Match_791.NearPalindromesDiv1;



import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class NearPalindromesDiv1 {
    public int solve(String S) {
        int[] cnts = new int[charset];
        for (char c : S.toCharArray()) {
            cnts[c - 'a'] ^= 1;
        }
        List<Integer> cs = new ArrayList<>();
        for (int i = 0; i < charset; i++) {
            if (cnts[i] == 1) {
                cs.add(i);
            }
        }
        if (cs.size() <= 1) {
            return 0;
        }

        if (cs.size() % 2 == 0) {
            return cost(cs);
        }
        int ans = (int) 1e9;
        for (int i = 0; i < cs.size(); i++) {
            List<Integer> clone = new ArrayList<>(cs);
            clone.remove(i);
            ans = Math.min(ans, cost(clone));
        }
        return ans;
    }

    int charset = 'z' - 'a' + 1;

    public int cost0(List<Integer> list) {
        int ans = 0;
        for (int i = 0; i < list.size(); i += 2) {
            int cost = Math.abs(list.get((i + 1) % list.size()) - list.get(i));
            cost = Math.min(cost, charset - cost);
            ans += cost;
        }
        return ans;
    }

    public int cost(List<Integer> list) {
        int ans0 = cost0(list);
        SequenceUtils.reverse(list);
        int ans1 = cost0(list);
        return Math.min(ans0, ans1);
    }
}
