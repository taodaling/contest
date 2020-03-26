#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

int MinOne(vector<int> vec) {
  if (vec.size() <= 0) {
    return 1e9;
  }
  sort(vec.begin(), vec.end());
  return vec[0];
}

int MinTwo(vector<int> vec) {
  if (vec.size() <= 1) {
    return 1e9;
  }
  sort(vec.begin(), vec.end());
  return vec[0] + vec[1];
}

int bf(int n, int m, vector<int> a) {
  int mask = (1 << m) - 1;
  int ans = 1e9;
  for (int i = 0; i < (1 << n); i++) {
    bool valid = true;
    for (int j = 0; j + m - 1 < n; j++) {
      if (bits::CountOne((unsigned int)(mask & (i >> j))) < 2) {
        valid = false;
        break;
      }
    }
    if (!valid) {
      continue;
    }
    int local = 0;
    for (int j = 0; j < n; j++) {
      if (bits::BitAt(i, j)) {
        local += a[j + 1];
      }
    }
    ans = min(ans, local);
  }
  return ans;
}

int solve(int n, int m, vector<int> a) {
  if (n == m) {
    return MinTwo(vector<int>(a.begin() + 1, a.end()));
  }
  if (n < 2 * m) {
    int l = 1;
    int lr = n - m + 1;
    int rl = m;
    int rr = n;
    dbg(l, lr, rl, rr);

    int ans1 = MinOne(vector<int>(a.begin() + l, a.begin() + lr)) +
               MinOne(vector<int>(a.begin() + lr, a.begin() + rl + 1)) +
               MinOne(vector<int>(a.begin() + rl + 1, a.end()));

    int ans2 = MinTwo(vector<int>(a.begin() + lr, a.begin() + rl + 1));

    int ans3 = MinTwo(vector<int>(a.begin() + l, a.begin() + lr)) +
               MinTwo(vector<int>(a.begin() + rl + 1, a.end()));

    return min(ans1, min(ans2, ans3));
  }

  vector<int> dp(n + 1, 1e9);
  vector<int> premin(n + 1, 1e9);
  vector<int> postmin(n + 1, 1e9);

  premin[1] = a[1];
  for (int i = 2; i <= n; i++) {
    premin[i] = min(premin[i - 1], a[i]);
  }

  postmin[n] = a[n];
  for (int i = n - 1; i >= 1; i--) {
    postmin[i] = min(postmin[i + 1], a[i]);
  }

  for (int i = 2; i <= m; i++) {
    dp[i] = a[i] + premin[i - 1];
  }

  for (int i = m + 1; i <= n - m; i++) {
    for (int j = i - 1; i - j < m; j--) {
      dp[i] = min(dp[i], a[i] + dp[j]);
    }
  }

  int ans = 1e9;
  for (int i = n - m + 1; i < n; i++) {
    for (int j = n - m; i - j < m; j--) {
      dp[i] = min(dp[i], a[i] + dp[j] + postmin[i + 1]);
    }
    ans = min(ans, dp[i]);
  }

  return ans;
}

void stress() {
  while (true) {
    int n = uniform_int_distribution<int>(2, 5)(rng);
    int m = uniform_int_distribution<int>(2, n)(rng);
    vector<int> a(n + 1);
    for (int i = 1; i <= n; i++) {
      a[i] = uniform_int_distribution<int>(1, 100)(rng);
    }
    int a1 = bf(n, m, a);
    int a2 = solve(n, m, a);
    if (a1 != a2) {
      dbg(n, m, a, a1, a2);
      exit(1);
    }
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;

  vector<int> a(n + 1);
  for (int i = 1; i <= n; i++) {
    in >> a[i];
  }

  //stress();
  dbg2(bf(n, m, a));
  out << solve(n, m, a);
}

RUN_ONCE