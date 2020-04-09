#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/radix.h"

const int ZERO = 50;
int a;
int b;
int dp[5 + 1][10 + 1][100 + 1][10][10][2][2];
radix::Radix<ll> rdx(10);

int Dp(int n, int i, int j, int inc, int dec, int ceil, int floor) {
  if (i == 0) {
    if (j == ZERO) {
      return false;
    }
    if (j < ZERO && j + inc >= ZERO) {
      return true;
    }
    if (j > ZERO && j - dec <= ZERO) {
      return true;
    }
    return false;
  }
  if (dp[n][i][j][inc][dec][ceil][floor] == -1) {
    dp[n][i][j][inc][dec][ceil][floor] = 0;
    int c = rdx.get(b, i - 1);
    int f = rdx.get(a, i - 1);
    int sign = i > n ? 1 : -1;
    int threshold = 0;
    if (i == n * 2) {
      threshold = 1;
    }
    for (int k = 0; k < 10; k++) {
      if (ceil && k > c || floor && k < f) {
        continue;
      }
      if (k > 0 && i > 2 * n) {
        continue;
      }
      if (k < threshold) {
        continue;
      }
      int ninc = inc;
      int ndec = dec;
      if (i <= 2 * n) {
        if (i > n) {
          ninc = max(inc, 9 - k);
          ndec = max(dec, k - threshold);
        } else {
          ninc = max(inc, k - threshold);
          ndec = max(dec, 9 - k);
        }
      }
      dp[n][i][j][inc][dec][ceil][floor] += Dp(
          n, i - 1, j + sign * k, ninc, ndec, ceil && k == c, floor && k == f);
    }
  }

  return dp[n][i][j][inc][dec][ceil][floor];
}

int solve(int aa, int bb) {
  a = aa;
  b = bb;

  C1(dp);

  // dbg2(Dp(1, 2, ZERO, 0, 0, 1, 1));

  int ans = 0;
  for (int i = 1; i <= 5; i++) {
    int local = Dp(i, 10, ZERO, 0, 0, 1, 1);
    // if (local) dbg(i, local);
    ans += local;
  }

  return ans;
}

int Check(int n) {
  stringstream ss;
  ss << n;
  string s = ss.str();
  if (s.size() & 1) {
    return 0;
  }
  int inc = 0;
  int dec = 0;
  int sum = 0;
  for (int i = 0; i < s.size() / 2; i++) {
    sum += s[i] - '0';
    inc = max(inc, '9' - s[i]);
    dec = max(dec, s[i] - (i == 0 ? '1' : '0'));
  }
  for (int i = s.size() / 2; i < s.size(); i++) {
    sum -= s[i] - '0';
    inc = max(inc, s[i] - '0');
    dec = max(dec, '9' - s[i]);
  }

  if (sum != 0 && sum + inc >= 0 && sum - dec <= 0) {
    return 1;
  }
  return 0;
}

int bf(int aa, int bb) {
  int sum = 0;
  for (int i = aa; i <= bb; i++) {
    sum += Check(i);
  }
  return sum;
}

void solve(int testId, istream &in, ostream &out) {

  // for (int i = 1;; i++) {
  //   if (bf(i, i) != solve(i, i)) {
  //     dbg(i);
  //     dbg2(bf(i, i));
  //     dbg2(solve(i, i));
  //     exit(-1);
  //   }
  // }

  int aa, bb;
  in >> aa >> bb;

  out << solve(aa, bb);
}

RUN_ONCE