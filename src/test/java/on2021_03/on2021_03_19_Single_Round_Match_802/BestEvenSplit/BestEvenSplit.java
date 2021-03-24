package on2021_03.on2021_03_19_Single_Round_Match_802.BestEvenSplit;



public class BestEvenSplit {
    int[] adj;
    int inf = (int) 1e9;

    int best = inf;
    int set;
    int way = 0;
    public void comb(int n, int remain, int cut) {
        if (remain > n + 1) {
            return;
        }
        if (n < 0) {
            if(cut < best){
                best = cut;
                way = 0;
            }
            way++;
            return;
        }
        //choose
        if (remain > 0) {
            set ^= 1 << n;
            comb(n - 1, remain - 1, cut + Integer.bitCount(~set & adj[n]));
            set ^= 1 << n;
        }
        comb(n - 1, remain, cut + Integer.bitCount(set & adj[n]));
    }

    public int count(int N, int[] X, int[] Y) {
        adj = new int[N];
        for (int i = 0; i < X.length; i++) {
            int u = X[i];
            int v = Y[i];
            adj[u] ^= 1 << v;
        }
        comb(N - 1, N / 2, 0);
        return way;
    }
}
