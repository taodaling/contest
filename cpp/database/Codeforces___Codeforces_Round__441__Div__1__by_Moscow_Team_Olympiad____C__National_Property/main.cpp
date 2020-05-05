#include "../../libs/common.h"
#include "../../libs/reader.h"
#include "../../libs/two_sat.h"

using two_sat::TwoSat;
vector<vector<int>> s;

bool valid;

void order(int l, int r, int index, TwoSat &ts) {
  if (l >= r) {
    return;
  }

  for (int i = l; i <= r && valid; i++) {
    int j = i;
    int v = s[j][index];
    while (j + 1 <= r && s[j + 1][index] == v) {
      j++;
    }
    if (v != -1) {
      order(i, j, index + 1, ts);
    }
    if (j + 1 <= r) {
      int next = s[j + 1][index];
      if (next == -1) {
        valid = false;
        continue;
      }
      if (v == -1) {
        continue;
      }
      if (v < next) {
        ts.deduce(ts.elementId(next), ts.elementId(v));
      } else {
        ts.isTrue(ts.elementId(v));
        ts.isFalse(ts.elementId(next));
      }
    }
  }
}

void solve(int testId, istream &is, ostream &out) {
  valid = true;
  reader::Input in(is);
  int n = in.ri();
  int m = in.ri();
  s.resize(n);
  for (int i = 0; i < n; i++) {
    int len = in.ri();
    s[i].resize(len + 1);
    for (int j = 0; j < len; j++) {
      s[i][j] = in.ri() - 1;
    }
    s[i][len] = -1;
  }

  TwoSat ts(m);
  order(0, n - 1, 0, ts);

  valid = valid && ts.solve(true);
  if (!valid) {
    out << ("No") << endl;
    return;
  }
  out << ("Yes") << endl;
  vector<int> capital;
  capital.reserve(m);
  for (int i = 0; i < m; i++) {
    if (ts.valueOf(i)) {
      capital.push_back(i);
    }
  }
  out << (capital.size()) << endl;
  for (int i = 0; i < capital.size(); i++) {
    out << (capital[i] + 1) << ' ';
  }
}

RUN_ONCE