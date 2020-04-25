package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class AShortProgram {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] cmd = new char[n];
        int[] operands = new int[n];
        for (int i = 0; i < n; i++) {
            cmd[i] = in.readChar();
            operands[i] = in.readInt();
        }

        int[] output = new int[2];
        output[0] = 0;
        output[1] = 1023;
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < n; i++) {
                if (cmd[i] == '&') {
                    output[j] = output[j] & operands[i];
                }
                if (cmd[i] == '|') {
                    output[j] = output[j] | operands[i];
                }
                if (cmd[i] == '^') {
                    output[j] = output[j] ^ operands[i];
                }
            }
        }

        char[] ansCmd = new char[]{'&', '|', '^'};
        int[] ansOperand = new int[]{1023, 0, 0};
        for (int i = 0; i < 10; i++) {
            int zero = Bits.bitAt(output[0], i);
            int one = Bits.bitAt(output[1], i);

            if (zero == one) {
                if (zero == 0) {
                    ansOperand[0] = Bits.setBit(ansOperand[0], i, false);
                } else {
                    ansOperand[1] = Bits.setBit(ansOperand[1], i, true);
                }
            } else {
                if (zero == 1) {
                    ansOperand[2] = Bits.setBit(ansOperand[2], i, true);
                }
            }
        }

        out.println(3);
        for(int i = 0; i < 3; i++){
            out.append(ansCmd[i]).append(' ').append(ansOperand[i]).println();
        }
    }
}
