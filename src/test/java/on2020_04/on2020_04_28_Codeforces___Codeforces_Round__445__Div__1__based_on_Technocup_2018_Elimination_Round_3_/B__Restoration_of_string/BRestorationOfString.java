package on2020_04.on2020_04_28_Codeforces___Codeforces_Round__445__Div__1__based_on_Technocup_2018_Elimination_Round_3_.B__Restoration_of_string;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class BRestorationOfString {
    int charset = 'z' - 'a' + 1;

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        String[] registries = new String[charset];
        for (int i = 0; i < n; i++) {
            Arrays.fill(occur, false);
            String s = in.readString();
            if (!check(s)) {
                out.println("NO");
                return;
            }

            for (int j = 0; j < occur.length; j++) {
                if (!occur[j] || registries[j] == null) {
                    continue;
                }
                s = merge(s, registries[j], (char) (j + 'a'));
                if (s == null || !check(s)) {
                    out.println("NO");
                    return;
                }
                clean(registries, registries[j]);
            }

            for (int j = 0; j < s.length(); j++) {
                registries[s.charAt(j) - 'a'] = s;
            }

            debug.debug("reg", registries);
        }

        TreeSet<String> remain = new TreeSet<>();
        for (String r : registries) {
            if (r == null) {
                continue;
            }
            remain.add(r);
        }
        for (
                String s : remain) {
            out.append(s);
        }

    }

    boolean[] occur = new boolean[charset];

    public boolean check(String s) {
        Arrays.fill(occur, false);
        for (int j = 0; j < s.length(); j++) {
            int c = s.charAt(j) - 'a';
            if (occur[c]) {
                return false;
            }
            occur[c] = true;
        }
        return true;
    }

    public void clean(String[] regs, String s) {
        for (int i = 0; i < s.length(); i++) {
            regs[s.charAt(i) - 'a'] = null;
        }
    }

    public String merge(String a, String b, char c) {
        int aIndex = a.indexOf(c);
        int bIndex = b.indexOf(c);
        if (aIndex < bIndex) {
            String sTmp = a;
            a = b;
            b = sTmp;
            int iTmp = aIndex;
            aIndex = bIndex;
            bIndex = iTmp;
        }
        StringBuilder ans = new StringBuilder(26);
        ans.append(a);
        for (int i = 0; i < b.length(); i++) {
            int match = i + (aIndex - bIndex);
            char v = b.charAt(i);
            if (match >= ans.length()) {
                ans.append(v);
            }
            if (ans.charAt(match) != v) {
                return null;
            }
        }
        return ans.toString();
    }
}
