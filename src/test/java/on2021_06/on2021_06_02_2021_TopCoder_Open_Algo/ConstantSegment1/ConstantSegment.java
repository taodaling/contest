package on2021_06.on2021_06_02_2021_TopCoder_Open_Algo.ConstantSegment1;



import java.util.Arrays;

public class ConstantSegment {
    public int sendSomeHome(int N, int K, int M, int[] Pprefix, int seed) {
        int[] P = new int[N];

        int L = Pprefix.length;
        for (int i=0; i<L; ++i) P[i] = Pprefix[i];

        long state = seed;
        for (int i=L; i<N; ++i) {
            state = (state * 1103515245 + 12345) % (1L << 31);
            P[i] = (int)((state / 16) % M);
        }

        int[] registry = new int[M];
        int[] next = new int[N];
        Arrays.fill(registry, N);
        for(int i = N - 1; i >= 0; i--){
            next[i] = registry[P[i]];
            registry[P[i]] = i;
        }
        int[] head = registry;
        int inf = (int)1e9;
        int best = inf;
        for(int i = 0; i < M; i++){
            if(head[i] >= N){
                continue;
            }
            int cur = head[i];
            int to = head[i];
            int size = 1;
            while(size < K && next[to] < N){
                size++;
                to = next[to];
            }
            if(size < K){
                continue;
            }
            while(to < N){
                int len = to - cur + 1;
                int remove = len - K;
                best = Math.min(best, remove);


                cur = next[cur];
                to = next[to];
            }
        }

        return best == inf ? -1 : best;
    }
}
