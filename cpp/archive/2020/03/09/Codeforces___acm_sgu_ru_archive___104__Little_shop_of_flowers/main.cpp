#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int f, v;
  in >> f >> v;
  vector<vector<int>> mat(v + 1, vector<int>(f + 1));
  for (int i = 1; i <= f; i++) {
    for (int j = 1; j <= v; j++) {
      in >> mat[j][i];
    }
  }
  vector<vector<int>> dp(v + 1, vector<int>(f + 1, (int)-1e9));
  vector<vector<int>> match(v + 1, vector<int>(f + 1));
  dp[0][0] = 0;
  for(int i = 1; i <= v; i++){
    for(int j = 0; j <= f; j++){
      dp[i][j] = dp[i - 1][j];
      if (j > 0 && dp[i][j] < dp[i - 1][j - 1] + mat[i][j]) {
        dp[i][j] = dp[i - 1][j - 1] + mat[i][j];
        match[i][j] = 1;
      }
    }
  }

  out << dp[v][f] << endl;
  vector<int> ans(f + 1);
  pair<int, int> state(v, f);
  while(state.first > 0){
    if(match[state.first][state.second]){
      ans[state.second] = state.first;
      state.second--;
    }
    state.first--;
  }

  for(int i = 1; i <= f; i++){
    out << ans[i] << ' ';
  }
}

RUN_ONCE