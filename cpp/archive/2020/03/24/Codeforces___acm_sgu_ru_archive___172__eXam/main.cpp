#include "../../libs/common.h"
#include "../../libs/dsu.h"

dsu::XorDeltaDSU<200> dset;

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  for(int i = 0; i < m; i++){
    int a, b;
    in >> a >> b;
    a--;
    b--;
    if(dset.find(a) == dset.find(b) && dset.delta(a, b) != 1){
      out << "no";
      return;
    }
    dset.merge(a, b, 1);
  }

  for(int i = 0; i < n - 1; i++){
    if(dset.find(i) != dset.find(i + 1)){
      dset.merge(i, i + 1, 0);
    }
  }
  
  vector<int> first;
  first.reserve(n);
  for(int i = 0; i < n; i++){
    if(dset.delta(0, i) == 0){
      first.push_back(i);
    }
  }

  out << "yes" << endl;
  out << first.size() << endl;
  for(int x : first){
    out << x + 1 << ' ';
  }
}

RUN_ONCE