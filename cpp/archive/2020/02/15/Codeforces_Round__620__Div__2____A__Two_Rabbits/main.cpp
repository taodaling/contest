#include "../../libs/common.h"


void solve(int testId, istream &in, ostream &out)
{
    int x, y, a, b;
    in >> x >> y >> a >> b;
    if((y - x) % (a + b) == 0){
        out << (y - x) / (a + b);
    }else{
        out << -1;
    }
    out << endl;
}

#include "../../libs/run_multi.h"