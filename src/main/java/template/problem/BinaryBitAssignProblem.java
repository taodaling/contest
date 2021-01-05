package template.problem;

public class BinaryBitAssignProblem {
    /**
     * is there a way to assign -1 or +1 coefficient for each bit number(2^i) and their sum is s
     *
     * @param cnt cnt[i] means the occurence of 2^i
     * @return
     */
    public static boolean assign2(long[] cnt, long s) {
        s = Math.abs(s);
        long sum = s;
        long bit = 0;
        for (int i = 0; sum > 0 || i < cnt.length; i++) {
            if (i < cnt.length) {
                sum += cnt[i];
            }
            if (i > 0 && i <= cnt.length) {
                bit += cnt[i - 1];
            }
            if ((sum & 1) == 1) {
                if (bit == 0) {
                    return false;
                }
                bit--;
            }
            sum >>>= 1;
            bit >>>= 1;
            assert sum >= 0;
            assert bit >= 0;
        }
        return true;
    }

    /**
     * is there a way to assign -1 or 0 or +1 coefficient for each bit number(2^i) and their sum is s
     *
     * @param cnt cnt[i] means the occurence of 2^i
     * @return
     */
    public static boolean assign3(long[] cnt, long s) {
        s = Math.abs(s);
        long sum = s;
        long bit = 0;
        for (int i = 0; s > 0 || i < cnt.length; i++) {
            if (i < cnt.length) {
                sum += cnt[i];
            }
            long contrib = 0;
            if (i > 0 && i <= cnt.length && cnt[i] > 0) {
                contrib = cnt[i];
            }
            if ((sum & 1) == 1) {
                if (bit == 0) {
                    if (contrib > 0) {
                        contrib--;
                    } else {
                        return false;
                    }
                } else {
                    bit--;
                }
            }
            sum >>= 1;
            bit >>= 1;
            bit += contrib;
        }
        return true;
    }
}

