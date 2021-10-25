package template.utils;

public class ToStringUtils {
    public static String toBinaryString(long x, int len) {
        StringBuilder ans = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            ans.append((x >>> i) & 1);
        }
        return ans.toString();
    }
}
