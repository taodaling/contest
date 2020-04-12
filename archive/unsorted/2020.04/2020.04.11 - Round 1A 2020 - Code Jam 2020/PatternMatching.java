package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PatternMatching {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        List<String> ps = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            ps.add(in.readString());
        }

        List<String> prefix = new ArrayList<>();
        List<String> suffix = new ArrayList<>();
        String fix = null;
        for (String s : ps) {
            int index = s.indexOf('*');
            int lastIndex = s.lastIndexOf('*');
            if (index == -1) {
                fix = s;
            } else {
                prefix.add(s.substring(0, index));
                suffix.add(s.substring(lastIndex + 1));
            }
        }
        if (fix != null) {
            for (String s : ps) {
                if (!match(fix, s)) {
                    out.println("*");
                    return;
                }
            }
            out.println(fix);
            return;
        }

        prefix.sort((a, b) -> Integer.compare(a.length(), b.length()));
        suffix.sort((a, b) -> Integer.compare(a.length(), b.length()));
        for (int i = 1; i < prefix.size(); i++) {
            if (!prefix.get(i).startsWith(prefix.get(i - 1))) {
                out.println("*");
                return;
            }
        }
        for (int i = 1; i < suffix.size(); i++) {
            if (!suffix.get(i).endsWith(suffix.get(i - 1))) {
                out.println("*");
                return;
            }
        }

        StringBuilder ans = new StringBuilder();
        ans.append(prefix.get(prefix.size() - 1));
        for (String p : ps) {
            int index = p.indexOf('*');
            int lastIndex = p.lastIndexOf('*');
            for (int i = index; i <= lastIndex; i++) {
                char c = p.charAt(i);
                if (c != '*') {
                    ans.append(c);
                }
            }
        }
        ans.append(suffix.get(suffix.size() - 1));
        out.println(ans);
    }

    public boolean match(String fix, String s) {
        int n = fix.length();
        int m = s.length();
        boolean[][] match = new boolean[n + 1][m + 1];
        match[0][0] = true;
        for(int i = 1; i <= m && s.charAt(i - 1) == '*'; i++){
            match[0][i] = true;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s.charAt(j - 1) == fix.charAt(i - 1) && match[i - 1][j - 1]) {
                    match[i][j] = true;
                } else if (s.charAt(j - 1) == '*' && (match[i - 1][j] || match[i][j - 1])) {
                    match[i][j] = true;
                }
            }
        }
        return match[n][m];
    }
}
