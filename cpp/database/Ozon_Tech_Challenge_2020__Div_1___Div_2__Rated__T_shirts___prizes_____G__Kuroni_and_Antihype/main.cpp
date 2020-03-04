#include "../../libs/common.h"
#include "../../libs/dsu.h"

const int LIMIT = 1 << 18;
dsu::DSU<LIMIT> cc;
int cnts[LIMIT];

void solve(int testId, istream &in, ostream &out) {
    int n;
    in >> n;
    cnts[0]++;
    ll sum = 0;
    for(int i = 0; i < n; i++){
        int x;
        in >> x;
        cnts[x]++;
        sum -= x;
    }
    for(int i = LIMIT - 1; i >= 0; i--){
        for(int j = i; j; )
        {
            
        }
    }
}

RUN_ONCE