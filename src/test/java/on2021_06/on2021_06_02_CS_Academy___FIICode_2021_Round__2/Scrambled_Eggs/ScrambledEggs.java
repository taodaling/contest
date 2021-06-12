package on2021_06.on2021_06_02_CS_Academy___FIICode_2021_Round__2.Scrambled_Eggs;



import template.graph.DinicBipartiteMatch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.MultiplicativeFunctionSieve;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class ScrambledEggs {
    MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve((int) 1e7);
    int[] minFactor = sieve.getSmallestPrimeFactor();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int x = in.ri();
        IntegerArrayList allPossiblePrimes = new IntegerArrayList(8 * n);
        int[] a = in.ri(n);
        IntegerArrayList[] allPrimes = new IntegerArrayList[n];
        for (int i = 0; i < n; i++) {
            allPrimes[i] = new IntegerArrayList(8);
            int y = a[i];
            while (y > 1) {
                int f = minFactor[y];
                while (y % f == 0) {
                    y /= f;
                }
                allPossiblePrimes.add(f);
                allPrimes[i].add(f);
            }
        }
        allPossiblePrimes.unique();
        int[] occur = new int[allPossiblePrimes.size()];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < allPrimes[i].size(); j++) {
                allPrimes[i].set(j, allPossiblePrimes.binarySearch(allPrimes[i].get(j)));
                occur[allPrimes[i].get(j)]++;
            }
        }
        int hit = -1;
        for (int i = 0; i < occur.length; i++) {
            if (occur[i] >= x) {
                hit = i;
                break;
            }
        }

        int cntPrime = allPossiblePrimes.size();
        int R = cntPrime + 1;
        int L = n;
        DinicBipartiteMatch bm = new DinicBipartiteMatch(L, R, i -> 1, i -> i == R - 1 ? n - k : x);
        for(int i = 0; i < n; i++){
            for (int j = 0; j < allPrimes[i].size(); j++) {
                int v = allPrimes[i].get(j);
                bm.addEdge(i, v);
            }
            bm.addEdge(i, cntPrime);
        }
        int flow = bm.solve();
        debug.debug("flow", flow);
        debug.debug("hit", hit);
        if (flow < n || hit == -1) {
            out.println("IMPOSSIBLE");
            return;
        }
        size = new int[cntPrime + 1];
        mate = new int[L];
        for (int i = 0; i < L; i++) {
            mate[i] = bm.mateOfLeft(i);
            size[mate[i]]++;
        }
        for(int i = 0; i < L && size[hit] < x; i++){
            boolean ok = false;
            for(int j = 0; j < allPrimes[i].size(); j++){
                int v = allPrimes[i].get(j);
                if(v == hit){
                    ok = true;
                    break;
                }
            }
            if(ok){
                migrate(i, hit);
            }
        }
        for(int i = 0; i < L && size[cntPrime] < n - k; i++){
            if(mate[i] == hit){
                continue;
            }
            migrate(i, cntPrime);
        }
        allPossiblePrimes.add(-1);
        for(int i = 0; i < n; i++){
            int p = allPossiblePrimes.get(mate[i]);
            out.append(p).append(' ');
        }
    }

    Debug debug = new Debug(false);
    int[] size;
    int[] mate;

    void migrate(int i, int j){
        size[mate[i]]--;
        size[mate[i] = j]++;
    }
}
