package on2020_05.on2020_05_24_AtCoder___AtCoder_Grand_Contest_044.D___Guess_the_Password;



import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerRange2DequeAdapter;

public class DGuessThePassword {
    char[] charset;

    {
        StringBuilder charsetBuilder = new StringBuilder();
        for (char i = 'a'; i <= 'z'; i++) {
            charsetBuilder.append(i);
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            charsetBuilder.append(i);
        }
        for (char i = '0'; i <= '9'; i++) {
            charsetBuilder.append(i);
        }
        charset = charsetBuilder.toString().toCharArray();
    }

    int[] cnts;
    int L;

    StringBuilder query = new StringBuilder(128);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        cnts = new int[charset.length];
        for (int i = 0; i < charset.length; i++) {
            query.setLength(0);
            for (int j = 0; j < 128; j++) {
                query.append(charset[i]);
            }
            cnts[i] = 128 - ask(query);
            L += cnts[i];
        }

        String ans = dac(0, charset.length - 1);
        out.append("! ").println(ans).flush();
    }


    public String dac(int l, int r) {
        if (l == r) {
            query.setLength(0);
            for (int i = 0; i < cnts[l]; i++) {
                query.append(charset[l]);
            }
            return query.toString();
        }

        int m = (l + r) / 2;
        String left = dac(l, m);
        String right = dac(m + 1, r);

        IntegerDeque ldq = new IntegerRange2DequeAdapter(i -> left.charAt(i), 0, left.length() - 1);
        IntegerDeque rdq = new IntegerRange2DequeAdapter(i -> right.charAt(i), 0, right.length() - 1);

        query.setLength(0);
        while (!ldq.isEmpty() && !rdq.isEmpty()) {
            int len = query.length();
            query.append((char)rdq.peekFirst());
            for (IntegerIterator iterator = ldq.iterator(); iterator.hasNext(); ) {
                query.append((char)iterator.next());
            }
            if (ask(query) == L - query.length()) {
                rdq.removeFirst();
                query.setLength(len + 1);
            } else {
                query.setLength(len);
                query.append((char)ldq.removeFirst());
            }
        }

        while (!ldq.isEmpty()) {
            query.append((char)ldq.removeFirst());
        }
        while (!rdq.isEmpty()) {
            query.append((char)rdq.removeFirst());
        }
        return query.toString();
    }

    FastInput in;
    FastOutput out;

    public int ask(Object s) {
        out.append("? ").println(s).flush();
        return in.readInt();
    }
}
