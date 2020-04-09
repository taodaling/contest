#include "../../libs/common.h"
#include "../../libs/debug.h"

vector<pair<int, int>> props;
int dp[500][11][11][11][11];

int Dp(int i, int d1, int d2, int d3, int d4) {
  if (i <= 0) {
    return 0;
  }
  if (dp[i][d1][d2][d3][d4] == -1) {
    dp[i][d1][d2][d3][d4] = Dp(i - 1, 10, d1, d2, d3);
    for (int j = 0; j < props.size(); j++) {
      int add = X(props[j]);
      int rest = Y(props[j]);
      if (d1 == j) {
        continue;
      }
      if (d2 == j && rest > 1) {
        continue;
      }
      if (d3 == j && rest > 2) {
        continue;
      }
      if (d4 == j && rest > 3) {
        continue;
      }
      dp[i][d1][d2][d3][d4] =
          max(dp[i][d1][d2][d3][d4], Dp(i - 1, j, d1, d2, d3) + add);
    }
  }

  return dp[i][d1][d2][d3][d4];
}

void solve(int testId, istream &in, ostream &out) {
  int m, n;
  in >> m >> n;
  props.resize(n);
  for (int i = 0; i < n; i++) {
    in >> X(props[i]) >> Y(props[i]);
  }

  C1(dp);

  for (int i = 0;; i++) {
    if (Dp(i, 10, 10, 10, 10) >= m) {
      out << i;
      return;
    }
  }
}

RUN_ONCE