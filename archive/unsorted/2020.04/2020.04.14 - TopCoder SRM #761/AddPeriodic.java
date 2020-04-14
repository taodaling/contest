package contest;

import template.string.KMPAutomaton;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AddPeriodic {
    public String add(String A, String B) {
        BigDecimal a = parse(A);
        BigDecimal b = parse(B);
        BigDecimal c = a.add(b).setScale(5000, RoundingMode.FLOOR);
        String cval = c.toPlainString();
        if (cval.endsWith("9") && cval.indexOf('.') >= 0) {
            StringBuilder modify = new StringBuilder(cval);
            for (int i = 0; i < modify.length(); i++) {
                if (modify.charAt(i) >= '0' && modify.charAt(i) <= '9') {
                    modify.setCharAt(i, '0');
                }
            }
            modify.setCharAt(modify.length() - 1, '1');
            c = c.add(new BigDecimal(modify.toString()));
        }
        long integer = c.longValue();
        c = c.subtract(new BigDecimal(integer));
        StringBuilder builder = new StringBuilder();
        if (c.equals(BigDecimal.ZERO)) {
        } else {
            builder.append(c.toPlainString().substring(2));
        }
        while (builder.length() < 100) {
            builder.append(0);
        }
        String s = builder.toString();
        //try first 20
        for (int i = 0; ; i++) {
            String t = s.substring(i);
            KMPAutomaton kmp = new KMPAutomaton(t.length());
            for (int j = 0; j < t.length(); j++) {
                kmp.build(t.charAt(j));
            }
            int period = t.length() - kmp.maxBorder(t.length() - 1);
            if (period < 50) {
                String p = t.substring(0, period);
                String ans = integer + "." + s.substring(0, i) + "(" + p + ")";
                return ans;
            }
        }
    }

    public BigDecimal parse(String s) {
        StringBuilder builder = new StringBuilder();
        int l = s.indexOf("(");
        int r = s.indexOf(")");
        String rep = s.substring(l + 1, r);
        builder.append(s.substring(0, l));
        while (builder.length() < 10000) {
            builder.append(rep);
        }
        BigDecimal bd = new BigDecimal(builder.toString());
        return bd;
    }
}