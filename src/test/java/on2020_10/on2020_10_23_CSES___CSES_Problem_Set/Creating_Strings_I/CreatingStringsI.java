package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Creating_Strings_I;



import template.io.FastInput;
import template.math.PermutationUtils;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class CreatingStringsI {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        Set<String> ans = new TreeSet<>();
        String s = in.readString();
        int[] perm = IntStream.range(0, s.length()).toArray();
        do {
            StringBuilder builder = new StringBuilder(s.length());
            for (int j = 0; j < s.length(); j++) {
                builder.append(s.charAt(perm[j]));
            }
            ans.add(builder.toString());
        } while (PermutationUtils.nextPermutation(perm));
        out.println(ans.size());
        for(String x : ans){
            out.println(x);
        }
    }
}
