#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/util.h"
#include "../../libs/debug.h"

const int MAX_STEP = 9;
const int validMask = (1 << MAX_STEP) - 1;

vector<int> Mul(vector<int> &a, vector<int> &b) {
  vector<int> ans(validMask + 1);
  for (int i = 0; i <= validMask; i++) {
    ans[i] = b[a[i]];
  }
  return ans;
}

vector<int> Pow(vector<int> &x, int n) {
  if (n == 0) {
    return util::Range(0, validMask);
  }
  vector<int> ans = Pow(x, n / 2);
  ans = Mul(ans, ans);
  if (n % 2) {
    ans = Mul(ans, x);
  }
  return ans;
}

void FirstWin(ostream &out, bool flag) {
  if (flag) {
    out << "FIRST PLAYER MUST WIN";
  } else {
    out << "SECOND PLAYER MUST WIN";
  }
  out << endl;
}

void solve(int testId, istream &in, ostream &out) {
  int n, m, mask;
  in >> n >> m;
  vector<int> step(m + 1);
  step[0] = 1;
  for (int i = 0; i < m; i++) {
    int t;
    in >> t;
    step[i + 1] = t;
  }
  vector<bool> dp(MAX_STEP);
  dp[0] = true;
  for (int i = 1; i < MAX_STEP; i++) {
    for (int s : step) {
      if (i - s >= 0 && !dp[i - s]) {
        dp[i] = true;
      }
    }
  }

  if (n < MAX_STEP) {
    FirstWin(out, dp[n]);
    return;
  }


  int top = 1 << (MAX_STEP - 1);
  mask = 0;
  for (int s : step) {
    mask = bits::SetBit(mask, MAX_STEP - s);
  }

  vector<int> f(validMask + 1);
  for (int i = 0; i <= validMask; i++) {
    f[i] = i >> 1;
   // int val = (~i) & mask;
    if ((~i) & mask) {
      f[i] |= top;
    }
  }

  vector<int> fn = Pow(f, n - MAX_STEP + 1);
  int init = 0;
  for (int i = 0; i < MAX_STEP; i++) {
    if (dp[i]) {
      init = bits::SetBit(init, i);
    }
  }

dbg(dp, init);
dbg(f[init]);
dbg(fn[init]);
  int final = fn[init];
  FirstWin(out, final & top);
}

RUN_MULTI