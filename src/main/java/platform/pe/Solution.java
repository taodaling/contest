package platform.pe;

import framework.io.FileUtils;
import net.egork.chelper.tester.StringInputStream;
import template.algo.IntBinarySearch;
import template.binary.Bits;
import template.io.FastInput;
import template.math.*;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongHashSet;
import template.primitve.generated.datastructure.LongIterator;
import template.utils.Debug;

import java.io.File;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.*;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;

public class Solution {
    Debug debug = new Debug(true);

    public int extract(String s, int mask) {
        int ans = 0;
        int val = -1;
        for (int i = 0; i < s.length(); i++) {
            int v = s.charAt(i) - '0';
            if (Bits.get(mask, i) == 1) {
                if (val == -1) {
                    val = v;
                }
                if (val != v) {
                    return -1;
                }
            } else {
                ans = ans * 10 + v;
            }
        }
        return ans;
    }

    public int[] decompose(int x) {
        int[] ans = new int[10];
        while (x != 0) {
            ans[x % 10]++;
            x /= 10;
        }
        return ans;
    }

    public void solve() {
        //String s = FileUtils.readFile(new File("C:\\Users\\dalt\\Downloads"), "p042_words.txt");
        //FastInput in = new FastInput(new StringInputStream(s.replaceAll("[^A-Z]", " ")));

        int threshold = (int) 1e7;
        for (int i = 1; i <= threshold; i++) {
            int[] sig = decompose(i);
            boolean valid = true;
            for (int j = 2; j <= 6 && valid; j++) {
                if (!Arrays.equals(sig, decompose(j * i))) {
                   valid = false;
                }
            }
            if(valid){
                debug.debug("ans", i);
                return;
            }
        }
    }

    public static void main(String[] args) {
        new Solution().solve();
    }
}
