package template.math;

import template.datastructure.BitSet;

public class Mod2GuassianElimination {
    BitSet[] mat;
    BitSet solutions;
    int rank;
    int n;
    int m;
    int start;

    public int rank() {
        return rank;
    }

    public Mod2GuassianElimination(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new BitSet[n + 1];
        for (int i = 0; i <= n; i++) {
            mat[i] = new BitSet(m + 1);
        }
        solutions = mat[n];
    }

    public void clear(int n, int m) {
        this.n = n;
        this.m = m;
        for (int i = 0; i <= n; i++) {
            mat[i].fill(false);
        }
        solutions = mat[n];
    }

    public void setRight(int row, int val) {
        if((val & 1) == 1){
            mat[row].flip(m);
        }
    }

    public void setLeft(int row, int col, int val) {
        if((val & 1) == 1){
            mat[row].set(col);
        }else{
            mat[row].clear(col);
        }
    }

    public int getLeft(int row, int col) {
        return mat[row].get(col) ? 1 : 0;
    }

    public int getRight(int row) {
        return mat[row].get(m) ? 1 : 0;
    }

    public void modifyLeft(int row, int col, int val) {
        if((val & 1) == 1) {
            mat[row].flip(col);
        }
    }


    public void modifyRight(int row, int val) {
        if((val & 1) == 1) {
            mat[row].flip(m);
        }
    }

    public BitSet getSolutions() {
        return solutions;
    }

    /**
     * O(nm^2/32)
     *
     * @return
     */
    public boolean solve() {
        int now = 0;
        for (int i = 0; i < m; i++) {
            start = i;
            int maxRow = now;
            for (int j = now; j < n; j++) {
                if (mat[j].get(i)) {
                    maxRow = j;
                    break;
                }
            }
            if (!mat[maxRow].get(i)) {
                continue;
            }
            swapRow(now, maxRow);
            for (int j = now + 1; j < n; j++) {
                if (!mat[j].get(i)) {
                    continue;
                }
                subtractRow(j, now);
            }

            now++;
        }

        rank = now;
        for (int i = now; i < n; i++) {
            if (mat[i].get(m)) {
                return false;
            }
        }

        for (int i = now - 1; i >= 0; i--) {
            int x = -1;
            for (int j = 0; j < m; j++) {
                if (mat[i].get(j)) {
                    x = j;
                    break;
                }
            }
            if(mat[i].get(m)){
                mat[n].set(x);
            }
            if(mat[n].get(x)) {
                for (int j = i - 1; j >= 0; j--) {
                    if (!mat[j].get(x)) {
                        continue;
                    }
                    mat[j].flip(m);
                    mat[j].clear(x);
                }
            }
        }
        return true;
    }

    void swapRow(int i, int j) {
        BitSet row = mat[i];
        mat[i] = mat[j];
        mat[j] = row;
    }

    void subtractRow(int i, int j) {
        mat[i].xor(mat[j]);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < m; j++) {
                if (!mat[i].get(j)) {
                    continue;
                }
                row.append("x").append(j).append('+');
            }
            if (row.length() > 0) {
                row.setLength(row.length() - 1);
            } else {
                row.append(0);
            }
            row.append("=").append(mat[i].get(m) ? 1 : 0);
            builder.append(row).append('\n');
        }
        return builder.toString();
    }
}
