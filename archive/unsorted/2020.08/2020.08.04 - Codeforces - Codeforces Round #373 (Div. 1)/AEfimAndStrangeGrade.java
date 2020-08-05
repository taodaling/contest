package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AEfimAndStrangeGrade {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int t = in.readInt();
        char[] data = new char[n];
        in.readString(data, 0);
        int dot = 0;
        while (dot < n) {
            if (data[dot++] == '.') {
                break;
            }
        }
        dot--;
        boolean add = false;
        for (int i = dot + 1; i < n; i++) {
            if (data[i] < '5') {
                continue;
            }
            for (int j = i; j < n; j++) {
                data[j] = '0';
            }
            if (i == dot + 1) {
                add = true;
            } else {
                data[i - 1]++;
            }
            t--;
            for (int j = i - 1; j > dot && t > 0; j--) {
                if (data[j] < '5') {
                    break;
                }
                data[j] = '0';
                if (j == dot + 1) {
                    add = true;
                } else {
                    data[j - 1]++;
                }
                t--;
            }

            break;
        }
        for (int i = dot - 1; i >= 0 && add; i--) {
            add = false;
            if (data[i] < '9') {
                data[i]++;
                break;
            }
            data[i] = '0';
            add = true;
        }
        if (add) {
            out.append(1);
        }
        int tail = data.length - 1;
        while (tail > dot && data[tail] == '0') {
            tail--;
        }
        if (tail == dot) {
            tail--;
        }
        for(int i = 0; i <= tail; i++){
            out.append(data[i]);
        }
    }
}
