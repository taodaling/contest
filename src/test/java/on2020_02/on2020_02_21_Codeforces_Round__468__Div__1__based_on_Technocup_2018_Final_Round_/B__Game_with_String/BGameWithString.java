package on2020_02.on2020_02_21_Codeforces_Round__468__Div__1__based_on_Technocup_2018_Final_Round_.B__Game_with_String;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BGameWithString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int n = s.length;
        Rotate[] rotates = new Rotate[n];
        for (int i = 0; i < n; i++) {
            rotates[i] = new Rotate(s, i);
        }
        Map<Integer, List<Rotate>> groupByFirst =
                Stream.of(rotates)
                        .collect(Collectors.groupingBy(x -> x.get(0)));

        Rotate[] appear = new Rotate['z' - 'a' + 1];
        long sum = 0;
        Rotate dump = new Rotate(s, 0);
        for (List<Rotate> list : groupByFirst.values()) {
//            for (Rotate r : list) {
//                System.err.println(r);
//            }
            long max = 0;
            for (int i = 0; i < n; i++) {
                Arrays.fill(appear, null);
                for (Rotate r : list) {
                    int c = r.get(i) - 'a';
                    if (appear[c] != null) {
                        appear[c] = dump;
                    } else {
                        appear[c] = r;
                    }
                }
                int cnt = 0;
                for (Rotate r : appear) {
                    if (r != null && r != dump) {
                        cnt++;
                    }
                }
                max = Math.max(max, cnt);
            }

            sum += max;
        }

        out.printf("%.15f", (double) sum / n);
    }

}

class Rotate {
    char[] s;
    int begin;
    boolean valid;

    public Rotate(char[] s, int begin) {
        this.s = s;
        this.begin = begin;
    }

    public int get(int i) {
        i += begin;
        if (i >= s.length) {
            i -= s.length;
        }
        return s[i];
    }

    @Override
    public String toString() {
        int n = s.length;
        StringBuilder builder = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            builder.append((char) get(i));
        }
        return builder.toString();
    }
}