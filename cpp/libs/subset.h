#ifndef SUBSET_H
#define SUBSET_H

#include "common.h"

namespace subset {
class Subset {
 private:
  ui mask;
  ui cur;

 public:
  Subset(ui m) {
    mask = m;
    cur = m + 1;
  }

  bool more() { return cur; }

  ui next() { return cur = (cur - 1) & mask; }
};
}  // namespace subset

#endif