package on2020_06.on2020_06_09_Codeforces___Codeforces_Round__412__rated__Div__1__based_on_VK_Cup_2017_Round_3_.C__Prairie_Partition;



import template.algo.IntBinarySearch;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongList;
import template.utils.CompareUtils;

public class CPrairiePartition {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        int[] cnts = new int[50];
        LongList special = new LongList(n);

        in.populate(a);
        for (long x : a) {
            if (Long.lowestOneBit(x) == x) {
                cnts[Log2.floorLog(x)]++;
            } else {
                special.add(x);
            }
        }
        special.sort();

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                int one = cnts[0] - mid;
                int specialIndex = 0;

                int flow = mid;
                int taged = 0;
                for (int i = 1; i < cnts.length; i++) {
                    int c = cnts[i];
                    if (c <= flow) {
                        int kickOut = flow - c;
                        int kickTaged = Math.min(taged, kickOut);
                        taged -= kickTaged;
                        int kickUnTaged = kickOut - kickTaged;
                        flow -= kickOut;


                        while (kickUnTaged > 0 && specialIndex < special.size() &&
                                special.get(specialIndex) <= (1L << i)) {
                            kickUnTaged--;
                            specialIndex++;
                        }
                        while (kickUnTaged > 0 && one > 0) {
                            kickUnTaged--;
                            one--;
                        }
                    } else {
                        c -= flow;
                        taged += c;
                    }
                }

                int kickOut = flow - taged;
                return kickOut >= special.size() - specialIndex + one;
            }
        };

        int ans = ibs.binarySearch(0, cnts[0]);
        if (!ibs.check(ans)) {
            out.println(-1);
            return;
        }

        for (int i = ans; i <= cnts[0]; i++) {
            out.append(i).append(' ');
        }
    }
}
