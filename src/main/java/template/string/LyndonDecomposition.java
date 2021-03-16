package template.string;

import java.util.*;

/**
 * copied from https://github.com/indy256/codelibrary/blob/master/java/strings/LyndonDecomposition.java
 * modified by dalt
 */
public class LyndonDecomposition {
//
//    public static String minCyclicShift(String a) {
//        char[] s = (a + a).toCharArray();
//        int n = s.length;
//        int res = 0;
//        for (int i = 0; i < n / 2; ) {
//            res = i;
//            int j = i + 1, k = i;
//            while (j < n && s[k] <= s[j]) {
//                if (s[k] < s[j])
//                    k = i;
//                else
//                    ++k;
//                ++j;
//            }
//            while (i <= k) i += j - k;
//        }
//        return new String(s, res, n / 2);
//    }

    public static List<CharSequence> decompose(CharSequence s) {
        List<CharSequence> res = new ArrayList<>(s.length());
        int n = s.length();
        int i = 0;
        while (i < n) {
            int j = i + 1, k = i;
            while (j < n && s.charAt(k) <= s.charAt(j)) {
                if (s.charAt(k) < s.charAt(j))
                    k = i;
                else
                    ++k;
                ++j;
            }
            while (i <= k) {
                res.add(s.subSequence(i, i + j - k));
                i += j - k;
            }
        }
        return res;
    }

//    public static void main(String[] args) {
//        String s = "bara";
//        String[] decompose = decompose(s);
//        System.out.println(Arrays.toString(decompose));
//        System.out.println(minCyclicShift(s));
//    }
}
