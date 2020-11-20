package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhoneNumbers {
    char[] s = new char[100];
    int[] map = new int[128];

    {
        String[] s = new String[10];
        s[1] = "ij";
        s[2] = "abc";
        s[3] = "def";
        s[4] = "gh";
        s[5] = "kl";
        s[6] = "mn";
        s[7] = "prs";
        s[8] = "tuv";
        s[9] = "wxy";
        s[0] = "oqz";
        for (int i = 0; i < 10; i++) {
            for (char c : s[i].toCharArray()) {
                map[c] = i;
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readString(s, 0);
        if (n == 2 && s[0] == '-') {
            throw new UnknownError();
        }

        int m = in.readInt();
        Trie root = new Trie();
        String[] words = new String[m];
        for (int i = 0; i < m; i++) {
            words[i] = in.readString();
            int k = words[i].length();
            Trie cur = root;
            for (int j = k - 1; j >= 0; j--) {
                cur = cur.get(map[words[i].charAt(j)]);
            }
            cur.word = i;
        }

        int inf = (int) 1e9;
        int[] dp = new int[n + 1];
        int[] use = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            dp[i] = inf;
            Trie cur = root;
            for (int j = i - 1; j >= 0; j--) {
                cur = cur.next[s[j] - '0'];
                if (cur == null) {
                    break;
                }
                if (cur.word != -1 && dp[j] + 1 < dp[i]) {
                    dp[i] = dp[j] + 1;
                    use[i] = cur.word;
                }
            }
        }

        if (dp[n] == inf) {
            out.println("No solution.");
            return;
        }
        List<String> seq = new ArrayList<>(n);
        for (int i = n; i > 0; i -= words[use[i]].length()) {
            seq.add(words[use[i]]);
        }
        Collections.reverse(seq);
        for(String s : seq){
            out.append(s).append(' ');
        }
        out.println();
    }
}

class Trie {
    Trie[] next = new Trie[10];
    int word = -1;

    public Trie get(int i) {
        if (next[i] == null) {
            next[i] = new Trie();
        }
        return next[i];
    }
}