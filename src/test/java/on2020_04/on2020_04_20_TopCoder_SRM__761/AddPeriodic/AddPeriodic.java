package on2020_04.on2020_04_20_TopCoder_SRM__761.AddPeriodic;



import template.math.BigFraction;

public class AddPeriodic {
    public String add(String A, String B) {
        BigFraction a = BigFraction.parsePeriodFractionalStyleString(A);
        BigFraction b = BigFraction.parsePeriodFractionalStyleString(B);
        BigFraction c = BigFraction.plus(a, b);
        String ans = BigFraction.formatPeriodFractionalStyleString(c);
        return ans;
    }
}
