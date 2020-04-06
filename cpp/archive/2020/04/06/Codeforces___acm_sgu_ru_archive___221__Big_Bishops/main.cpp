#include "../../libs/bigint.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

using bi = bigint::BigInt;

vector<bi> solve(vector<int> row) {
  int n = row.size();
  sort(row.begin(), row.end());
  vector<bi> ans(n + 1);
  ans[0] = 1;
  for (int x : row) {
    for (int i = n; i >= 0; i--) {
      if (i > 0) {
        ans[i] += ans[i - 1] * (x - (i - 1));
      }
    }
  }
  return ans;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  int k;
  in >> n >> k;

  vector<int> len(2 * n - 1);
  for (int i = 0; i <= n - 1; i++) {
    len[i] = i + 1;
  }
  for (int i = n; i < 2 * n - 1; i++) {
    len[i] = len[i - 1] - 1;
  }

  vector<int> rows[2];
  for (int i = 0; i < 2 * n - 1; i++) {
    rows[i & 1].push_back(len[i]);
  }

  dbg(len, vector<vector<int>>{rows[0], rows[1]});

  vector<bi> sum1 = solve(rows[0]);
  vector<bi> sum2 = solve(rows[1]);

  dbg(sum1, sum2);

  sum1.resize(k + 1);
  sum2.resize(k + 1);
  bi ans = 0;
  for (int i = 0; i <= k; i++) {
    ans += sum1[i] * sum2[k - i];
  }

  out << ans;
}

RUN_ONCE