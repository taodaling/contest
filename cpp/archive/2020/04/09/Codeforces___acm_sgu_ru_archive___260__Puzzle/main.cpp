#include "../../libs/common.h"
#include "../../libs/guass_elimination.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  guass_elimination::ModGussianElimination<int> zero(n, n, 2);
  for (int i = 0; i < n; i++) {
    int m;
    in >> m;
    for (int j = 0; j < m; j++) {
      int x;
      in >> x;
      x--;
      zero.mat()[x][i] = 1;
    }
  }

  guass_elimination::ModGussianElimination<int> one = zero;
  for (int i = 0; i < n; i++) {
    int x;
    in >> x;
    zero.setRight(i, x);
    one.setRight(i, 1 - x);
  }

  bool find = false;
  vector<int> req;
  if (zero.solve()) {
    find = true;
    for (int i = 0; i < n; i++) {
      if (zero.solution()[i]) {
        req.push_back(i);
      }
    }
  }
  if (!find && one.solve()) {
    find = true;
    for (int i = 0; i < n; i++) {
      if (one.solution()[i]) {
        req.push_back(i);
      }
    }
  }

  if (!find) {
    out << -1;
    return;
  }

  out << req.size() << endl;
  for (int x : req) {
    out << x + 1 << ' ';
  }
}

RUN_ONCE