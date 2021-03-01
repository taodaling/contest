package contest;

import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class EquivalentStrings {
    void rotate(char[] s) {
        int n = s.length;
        char first = s[0];
        for (int i = 1; i < n; i++) {
            s[i - 1] = s[i];
        }
        s[n - 1] = first;
    }


    int[] head;

    private boolean before(int a, int b) {
        if (b < 0 || b >= 10) {
            return true;
        }
        if (head[b] == -1) {
            return true;
        }
        if (head[a] == -1) {
            return false;
        }
        return head[a] < head[b];
    }

    public int count(String[] seeds) {
        Set<String> sets = new HashSet<>();
        head = new int[10];
        StringBuilder sb = new StringBuilder(2500);
        for (String s : seeds) {
            char[] seq = s.toCharArray();
            int n = seq.length;
            int[] next = new int[n];
            for (int r = 0; r < n; r++) {
                rotate(seq);

                sb.setLength(0);
                Arrays.fill(head, -1);
                for (int i = n - 1; i >= 0; i--) {
                    next[i] = head[seq[i] - '0'];
                    head[seq[i] - '0'] = i;
                }
                for (int i = 0; i < n; i++) {
                    int best = -1;
                    for (int j = 0; j < 10; j++) {
                        if (head[j] != -1 && before(j, j - 1) && before(j, j + 1)) {
                            best = j;
                            break;
                        }
                    }
                    assert best != -1;
                    sb.append(best);
                    head[best] = next[head[best]];
                }
                sets.add(sb.toString());
            }
        }
        return sets.size();
    }
}

