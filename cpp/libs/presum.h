#ifndef PRESUM_H
#define PRESUM_H
#include "common.h"
namespace pre_sum {
template <class T>
class PreSum {
 public:
  PreSum() {}
  PreSum(const vector<T> &vals) { populate(vals); }
  void populate(const vector<T> &vals) {
    _data = vals;
    _n = _data.size();
    for (int i = 1; i < _n; i++) {
      _data[i] += _data[i - 1];
    }
  }
  T prefix(int r) {
    if (r < 0) {
      return 0;
    }
    r = min(r, _n - 1);
    return _data[r];
  }
  T suffix(int l) { return _data[_n - 1] - prefix(l - 1); }
  T interval(int l, int r) { return prefix(r) - prefix(l - 1); }

 private:
  vector<T> _data;
  int _n;
};
}  // namespace pre_sum

#endif