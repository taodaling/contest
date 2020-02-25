#ifndef radix_h
#define radix_h

#include "common.h"
#include "decimal.h"

namespace radix {
template <class T>
class Radix {
 public:
  Radix(int radix) : _radix(radix) {
    assert(radix > 1);
    T limit = numeric_limits<T>::max();
    _digits.push_back(1);
    while (!decimal::IsMultiplicationOverflow(_digits.back(), radix, limit)) {
      _digits.push_back(_digits.back() * radix);
    }
  }

  int operator()(int i) { return _digits[i]; }

  int get(T x, int i) { return x / _digits[i] % _radix; }
  T set(T x, int i, int v) { return x + (v - get(x, i)) * _digits[i]; }

 private:
  vector<T> _digits;
  int _radix;
};
}  // namespace radix

#endif