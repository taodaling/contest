#include "../../libs/common.h"

const int MAX_N = 15;

int dp[MAX_N + 1][MAX_N + 1][MAX_N + 1][2][2][MAX_N * MAX_N];
byte lastL[MAX_N + 1][MAX_N + 1][MAX_N + 1][2][2][MAX_N * MAX_N];
byte lastR[MAX_N + 1][MAX_N + 1][MAX_N + 1][2][2][MAX_N * MAX_N];
byte lastDl[MAX_N + 1][MAX_N + 1][MAX_N + 1][2][2][MAX_N * MAX_N];
byte lastDr[MAX_N + 1][MAX_N + 1][MAX_N + 1][2][2][MAX_N * MAX_N];
int mat[MAX_N + 1][MAX_N + 1];

int Interval(int i, int l, int r) { return mat[i][r] - mat[i][l - 1]; }

int n;
int m;

int Query(int row, int l, int r, int dl, int dr, int k) {
  int len = r - l + 1;
  int sum = Interval(row, l, r);
  if (row == 0) {
    return 0;
  }
  if (len > k || l > r || l < 0 || r > n) {
    return -1e8;
  }

#define INDEX row][l][r][dl][dr][k
  if (dp[INDEX] == -1) {
    dp[INDEX] = sum;
    lastL[INDEX] = -1;
    lastR[INDEX] = -1;

    for (int i = 0; i <= dl; i++) {
      for (int j = 1; j >= dr; j--) {
        int cand = Query(row - 1, l, r, i, j, k - len) + sum;
        if (cand > dp[INDEX]) {
          dp[INDEX] = cand;
          lastL[INDEX] = l;
          lastR[INDEX] = r;
          lastDl[INDEX] = i;
          lastDr[INDEX] = j;
        }
      }
    }
    if (dl == 1) {
      int cand =
          Query(row, l - 1, r, dl, dr, k + 1) - Interval(row, l - 1, l - 1);

      if (cand > dp[INDEX]) {
        dp[INDEX] = cand;
        lastL[INDEX] = lastL[row][l - 1][r][dl][dr][k + 1];
        lastR[INDEX] = lastR[row][l - 1][r][dl][dr][k + 1];
        lastDl[INDEX] = lastDl[row][l - 1][r][dl][dr][k + 1];
        lastDr[INDEX] = lastDr[row][l - 1][r][dl][dr][k + 1];
      }
    }
    if (dl == 0) {
      int cand = Query(row, l + 1, r, dl, dr, k - 1) + Interval(row, l, l);

      if (cand > dp[INDEX]) {
        dp[INDEX] = cand;
        lastL[INDEX] = lastL[row][l + 1][r][dl][dr][k - 1];
        lastR[INDEX] = lastR[row][l + 1][r][dl][dr][k - 1];
        lastDl[INDEX] = lastDl[row][l + 1][r][dl][dr][k - 1];
        lastDr[INDEX] = lastDr[row][l + 1][r][dl][dr][k - 1];
      }
    }
    if (dr == 0) {
      int cand =
          Query(row, l, r + 1, dl, dr, k + 1) - Interval(row, r + 1, r + 1);
      if (cand > dp[INDEX]) {
        dp[INDEX] = cand;
        lastL[INDEX] = lastL[row][l][r + 1][dl][dr][k + 1];
        lastR[INDEX] = lastR[row][l][r + 1][dl][dr][k + 1];
        lastDl[INDEX] = lastDl[row][l][r + 1][dl][dr][k + 1];
        lastDr[INDEX] = lastDr[row][l][r + 1][dl][dr][k + 1];
      }
    }
    if (dr == 1) {
      int cand = Query(row, l, r - 1, dl, dr, k - 1) + Interval(row, r, r);
      if (cand > dp[INDEX]) {
        dp[INDEX] = cand;
        lastL[INDEX] = lastL[row][l][r - 1][dl][dr][k - 1];
        lastR[INDEX] = lastR[row][l][r - 1][dl][dr][k - 1];
        lastDl[INDEX] = lastDl[row][l][r - 1][dl][dr][k - 1];
        lastDr[INDEX] = lastDr[row][l][r - 1][dl][dr][k - 1];
      }
    }
  }
  return dp[INDEX];
#undef INDEX
}

void solve(int testId, istream &in, ostream &out) {
  int k;
  in >> n >> m >> k;
  C1(dp);
  for (int i = 1; i <= n; i++) {
    for (int j = 1; j <= m; j++) {
      in >> mat[i][j];
      mat[i][j] += mat[i][j - 1];
    }
  }

  int ans = -1;
  int row_ = -1;
  int r_ = -1;
  int l_ = -1;
  int dl_ = -1;
  int dr_ = -1;

  for (int i = 1; i <= n; i++) {
    for (int l = 1; l <= m; l++) {
      for (int r = 1; r <= m; r++) {
        for (int dl = 0; dl < 2; dl++) {
          for (int dr = 0; dr < 2; dr++) {
            int cand = Query(i, l, r, dl, dr, k);
            if (cand > ans) {
              ans = cand;
              row_ = i;
              l_ = l;
              r_ = r;
              dl_ = dl;
              dr_ = dr;
            }
          }
        }
      }
    }
  }

  out << "Oil : " << ans << endl;
  while(l_ != -1){
    for(int i = l_; i <= r_; i++){
      out << row_ << ' ' << i << endl;
    }
    
  }
}

RUN_ONCE