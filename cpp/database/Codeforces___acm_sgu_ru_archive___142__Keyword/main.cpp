#include "../../libs/common.h"
#include "../../libs/hash.h"
#include "../../libs/binary_search.h"
#include "../../libs/decimal.h"

using namespace hash;
using namespace binary_search;
using decimal::Merge;

#define MAX_N 500010
HashData<MAX_N, 31> hd31;
HashData<MAX_N, 61> hd61;
PartialHash<MAX_N, 31> ph31(hd31);
PartialHash<MAX_N, 61> ph61(hd61);
ModifiableHash<30, 31> mh31(hd31);
ModifiableHash<30, 61> mh61(hd61);

void dfs()

void solve(int testId, istream &in, ostream &out) {
  int n;
  vector<int> seq(n);
  for (int i = 0; i < n; i++) {
    char c;
    in >> c;
    seq[i] = c - 'a';
  }

  const function<int(int)> &func = [&](int i) { return seq[i]; };
  ph31.reset(func, 0, n - 1);
  ph61.reset(func, 0, n - 1);

  int length = BinarySearch<int>(1, 20, [&](int mid) {
    unordered_map<ll, bool, CustomHash> umap;
    for (int i = 0; i < n; i++) {
      int l = i - mid + 1;
      int r = i;
      if (l < 0) {
        continue;
      }
      ll h = Merge(ph31.hash(l, r, true), ph61.hash(l, r, true));
      umap[h] = true;
    }
    return umap.size() < (1 << mid);
  });

  unordered_map<ll, bool, CustomHash> umap;
  for (int i = 0; i < n; i++) {
    int l = i - length + 1;
    int r = i;
    if (l < 0) {
      continue;
    }
    ll h = Merge(ph31.hash(l, r, true), ph61.hash(l, r, true));
    umap[h] = true;
  }


}

RUN_ONCE