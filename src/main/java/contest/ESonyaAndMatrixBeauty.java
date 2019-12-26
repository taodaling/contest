package contest;

import template.datastructure.PreXor;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.ModifiableWholeHash;
import template.string.Manacher;

import java.util.Arrays;

public class ESonyaAndMatrixBeauty {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append(250).append(' ').append(250).append('\n');
        for (int i = 0; i < 250 * 250; i++) {
            builder.append('a');
        }
        System.out.println(builder.toString());
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] mat = new int[n][m];
        int[][] bits = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() - 'a';
                bits[i][j] = 1 << mat[i][j];
            }
        }

        int ans = 0;
        int limit = 'z' - 'a' + 1;
        ModifiableWholeHash[][] wholeHashes = new ModifiableWholeHash[n][2];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                wholeHashes[i][0] = new ModifiableWholeHash(31, limit);
                //wholeHashes[i][1] = new ModifiableWholeHash(61, limit);
            } else {
                wholeHashes[i][0] = new ModifiableWholeHash(wholeHashes[i - 1][0]);
                //wholeHashes[i][1] = new ModifiableWholeHash(wholeHashes[i - 1][1]);
            }
        }
        PreXor[] xors = new PreXor[n];
        for (int i = 0; i < n; i++) {
            xors[i] = new PreXor(bits[i]);
        }
        int[] hashes = new int[n];
        for (int l = 0; l < m; l++) {
            for (int i = 0; i < n; i++) {
                wholeHashes[i][0].clear();
                //wholeHashes[i][1].clear();
            }
            for (int r = l; r < m; r++) {
                int invalidCnt = 0;
                for (int i = 0; i < n; i++) {
                    wholeHashes[i][0].modify(mat[i][r], 1);
                    //wholeHashes[i][1].modify(mat[i][r], 1);
                    int interval = (int) xors[i].intervalSum(l, r);
                    if (interval != Integer.lowestOneBit(interval)) {
                        invalidCnt++;
                        hashes[i] = -invalidCnt;
                        continue;
                    }
                    hashes[i] = wholeHashes[i][0].hash();//DigitUtils.asLong(wholeHashes[i][0].hash(), wholeHashes[i][1].hash());
                }
                int contrib = howMany(hashes, n);
                //System.err.printf("[%d,%d]=>%d\n", l, r, contrib);
                ans += contrib;
            }
        }

        //System.err.println(invoke);
        out.println(ans);
    }

    int[] d1 = new int[300];
    int[] d2 = new int[300];


    public int howMany(int[] hash, int n) {
        int ans = 0;
//        Arrays.fill(d1, 0);
//        Arrays.fill(d2, 0);
        Manacher.oddPalindrome(hash, n, d1);
        Manacher.evenPalindrome(hash, n, d2);
        for (int i = 0; i < n; i++) {
            if (hash[i] < 0) {
                continue;
            }
            ans += d1[i] + d2[i];
            //invoke++;
        }

        return ans;
    }
}
