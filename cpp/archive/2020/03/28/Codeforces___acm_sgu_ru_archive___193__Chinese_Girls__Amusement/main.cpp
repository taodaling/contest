#include "../../libs/common.h"

void sub1(vector<int> &x) {
  for (int i = 0;; i++) {
    if (x[i] > 0) {
      x[i]--;
      return;
    }
    x[i] = 9;
  }
}

void div2(vector<int> &x) {
  int head = 0;
  for (int i = x.size() - 1; i >= 0; i--) {
    head = head * 10 + x[i];
    x[i] = head / 2;
    head %= 2;
  }
  assert(head == 0);
}

void solve(int testId, istream &in, ostream &out) {
  string s;
  in >> s;
  vector<int> bi(s.size());
  for (int i = 0; i < s.size(); i++) {
    bi[i] = s[i] - '0';
  }
  reverse(bi.begin(), bi.end());
  if (bi[0] % 2 == 1) {
    sub1(bi);
    div2(bi);
  } else {
    div2(bi);
    sub1(bi);
    if (bi[0] % 2 == 0) {
      sub1(bi);
    }
  }

  while (bi.size() > 1 && bi.back() == 0) {
    bi.pop_back();
  }

  reverse(bi.begin(), bi.end());
  for (int x : bi) {
    out << x;
  }
}

RUN_ONCE