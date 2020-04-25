package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.TreeSet;

public class CKThSubstring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        int k = in.readInt();
        TreeSet<String> set = new TreeSet<>();
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j - i + 1 <= k && j < s.length(); j++) {
                set.add(s.substring(i, j + 1));
            }
        }

        for(int i = 0; i < k - 1; i++){
            set.pollFirst();
        }

        out.println(set.first());
    }
}
