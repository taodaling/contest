package on2021_11.on2021_11_02_Codeforces___Codeforces_Round__752__Div__1_.C__Extreme_Extension;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.utils.Debug;

import java.util.Arrays;

public class CExtremeExtension {
    int mod = 998244353;
    int L = (int) 1e5;
    IntegerArrayList prevList = new IntegerArrayList(L);
    IntegerArrayList nextList = new IntegerArrayList(L);
//    IntegerHashMap prevListMap = new IntegerHashMap(L, false);
//    IntegerHashMap nextListMap = new IntegerHashMap(L, false);
    long[] prevWay = new long[L];
    long[] nextWay = new long[L];
    long[] prevCost = new long[L];
    long[] nextCost = new long[L];

    public void process(int x, IntegerArrayList list) {
        list.clear();
        for (int i = 1, r; i <= x; i = r + 1) {
            int v = x / i;
            r = x / v;
            assert r >= i;
            list.add(v);
        }
        list.reverse();
    }

    public int f(int x, int m) {
        return x / ((x + m - 1) / m);
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        prevList.clear();
//        prevListMap.clear();
        long sumOfCost = 0;

        for (int i = n - 1; i >= 0; i--) {
            process(a[i], nextList);
            debug.debug("i", i);
            debug.debug("nextList", nextList);
            int m = nextList.size();
            for (int j = 0; j < m; j++) {
                nextWay[j] = 0;
                nextCost[j] = 0;
            }
            int[] prevListData = prevList.getData();
            int prevSize = prevList.size();
            int[] nextListData = nextList.getData();
            int iter = 0;
            for (int j = 0; j < prevSize; j++) {
                int back = prevListData[j];
                int block = (a[i] + back - 1) / back;
                int f = a[i] / block;
                while(nextListData[iter] < f){
                    iter++;
                }
                int findex = iter;
                nextWay[findex] += prevWay[j];
                nextCost[findex] += prevCost[j] + prevWay[j] * (block - 1);
            }

            //add new end
            int findex = nextList.size() - 1;
            nextWay[findex]++;
            for(int j = 0; j < m; j++){
                nextCost[j] %= mod;
                nextWay[j] %= mod;
                sumOfCost += nextCost[j];
            }
            debug.run(() -> {
                debug.debug("nextWay", Arrays.copyOf(nextWay, m));
                debug.debug("nextCost", Arrays.copyOf(nextCost, m));
            });

            {
                IntegerArrayList tmp = prevList;
                prevList = nextList;
                nextList = tmp;
            }
//            {
//                IntegerHashMap tmp = prevListMap;
//                prevListMap = nextListMap;
//                nextListMap = tmp;
//            }
            {
                long[] tmp = prevCost;
                prevCost = nextCost;
                nextCost = tmp;
            }
            {
                long[] tmp = prevWay;
                prevWay = nextWay;
                nextWay = tmp;
            }
        }

        sumOfCost = DigitUtils.mod(sumOfCost, mod);
        out.println(sumOfCost);
    }
}
