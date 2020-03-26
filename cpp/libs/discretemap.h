#ifndef DISCRETEMAP_H
#define DISCRETEMAP_H

#include "common.h"

namespace discretemap {
template <class T, class C = function<bool(const T &, const T &)>>
class DiscreteMap {
 private:
  vector<T> _vec;
  C _comp;

 public:
  DiscreteMap(const vector<T> &vec, C comp = std::less<T>())
      : _vec(vec), _comp(comp) {
    sort(_vec.begin(), _vec.end(), comp);
    _vec.erase(unique(_vec.begin(), _vec.end()), _vec.end());
  }
  int apply(const T& x) {
    return std::lower_bound(_vec.begin(), _vec.end(), x, _comp) - _vec.begin();
  }
  const T& inverse(int x) { return _vec[x]; }
  int min() { return 0; }
  int max() { return _vec.size() - 1; }
};
}  // namespace discretemap

#endif