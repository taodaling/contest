package strings;

// Manacher's algorithm to finds all palindromes: https://cp-algorithms.com/string/manacher.html
public class Manacher {
    // d1[i] - how many palindromes of odd length with center at i
    public static int[] oddPalindromes(char[] s, int n, int[] d1) {
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int k = (i > r ? 0 : Math.min(d1[l + r - i], r - i));
            while (i + k < n && i - k >= 0 && s[i + k] == s[i - k]) ++k;
            d1[i] = k--;
            if (i + k > r) {
                l = i - k;
                r = i + k;
            }
        }
        return d1;
    }

    // d1[i] - how many palindromes of odd length with center at i
    public static int[] oddPalindromes(long[] s, int n, int[] d1) {
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int k = (i > r ? 0 : Math.min(d1[l + r - i], r - i));
            while (i + k < n && i - k >= 0 && s[i + k] == s[i - k]) ++k;
            d1[i] = k--;
            if (i + k > r) {
                l = i - k;
                r = i + k;
            }
        }
        return d1;
    }

    // d1[i] - how many palindromes of odd length with center at i
    public static int[] oddPalindromes(int[] s, int n, int[] d1) {
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int k = (i > r ? 0 : Math.min(d1[l + r - i], r - i));
            while (i + k < n && i - k >= 0 && s[i + k] == s[i - k]) ++k;
            d1[i] = k--;
            if (i + k > r) {
                l = i - k;
                r = i + k;
            }
        }
        return d1;
    }

    // d2[i] - how many palindromes of even length with center(right character) at i
    public static int[] evenPalindromes(char[] s, int n, int[] d2) {
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int k = (i > r ? 0 : Math.min(d2[l + r - i + 1], r - i + 1)) + 1;
            while (i + k - 1 < n && i - k >= 0 && s[i + k - 1] == s[i - k]) ++k;
            d2[i] = --k;
            if (i + k - 1 > r) {
                l = i - k;
                r = i + k - 1;
            }
        }
        return d2;
    }

    // d2[i] - how many palindromes of even length with center(right character) at i
    public static int[] evenPalindromes(int[] s, int n, int[] d2) {
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int k = (i > r ? 0 : Math.min(d2[l + r - i + 1], r - i + 1)) + 1;
            while (i + k - 1 < n && i - k >= 0 && s[i + k - 1] == s[i - k]) ++k;
            d2[i] = --k;
            if (i + k - 1 > r) {
                l = i - k;
                r = i + k - 1;
            }
        }
        return d2;
    }

    // d2[i] - how many palindromes of even length with center(right character) at i
    public static int[] evenPalindromes(long[] s, int n, int[] d2) {
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int k = (i > r ? 0 : Math.min(d2[l + r - i + 1], r - i + 1)) + 1;
            while (i + k - 1 < n && i - k >= 0 && s[i + k - 1] == s[i - k]) ++k;
            d2[i] = --k;
            if (i + k - 1 > r) {
                l = i - k;
                r = i + k - 1;
            }
        }
        return d2;
    }
}
