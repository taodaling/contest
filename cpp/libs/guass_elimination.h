#ifndef GUASS_ELIMINATION_H
#define GUASS_ELIMINATION_H

#include "modular.h"

namespace guass_elimination {
using namespace modular;
template <class T>
class ModGussianElimination {
 private:
  vector<vector<T>> _mat;
  int _rank;
  int _n;
  int _m;
  T _mod;
  template<class V>
  friend ostream& operator<<(ostream& os, const ModGussianElimination<V>& e);

 public:
  ModGussianElimination(int n, int m, T modular) {
    _n = n;
    _m = m;
    _mat.resize(n + 1, vector<T>(m + 1));
    _mod = modular;
    _rank = 0;
  }

  void clear(int n, int m) {
    _n = n;
    _m = m;
    for (int i = 0; i <= n; i++) {
      for (int j = 0; j <= m; j++) {
        _mat[i][j] = 0;
      }
    }
  }

  void setRight(int row, int val) { _mat[row][_m] = val; }
  vector<vector<T>>& mat() { return _mat; }
  bool solve() {
    for (int i = 0; i <= _n; i++) {
      for (int j = 0; j <= _m; j++) {
        _mat[i][j] = Mod(_mat[i][j], _mod);
      }
    }

    int now = 0;
    for (int i = 0; i < _m; i++) {
      int maxRow = now;
      for (int j = now; j < _n; j++) {
        if (_mat[j][i] != 0) {
          maxRow = j;
          break;
        }
      }

      if (_mat[maxRow][i] == 0) {
        continue;
      }
      swapRow(now, maxRow);
      divideRow(now, _mat[now][i]);
      for (int j = now + 1; j < _n; j++) {
        if (_mat[j][i] == 0) {
          continue;
        }
        int f = _mat[j][i];
        subtractRow(j, now, f);
      }

      now++;
    }

    _rank = now;
    for (int i = now; i < _n; i++) {
      if (_mat[i][_m] != 0) {
        return false;
      }
    }

    for (int i = now - 1; i >= 0; i--) {
      int x = -1;
      for (int j = 0; j < _m; j++) {
        if (_mat[i][j] != 0) {
          x = j;
          break;
        }
      }
      _mat[_n][x] = Modmul(_mat[i][_m], Inverse(_mat[i][x], _mod), _mod);
      for (int j = i - 1; j >= 0; j--) {
        if (_mat[j][x] == 0) {
          continue;
        }
        _mat[j][_m] =
            Mod(_mat[j][_m] - Modmul(_mat[j][x], _mat[_n][x], _mod), _mod);
        _mat[j][x] = 0;
      }
    }
    return true;
  }

  void swapRow(int i, int j) { _mat[i].swap(_mat[j]); }

  void subtractRow(int i, int j, T f) {
    for (int k = 0; k <= _m; k++) {
      _mat[i][k] = Mod(_mat[i][k] - Modmul(_mat[j][k], f, _mod), _mod);
    }
  }

  void divideRow(int i, T f) {
    int divisor = Inverse(f, _mod);
    for (int k = 0; k <= _m; k++) {
      _mat[i][k] = Modmul(_mat[i][k], divisor, _mod);
    }
  }

  vector<T>& solution() { return _mat[_n]; }
};

template<class V>
ostream& operator<<(ostream& os, const ModGussianElimination<V>& e) {
  for (int i = 0; i <= e._n; i++) {
    for (int j = 0; j <= e._m; j++) {
      os << e._mat[i][j] << ' ';
    }
    os << endl;
  }
  return os;
}

}  // namespace guass_elimination
#endif