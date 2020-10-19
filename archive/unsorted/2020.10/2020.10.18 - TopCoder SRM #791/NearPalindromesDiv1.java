package contest;

import template.problem.OnCircleMinCostMatchProblem;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class NearPalindromesDiv1 {
    int charset = 'z' - 'a' + 1;

    public int solve(String S) {
        int[] cnt = new int[charset];
        for (char c : S.toCharArray()) {
            cnt[c - 'a']++;
        }
        List<Integer> odd = new ArrayList<>();
        for (int i = 0; i < charset; i++) {
            if (cnt[i] % 2 == 1) {
                odd.add(i);
            }
        }
        if (odd.size() <= 1) {
            return 0;
        }
        if(odd.size() % 2 == 0){
            return match(odd);
        }
        int ans = (int)1e9;
        for(int i = 0; i < odd.size(); i++){
            List<Integer> clone = new ArrayList<>(odd);
            clone.remove(i);
            ans = Math.min(ans, match(clone));
        }
        return ans;

    }

    public int match0(List<Integer> seq) {
        int n = seq.size();
        int ans = 0;
        for (int i = 0; i < n; i += 2) {
            int cur = seq.get(i);
            int next = seq.get((i + 1) % n);
            int dist = Math.abs(cur - next);
            ans += Math.min(dist, charset - dist);
        }
        return ans;
    }

    public int match(List<Integer> seq) {
        int ans1 = match0(seq);
        SequenceUtils.rotate(seq, 0, seq.size() - 1, 1);
        int ans2 = match0(seq);
        return Math.min(ans1, ans2);
    }
}
