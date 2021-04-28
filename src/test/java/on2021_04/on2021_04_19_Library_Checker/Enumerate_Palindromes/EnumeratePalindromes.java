package on2021_04.on2021_04_19_Library_Checker.Enumerate_Palindromes;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.Manacher;

public class EnumeratePalindromes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 5e5];
        int n = in.rs(s);
        int[] odd = new int[n];
        int[] even = new int[n];
        Manacher.evenPalindrome(i -> s[i], n, even);
        Manacher.oddPalindrome(i -> s[i], n, odd);
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                out.append(even[i] * 2).append(' ');
            }
            out.append(odd[i] * 2 - 1).append(' ');
        }
    }
}
