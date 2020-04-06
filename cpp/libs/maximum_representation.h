#ifndef MAXIMUM_REPRESENTATION_H
#define MAXIMUM_REPRESENTATION_H

#include "common.h"

namespace maximum_representation {
int MaximumRepresentation(const function<int(int)> &func, int n) {
  int i = 0;
  int j = i + 1;
  while (j < n) {
    int k = 0;
    while (k < n && func((i + k) % n) == func((j + k) % n)) {
      k++;
    }
    if (func((i + k) % n) >= func((j + k) % n)) {
      j = j + k + 1;
    } else {
      int next = j;
      j = max(j + 1, i + k + 1);
      i = next;
    }
  }
  return i;
}
}  // namespace maximum_representation

#endif