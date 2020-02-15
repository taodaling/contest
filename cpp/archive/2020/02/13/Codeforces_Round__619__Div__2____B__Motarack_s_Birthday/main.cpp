#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out)
{
    int n;
    in >> n;
    vector<int> a(n);
    for(int i = 0; i < n; i++){
        in >> a[i];
    }
    int d = 0;
    int mx = 0;
    int mi = 1e9;
    for(int i = 1; i < n; i++){
        if(a[i] >= 0 && a[i - 1] >= 0){
            d = max(abs(a[i] - a[i - 1]), d);
        }else if(a[i] == -1 && a[i - 1] == -1){
        }else if(a[i] == -1){
            mx = max(a[i - 1], mx);
            mi = min(a[i - 1], mi);
        }else{
            mx = max(a[i], mx);
            mi = min(a[i], mi);
        }
    }
    if(mx < mi){
        mi = mx = 0;
    }
    int k = (mi + mx) / 2;
    int ans = max(max(d, k - mi), mx - k);
    out << ans << ' ' << k << endl;
}

#include "../../libs/run_multi.h"