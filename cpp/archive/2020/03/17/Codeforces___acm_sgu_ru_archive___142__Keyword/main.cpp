#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

#define MAX_N (1 << 20)

bool EXIST[MAX_N];

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> seq(n);
  for (int i = 0; i < n; i++) {
    char c;
    in >> c;
    seq[i] = c - 'a';
  }

  int length = 0;
  while (true) {
    length++;
    C0(EXIST);
    int mask = 0;
    for (int i = 0; i < length - 1; i++) {
      if (seq[i]) mask = bits::SetBit(mask, i);
    }
    for (int i = length - 1; i < n; i++, mask >>= 1) {
      if (seq[i]) mask = bits::SetBit(mask, length - 1);
      EXIST[mask] = true;
    }

    int cnt = 0;
    for (int i = 0; i < MAX_N; i++) {
      cnt += EXIST[i];
    }
    dbg(cnt);
    if (cnt < (1 << length)) {
      break;
    }
  }

  int mask = 0;
  while (EXIST[mask]) {
    mask++;
  }

  out << length << endl;
  for (int i = 0; i < length; i++) {
    char c = 'a' + bits::BitAt(mask, i);
    out << c;
  }
}

RUN_ONCE