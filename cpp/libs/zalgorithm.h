#ifndef ZALGORITHM_H
#define ZALGORITHM_H

#include "common.h"

namespace zalgorithm {

template <class T>
void Z(const function<T(int)> &func, vector<int> &z) {
  int n = z.size();
  if (n == 0) {
    return;
  }

  int l = 0;
  int r = -1;
  z[0] = n;
  for (int i = 1; i < n; i++) {
    if (r < i) {
      l = r = i;
    } else {
      int t = i - l;
      int k = r - i + 1;
      if (z[t] < k) {
        z[i] = z[t];
        continue;
      }
      l = i;
      r++;
    }
    while (r < n && func(r - l) == func(r)) {
      r++;
    }
    r--;
    z[i] = r - l + 1;
  }
}

}  // namespace zalgorithm

#endif