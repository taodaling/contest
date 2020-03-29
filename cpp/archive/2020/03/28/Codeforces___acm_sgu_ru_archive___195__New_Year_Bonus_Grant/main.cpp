#include "../../libs/common.h"

vector<int> fathers;
vector<bool> used;


void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;

  fathers.resize(n);
  used.resize(n);
  for (int i = 1; i < n; i++) {
    in >> fathers[i];
    fathers[i]--;
  }
  vector<int> children;
  children.reserve(n);
  for (int i = n - 1; i >= 1; i--) {
    if (!used[i] && !used[fathers[i]]) {
      children.push_back(i);
      used[i] = used[fathers[i]] = true;
    }
  }
  out << children.size() * 1000 << endl;
  reverse(children.begin(), children.end());
  for (int x : children) {
    out << x + 1 << ' ';
  }
}

RUN_ONCE