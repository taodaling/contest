#include "../../libs/bigint.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

using BI = bigint::Bigint;

int SumOfDigit(ll x) {
  if (x == 0) {
    return 0;
  }
  return SumOfDigit(x / 10) + x % 10;
}

int Root(ll x) {
  if (x < 10) {
    return x;
  }
  return Root(SumOfDigit(x));
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  BI s = 0;
  BI x = 1;
  for (int i = 0; i < n; i++) {
    int y;
    in >> y;
    x *= y;
    s += x;
  }
  dbg(n);
  dbg(x);

  ll sum = 0;
  string str = to_string(s);
  for (char c : str) {
    sum += c - '0';
  }

  ll ans = Root(sum);
  out << ans << endl;
}

RUN_MULTI