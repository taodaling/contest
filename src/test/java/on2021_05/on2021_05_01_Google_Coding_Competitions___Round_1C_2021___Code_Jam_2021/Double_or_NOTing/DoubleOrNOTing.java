package on2021_05.on2021_05_01_Google_Coding_Competitions___Round_1C_2021___Code_Jam_2021.Double_or_NOTing;



import template.io.FastInput;
import template.io.FastOutput;

public class DoubleOrNOTing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);

        String s = in.rs();
        String e = in.rs();

        int extra = 0;
        String impossible = "IMPOSSIBLE";
        if (s.equals("0")) {
            if (e.equals("0")) {
                out.println(0);
                return;
            }
            s = "1";
            extra++;
        }
        if (e.equals("0")) {
            e = "";
        }

        long inf = (long) 1e18;
        long ans = inf;
        for (int i = 0; i <= s.length() && i <= e.length(); i++) {
            String shead = s.substring(0, s.length() - i);
            String stail = s.substring(s.length() - i, s.length());

            int chance = diff(shead, false);
            if (shead.length() > 0) {
                chance++;
            }
            String er = chance % 2 == 0 ? e : reverse(e);
            String ehead = er.substring(0, i);
            String etail = e.substring(i, e.length());
            if (!stail.equals(ehead)) {
                continue;
            }
            if (shead.length() > 0 && stail.length() > 0 && shead.charAt(shead.length() - 1) == stail.charAt(0)) {
                continue;
            }
            int req = diff(etail, true);
            if (req > chance) {
                continue;
            }
            long cand = chance + etail.length();
            ans = Math.min(ans, cand);
        }
        if (ans == inf) {
            out.println(impossible);
            return;
        }
        out.println(ans + extra);
    }

    public String reverse(String s) {
        StringBuilder ans = new StringBuilder(s.length());
        for (char c : s.toCharArray()) {
            ans.append((char) ('1' - c + '0'));
        }
        return ans.toString();
    }

    public int diff(String s, boolean endWithZero) {
        if (endWithZero) {
            s += "0";
        }
        int ans = 0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) != s.charAt(i - 1)) {
                ans++;
            }
        }
        return ans;
    }

}
