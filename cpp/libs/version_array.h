#ifndef VERSION_ARRAY_H
#define VERSION_ARRAY_H

#include "common.h"

namespace version_array {
template <class T, int N>
class VersionArray {
 private:
  T _data[N];
  int _version[N];
  T _def;
  int _now;
  inline void access(int i) {
    if (_version[i] != _now) {
      _version[i] = _now;
      _data[i] = _def;
    }
  }

 public:
  VersionArray(const T &def = 0) : _def(def), _now(0) { C0(_version); }
  inline void clear() { _now++; }
  inline T &operator[](int i) {
    access(i);
    return _data[i];
  }
};
}  // namespace version_array

#endif