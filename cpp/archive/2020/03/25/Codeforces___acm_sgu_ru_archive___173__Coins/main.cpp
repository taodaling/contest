#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/guass_elimination.h"
#include "../../libs/modmatrix.h"

using modular::Mod;
using namespace modmatrix;

void solve(int testId, istream &in, ostream &out) {
  int n, m, k, l;
  in >> n >> m >> k >> l;
  vector<pair<int, int>> ops(m);
  for (int i = 0; i < m; i++) {
    in >> ops[i].first >> ops[i].second;
    ops[i].first--;
  }

  guass_elimination::ModGussianElimination<int> mge(l, k - 1, 2);
  for (int i = 0; i < l; i++) {
    string src, dst;
    in >> src >> dst;
    mge.setRight(i, dst[k - 1] - src[0]);
    for (int j = 0; j < k - 1; j++) {
      mge.mat()[i][j] = src[j + 1] - '0';
    }
  }

  dbg(mge);
  assert(mge.solve());
  vector<int> &a = mge.solution();

  vector<vector<int>> t1 = UnitMatrix(k, 2);
  for (int i = 0; i < k - 1; i++) {
    t1[k - 1][i] = a[i];
  }
  vector<vector<int>> t2(k, vector<int>(k));
  for (int i = 0; i < k; i++) {
    t2[i][Mod(i - 1, k)] = 1;
  }

  vector<vector<int>> single = Transpose(Mul(t2, t1, 2));

  dbg(t1, t2, single);

  dbg(a);
  vector<int> state(n);

  for (int i = 0; i < n; i++) {
    char c;
    in >> c;
    state[i] = c - '0';
  }

  reverse(ops.begin(), ops.end());
  for (auto &p : ops) {
    dbg(state);
    int l = p.first;
    int r = l + k - 1;
    vector<int> extract(state.begin() + l, state.begin() + r + 1);
    vector<int> recover = Mul(vector<vector<int>>(1, extract), Pow(single, p.second, 2), 2)[0];
    for (int i = 0; i < k; i++) {
      state[i + l] = recover[i];
    }
  }

  for (int i = 0; i < n; i++) {
    out << state[i];
  }
}

RUN_ONCE