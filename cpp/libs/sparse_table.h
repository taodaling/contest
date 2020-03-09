#ifndef SPARSE_TABLE_H
#define SPARSE_TABLE_H

#include "bits.h"
#include "common.h"

namespace sparse_table {
template <class T>
class SparseTable {
 private:
  vector<vector<T>> _levels;
  int _n;
  function < T(const T &, const T &) _func;

 public:
  void init(vector<T> &data, function<T(const T, const T)> func) {
    _n = data.size();
    int log = bits::FloorLog2(n);
    _levels.resize(log + 1, data);
    for (int i = 1; i <= log; i++) {
      int step = 1 << (i - 1);
      for (int j = 0; j < _n; j++) {
        int next = min(j + step, _n - 1);
        _levels[i][j] = func(_levels[i - 1][j], _levels[i - 1][next]);
      }
    }
  }

  T operator()(int l, int r) {
    int log = bits::FloorLog2(r - l + 1);
    return _func(_levels[log][l], _levels[log][r + 1 - (1 << log)]);
  }
};
}  // namespace sparse_table

#endif