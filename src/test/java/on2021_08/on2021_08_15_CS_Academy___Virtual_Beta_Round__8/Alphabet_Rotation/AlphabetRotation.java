package on2021_08.on2021_08_15_CS_Academy___Virtual_Beta_Round__8.Alphabet_Rotation;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashMap;
import java.util.Map;

public class AlphabetRotation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Map<String, Integer> cntMap = new HashMap<>(n);
        String[] input = new String[n];
        for (int i = 0; i < n; i++) {
            String s = in.rs();
            String t = minRepresentation(s);
            input[i] = t;
            cntMap.put(t, cntMap.getOrDefault(t, 0) + 1);
        }
        for (int i = 0; i < n; i++) {
            if (cntMap.get(input[i]) > 1) {
                out.println(1);
            } else {
                out.println(0);
            }
        }
    }

    int charset = 'z' - 'a' + 1;

    public String minRepresentation(String s) {
        int first = s.charAt(0) - 'a';
        //- first
        StringBuilder ans = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            c -= first;
            if (c < 0) {
                c += charset;
            }
            ans.append((char) (c + 'a'));
        }
        return ans.toString();
    }
}
