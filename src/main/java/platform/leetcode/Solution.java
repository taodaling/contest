package platform.leetcode;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {

    public static void main(String[] args) {
        int[] input = new int[]{};
        int[] output = new int[]{2, 3};

        System.out.println();
    }

    int valueOf(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ? 1 : 0;
    }

    public int maxVowels(String s, int k) {
        int cnt = 0;
        int ans = 0;
        int r = -1;
        int l = 0;

        for (int i = 0; i < s.length() && i + k - 1 < s.length(); i++) {
            while (r + 1 <= i + k - 1) {
                r++;
                cnt += valueOf(s.charAt(r));
            }
            while (l < i) {
                cnt -= valueOf(s.charAt(l));
                l--;
            }
            ans = Math.max(cnt, ans);
        }
        return ans;
    }
}
