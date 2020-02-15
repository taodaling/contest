#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out)
{
    string s;
    in >> s;
    int l = -1;
    int r = -1;
    for(int i = 0; i < s.size(); i++){
        if(s[i] == '1'){
            if(l == -1){
                l = i;
            }
            r = i;
        }
    }

    if(l == -1){
        out << 0 << endl;
        return;
    }
    int cnt = 0;
    for(int i = l; i <= r; i++){
        if(s[i] == '0'){
            cnt++;
        }
    }
    out << cnt << endl;
}

#include "../../libs/run_multi.h"