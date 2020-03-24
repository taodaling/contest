#ifndef MODMATRIX_H
#define MODMATRIX_H

#include "common.h"
#include "modular.h"
namespace modmatrix {
using namespace modular;

template <class T>
vector<vector<T>> UnitMatrix(int n, T mod) {
  int one = Mod(1, mod);
  vector<vector<T>> ans(n, vector<T>(n));
  for (int i = 0; i < n; i++) {
    ans[i][i] = one;
  }
  return ans;
}

template <class T>
vector<vector<T>> Mul(const vector<vector<T>> &a, const vector<vector<T>> &b,
                      T mod) {
  assert(a[0].size() == b.size());
  int n = a.size();
  int m = b[0].size();
  int mid = b.size();
  vector<vector<T>> c(n, vector<T>(m));
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
      for (int k = 0; k < mid; k++) {
        c[i][j] = Mod(c[i][j] + Modmul(a[i][k], b[k][j], mod), mod);
      }
    }
  }
  return c;
}

template <class T>
vector<vector<int>> Transpose(const vector<vector<T>> &a) {
  int n = a.size();
  int m = a[0].size();
  vector<vector<T>> ans(m, vector<T>(n));
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
      ans[j][i] = a[i][j];
    }
  }
  return ans;
}

template <class T>
vector<vector<T>> Pow(const vector<vector<T>> &x, ll n, T mod) {
  if (n == 0) {
    return UnitMatrix(x.size(), mod);
  }
  vector<vector<T>> ans = Pow(x, n / 2, mod);
  ans = Mul(ans, ans, mod);
  if (n & 1) {
    ans = Mul(ans, x, mod);
  }
  return ans;
}
}  // namespace modmatrix

#endif