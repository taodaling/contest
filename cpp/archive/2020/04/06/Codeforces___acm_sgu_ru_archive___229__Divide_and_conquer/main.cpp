#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/two_sat.h"

vector<vector<int>> Rotate(const vector<vector<int>> &vec) {
  int n = vec.size();
  vector<vector<int>> ans(n, vector<int>(n));
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      ans[n - 1 - j][i] = vec[i][j];
    }
  }
  return ans;
}

int n;

#ifndef LOCAL
two_sat::TwoSat ts(400);
#else
two_sat::TwoSat ts(16);
#endif

vector<vector<int>> a;
vector<vector<int>> b;
vector<pair<int, int>> invA;
vector<pair<int, int>> invB;

bool CheckIndex(int i, int j) { return i >= 0 && j >= 0 && i < n && j < n; }

bool solve(pair<int, int> aCenter, pair<int, int> bCenter) {
  ts.clear();
  ts.isFalse(ts.elementId(a[X(aCenter)][Y(aCenter)]));
  ts.isTrue(ts.elementId(b[X(bCenter)][Y(bCenter)]));
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (a[i][j] == -1) {
        continue;
      }
      // false if
      int ni = X(bCenter) - X(aCenter) + i;
      int nj = Y(bCenter) - Y(aCenter) + j;
      if (CheckIndex(ni, nj) && b[ni][nj] != -1) {
        ts.deduce(ts.negate(ts.elementId(a[i][j])), ts.elementId(b[ni][nj]));
      } else {
        ts.isTrue(ts.elementId(a[i][j]));
      }
    }
  }

  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (b[i][j] == -1) {
        continue;
      }
      // true if
      int ni = X(aCenter) - X(bCenter) + i;
      int nj = Y(aCenter) - Y(bCenter) + j;
      if (CheckIndex(ni, nj) && a[ni][nj] != -1) {
        ts.deduce(ts.elementId(b[i][j]), ts.negate(ts.elementId(a[ni][nj])));
      } else {
        ts.isFalse(ts.elementId(b[i][j]));
      }
    }
  }

  bool ans = ts.solve(true);
  if (ans) {
    dbg(aCenter, bCenter, ts);
  }
  return ans;
}

vector<pair<int, int>> Inv(const vector<vector<int>> &mat) {
  int n = mat.size();
  vector<pair<int, int>> ans(n * n, make_pair(-1, -1));
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (mat[i][j] != -1) {
        ans[mat[i][j]] = make_pair(i, j);
      }
    }
  }
  return ans;
}

void solve(int testId, istream &in, ostream &out) {
  in >> n;
  int sum = 0;
  a.resize(n, vector<int>(n, -1));
  int idAlloc = 0;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      char c;
      in >> c;
      if (c == '1') {
        a[i][j] = idAlloc++;
        sum++;
      }
    }
  }

  if (sum == 0) {
    out << "YES" << endl;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        out << 0;
      }
      out << endl;
    }
    return;
  }

  if (sum % 2) {
    out << "NO";
    return;
  }

  b = a;

  bool find = false;
  for (int t = 0; t < 3 && !find; t++) {
    invA = Inv(a);
    invB = Inv(b);

    dbg(a, b, invA, invB);

    int x = -1;
    int y = -1;
    for (int i = 0; i < n && x == -1; i++) {
      for (int j = 0; j < n && x == -1; j++) {
        if (a[i][j] != -1) {
          x = i;
          y = j;
          break;
        }
      }
    }
    dbg(x, y);

    // put at a
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (b[i][j] == -1) {
          continue;
        }
        find = find || solve(make_pair(x, y), make_pair(i, j));
      }
    }

    // put at b
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (a[i][j] == -1) {
          continue;
        }
        find = find || solve(make_pair(i, j), invB[a[x][y]]);
      }
    }

    b = Rotate(b);
  }

  if (!find) {
    out << "NO";
    return;
  }

  out << "YES" << endl;

  vector<vector<int>> ans(n, vector<int>(n));
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (a[i][j] == -1) {
        continue;
      }
      ans[i][j] = ts.valueOf(a[i][j]);
    }
  }

  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      out << ans[i][j];
    }
    out << endl;
  }
}

RUN_ONCE