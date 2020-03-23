#include "../../libs/common.h"
#include "../../libs/debug.h"

int Round(double x) { return (int)(x + 0.5); }

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> hs(n);
  vector<int> pos;
  vector<int> neg;
  pos.reserve(n);
  neg.reserve(n);

  for (int i = 0; i < n; i++) {
    double h;
    in >> h;
    hs[i] = Round(h * 1e6);
  }

  int thresold = 100000;
  for (int i = 0; i < n; i++) {
    hs[i] -= 2000000;
    if (hs[i] >= 0) {
      pos.push_back(i);
    } else {
      neg.push_back(i);
    }
  }

  dbg(hs);
  ll sum = 0;
  vector<int> ans;
  ans.reserve(n);
  while (!pos.empty()) {
    dbg(sum);
    if (sum + hs[pos.back()] < thresold) {
      sum += hs[pos.back()];
      ans.push_back(pos.back());
      pos.pop_back();
    } else {
      sum += hs[neg.back()];
      ans.push_back(neg.back());
      neg.pop_back();
    }
  }
  while (!neg.empty()) {
    ans.push_back(neg.back());
    neg.pop_back();
  }
  out << "yes" << endl;
  for (int x : ans) {
    out << x + 1 << ' ';
  }
}

RUN_ONCE