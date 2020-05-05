package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ADidYouMean {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int[] cnts = new int[128];
        boolean[] consonants = new boolean[128];
        Arrays.fill(consonants, true);
        consonants['a'] = consonants['e'] = consonants['i'] = consonants['o'] = consonants['u'] = false;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            char c = s[i];
            if (!consonants[c]) {
                builder.append(c);
                Arrays.fill(cnts, 0);
                continue;
            }
            cnts[c]++;
            if (diff(cnts) > 1 && total(cnts) > 2) {
                out.append(builder.toString()).append(' ');
                builder.setLength(0);
                builder.append(c);
                Arrays.fill(cnts, 0);
                cnts[c]++;
                continue;
            }
            builder.append(c);
        }

        if(builder.length() > 0){
            out.append(builder.toString());
        }
    }

    public int diff(int[] cnts) {
        int ans = 0;
        for (int x : cnts) {
            ans += x > 0 ? 1 : 0;
        }
        return ans;
    }

    public int total(int[] cnts) {
        int ans = 0;
        for (int x : cnts) {
            ans += x;
        }
        return ans;
    }
}
