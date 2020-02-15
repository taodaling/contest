#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out)
{
    string a, b, c;
    in >> a >> b >> c;
    int n = a.size();
    bool valid = true;
    for(int i = 0; i < n; i++){
        valid = valid && (a[i] == c[i] || b[i] == c[i]);
    }
    out << (valid ? "YES" : "NO") << endl;
}

#include "../../libs/run_multi.h"