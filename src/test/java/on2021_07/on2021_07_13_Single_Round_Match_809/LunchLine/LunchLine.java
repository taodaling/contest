package on2021_07.on2021_07_13_Single_Round_Match_809.LunchLine;



import template.datastructure.LinkedListBeta;

public class LunchLine {
    public long simulate(int N, int M, int A, int B, int C, int D, int E) {
        int[] K = new int[M];
        if (M > 0) K[0] = A;
        if (M > 1) K[1] = B;
        for (int i = 2; i <= M - 1; i++) K[i] = (int) (((long) C * K[i - 1] + (long) D * K[i - 2] + E) % N);
        LinkedListBeta.Node<Integer>[] kids = new LinkedListBeta.Node[N];
        LinkedListBeta<Integer> list = new LinkedListBeta<>();
        for (int i = 0; i < N; i++) {
            kids[i] = new LinkedListBeta.Node<>(i);
            list.addLast(kids[i]);
        }
        for (int i = 0; i < M; i++) {
            list.remove(kids[K[i]]);
            list.addLast(kids[K[i]]);
        }
        long ans = 0;
        long index = 0;
        for(int x : list){
            ans += index * x;
            index++;
        }
        return ans;
    }
}
