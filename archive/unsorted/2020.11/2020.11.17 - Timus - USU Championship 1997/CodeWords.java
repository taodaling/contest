package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

public class CodeWords {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n + 1];
        IntegerPreSum ps = new IntegerPreSum(n + 1);
        int test = 0;
        while (in.hasMore()) {
            test++;
            int m = in.readString(s, 0);
            for (int i = 0; i < m; i++) {
                s[i] -= '0';
            }
            ps.populate(i -> s[i], m);
            int sum = 0;
            for (int j = 0; j < m; j++) {
                sum += (j + 1) * s[j];
            }

            if (m == n) {
                //case 1
                if (sum % (n + 1) != 0) {
                    //replace 1 to 0
                    for (int i = 0; i < n; i++) {
                        if ((sum - (i + 1) * s[i]) % (n + 1) == 0) {
                            s[i] = 0;
                            break;
                        }
                    }
                }

            } else if (m == n + 1) {
                //remove any
                for (int i = 0; i < m; i++) {
                    if ((sum - s[i] * (i + 1) - ps.post(i + 1)) % (n + 1) == 0) {
                        System.arraycopy(s, i + 1, s, i, m - (i + 1));
                        break;
                    }
                }
            } else {
                //add any
                for (int i = 0; i <= m; i++) {
                    if ((sum + ps.post(i)) % (n + 1) == 0) {
                        System.arraycopy(s, i, s, i + 1, m - i);
                        s[i] = 0;
                        break;
                    }
                    if ((sum + ps.post(i) + i + 1) % (n + 1) == 0) {
                        System.arraycopy(s, i, s, i + 1, m - i);
                        s[i] = 1;
                        break;
                    }
                }
            }
            for (int j = 0; j < n; j++) {
                out.append((char)(s[j] + '0'));
            }
            out.println();
        }
    }
}
