package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.regex.Pattern;

public class SpamFilter {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        String letter = merge("[a-zA-Z]");
        String symbol = merge(letter, "[0-9]", "_", "-");
        String word = merge(symbol + "+");
        String prefix = merge(word + merge("\\." + word) + "*");
        String domain = merge(letter + letter, letter + letter + letter);
        String suffix = merge(prefix + "\\." + domain);
        String address = merge(prefix + "\\@" + suffix);

        debug.debug("address", address);
        Pattern p = Pattern.compile(address);
        int n = in.ri();
        for (int i = 0; i < n; i++) {
            in.skipEmptyLine();
            String x = in.readLine();
            out.println(p.matcher(x).matches() ? "YES" : "NO");
        }
    }

    public String merge(String... s) {
        StringBuilder ans = new StringBuilder("(");
        for (String x : s) {
            ans.append(x).append('|');
        }
        ans.setLength(ans.length() - 1);
        ans.append(')');
        return ans.toString();
    }

    public boolean isLetter(String s) {
        char head = s.charAt(0);
        return s.length() == 1 && ('a' <= head && head <= 'z' || 'A' <= head && head <= 'Z');
    }

    public boolean isSymbol(String s) {
        char head = s.charAt(0);
        return s.length() == 1 && (isLetter(s) || (head >= '0' && head <= '9') ||
                head == '_' || head == '-');
    }

    public boolean isWord(String s) {
        boolean ans = s.length() > 0;
        for (int i = 0; i < s.length(); i++) {
            ans = ans && isSymbol(s.substring(i, i + 1));
        }
        return ans;
    }

    public boolean isPrefix(String s) {
        String[] splited = s.split("\\.", -1);
        boolean ans = s.length() > 0;
        for (String x : splited) {
            ans = ans && isWord(x);
        }
        return ans;
    }

    public boolean isDomain(String s) {
        boolean ans = s.length() >= 2 && s.length() <= 3;
        for (int i = 0; i < s.length(); i++) {
            ans = ans && isLetter(s.substring(i, i + 1));
        }
        return ans;
    }

    public boolean isSuffix(String s) {
        int index = s.lastIndexOf('.');
        if (index < 0) {
            return false;
        }
        return isPrefix(s.substring(0, index)) &&
                isDomain(s.substring(index + 1));
    }

    public boolean isAddress(String s) {
        int index = s.lastIndexOf('@');

        if (index < 0) {
            return false;
        }
        return isPrefix(s.substring(0, index)) &&
                isSuffix(s.substring(index + 1));
    }
}
