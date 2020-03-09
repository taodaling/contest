#include "../../libs/common.h"

bitset<(int)1e7 + 1> bs;

struct Query{
  int k;
  int ans;
};

int Plus(int x){
  if(x == 0){
    return x;
  }
  return x % 10 + Plus(x / 10);
}

Query qs[5000];
void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  for(int i = 1; i <= n; i++){
    int x = i + Plus(i);
    if(x > n){
      continue;
    }
    bs[x] = true;
  }
  vector<int> ks(k);
  for(int i = 0; i < k; i++){
    ks[i] = i;
    in >> qs[i].k;
  }
  sort(ks.begin(), ks.end(), [&](int a, int b) {return qs[a].k < qs[b].k;});
  deque<int> dq;
  dq.assign(ks.begin(), ks.end());
  int cnt = 0;
  for(int i = 1; i <= n; i++){
    if(!bs[i]){
      cnt++;
    }
    while(!dq.empty() && qs[dq.front()].k == cnt){
      qs[dq.front()].ans = i;
      dq.pop_front();
    }
  }

  out << cnt << endl;

  for(int i = 0; i < k; i++){
    out << qs[i].ans << ' ';
  }
}

RUN_ONCE