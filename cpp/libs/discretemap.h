#ifndef DISCRETEMAP_H
#define DISCRETEMAP_H

#include "common.h"

namespace discretemap {
template <class T>
class DiscreteMap {
 private:
  vector<T> &_vec;

 public:
  DiscreteMap(vector<T> &vec) : _vec(vec) {
    sort(_vec.begin(), _vec.end());
    _vec.erase(unique(_vec.begin(), _vec.end()), _vec.end());
  }
  int apply(T x) {
    return std::lower_bound(_vec.begin(), _vec.end(), x) - _vec.begin();
  }
  int inverse(int x) { return _vec[x]; }
  int min() { return 0; }
  int max() { return _vec.size() - 1; }
};
}  // namespace discretemap

#endif