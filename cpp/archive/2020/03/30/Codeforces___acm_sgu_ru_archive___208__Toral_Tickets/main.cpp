#include "../../libs/bigint.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/dsu.h"
#include "../../libs/mathext.h"
#include "../../libs/modular.h"

using modular::Mod;
using bi = bigint::BigInt;

dsu::DSU<20 * 20> dset;
int n;
int m;

int Id(int i, int j) { return i * m + j; }

int Mirror(int i, int x) { return x - 1 - i; }

int BuildDSU(int r, int c, bool flipR, bool flipC) {
  dset.reset();
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
      int x = Mod(i + r, n);
      int y = Mod(j + c, m);
      if (flipR) {
        x = Mirror(x, n);
      }
      if (flipC) {
        swap(x, y);
      }
      dset.merge(Id(i, j), Id(x, y));
    }
  }

  int cnt = 0;
  for (int i = 0; i < n * m; i++) {
    cnt += dset.find(i) == i;
  }
  return cnt;
}

void solve(int testId, istream &in, ostream &out) {
  in >> n >> m;
  if (n > m) {
    swap(n, m);
  }

  bi ans;

  if (n < m) {
    bi sum = 0;
    int g = n * m * 2;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        for (int k = 0; k < 2; k++) {
          int cnt = BuildDSU(i, j, k, false);
         // dbg(i, j, k, cnt);
          sum += mathext::Pow(bi(2), cnt);
        }
      }
    }
    ans = sum / g;
  } else {
    bi sum = 0;
    int g = n * m * 4;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        for (int k = 0; k < 2; k++) {
          for (int t = 0; t < 2; t++) {
            int cnt = BuildDSU(i, j, k, t);
            //dbg(i, j, k, cnt);
            sum += mathext::Pow(bi(2), cnt);
          }
        }
      }
    }
    ans = sum / g;
  }
  out << ans;
}

RUN_ONCE