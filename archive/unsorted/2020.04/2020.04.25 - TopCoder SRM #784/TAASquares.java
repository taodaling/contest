package contest;

public class TAASquares {
    public String[] construct(int N) {
        int[][] mat = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                mat[i][j] = 2;
            }
        }
        for (int i = 0; i < N; i++) {
            mat[i][i] = i < N / 2 ? 1 : 0;
        }

        String[] ans = new String[N];
        for(int i = 0; i < N; i++){
            StringBuilder builder = new StringBuilder();
            for(int j = 0; j < N; j++){
                builder.append(mat[i][j]);
            }
            ans[i] = builder.toString();
        }
        return ans;
    }
}
