#include "../../libs/binary_search.h"
#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

const int MAX_BIT = 1000;

ll dp[MAX_BIT][4][2][2];

ll Dp(const vector<byte> &top, int i, int j, int ceil, int floor) {
  if (j > 3) {
    return 0;
  }
  if (i < 0) {
    return j == 3 ? 1 : 0;
  }
  if (dp[i][j][ceil][floor] == -1) {
    dp[i][j][ceil][floor] = 0;
    int t = top[i];
    int b = 0;
    for (int k = 0; k < 2; k++) {
      if (ceil && k > t) {
        continue;
      }
      if (floor && k < b) {
        continue;
      }
      dp[i][j][ceil][floor] +=
          Dp(top, i - 1, j + k, ceil && k == t, floor && k == b);
    }
  }
  return dp[i][j][ceil][floor];
}

ll Count(const vector<byte> &top) {
  // Count how many number in 1, ... , n contian exactly 3 bit
  C1(dp);
  ll ans = Dp(top, MAX_BIT - 1, 0, 1, 1);
  // dbg(dp);
  return ans;
}

vector<byte> rightShift(const vector<byte> &data) {
  vector<byte> ans(MAX_BIT);
  for (int i = 0; i < MAX_BIT - 1; i++) {
    ans[i + 1] = data[i];
  }
  return ans;
}

vector<byte> leftShift(const vector<byte> &data) {
  vector<byte> ans(MAX_BIT);
  for (int i = 1; i < MAX_BIT; i++) {
    ans[i - 1] = data[i];
  }
  return ans;
}

vector<byte> plus(const vector<byte> &a, const vector<byte> &b) {
  vector<byte> ans(MAX_BIT);
  int up = 0;
  for (int i = 0; i < MAX_BIT; i++) {
    up += a[i] + b[i];
    ans[i] = up % 2;
    up /= 2;
  }
  return ans;
}

int Compare(const vector<byte> &a, const vector<byte> &b) {
  for (int i = 0; i < MAX_BIT; i++) {
    if (a[i] != b[i]) {
      return (int)a[i] - b[i];
    }
  }
  return 0;
}

ll CountK(const vector<byte> &k) { return Count(rightShift(k)) - Count(k); }
ll BruteForce(ll k) {
  int ans = 0;
  for (int i = 0; i <= k; i++) {
    if (bits::CountOne((ui)i) == 3) {
      ans++;
    }
  }
  return ans;
}

void solve(int testId, istream &in, ostream &out) {


  // Count(7);
  // exit(-1);

  // for (int i = 1; i <= 100; i++) {
  //   if (BruteForce(i) != Count(i)) {
  //     dbg(i, BruteForce(i), Count(i));
  //     exit(-1);
  //   }
  // }

  ll m;
  in >> m;

  vector<byte> left(MAX_BIT);
  vector<byte> right(MAX_BIT, 1);
  vector<byte> one(MAX_BIT);
  one[0] = 1;
  right[MAX_BIT - 1] = 0;
  right[MAX_BIT - 2] = 0;
  right[MAX_BIT - 3] = 0;
  right[MAX_BIT - 4] = 0;

  dbg(CountK(right));

  while (Compare(left, right) < 0) {
    vector<byte> mid = leftShift(plus(left, right));
    if (CountK(mid) >= m) {
      right = mid;
    } else {
      left = plus(mid, one);
    }
  }

  if (CountK(left) != m || CountK(plus(left, one)) == m) {
    out << "NO" << endl;
    return;
  }
  out << "YES" << endl;
}

RUN_MULTI