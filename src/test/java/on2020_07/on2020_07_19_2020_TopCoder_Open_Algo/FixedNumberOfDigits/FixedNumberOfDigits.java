package on2020_07.on2020_07_19_2020_TopCoder_Open_Algo.FixedNumberOfDigits;



import template.math.DigitUtils;

public class FixedNumberOfDigits {
    public long sum(int start, int step, long numberOfDigits) {
        long x = start;
        while (true) {
            int digit = ("" + x).length();
            long limit = Math.round(Math.pow(10, digit));
            //x + step * t >= limit
            long t = DigitUtils.ceilDiv(limit - x, step);
            if (t * digit < numberOfDigits) {
                numberOfDigits -= t * digit;
                x += t * step;
                continue;
            }
            long skip = DigitUtils.maximumIntegerLessThanDiv(numberOfDigits, digit);
            x += skip * step;
            numberOfDigits -= skip * digit;
            String s = "" + x;
            return Long.parseLong(s.substring(0, (int) numberOfDigits));
        }
    }
}
