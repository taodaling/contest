#include "../../libs/common.h"


void solve(int testId, istream &in, ostream &out)
{
    int n;
    string s;
    in >> n >> s;
    vector<int> ps(n);
    for(int i = 0; i < n - 1; i++)
    {
        ps[i] = s[i] == '<' ? 0 : 1;
    }
    for(int i = 1; i < n; i++){
        ps[i] += ps[i - 1];
    }
    vector<int> ind1(n);
    vector<int> ind2(n);
    for(int i = 0; i < n; i++){
        ind1[i] = ind2[i] = i;
    }
    sort(ind1.begin(), ind1.end(), [&](int a, int b){
        if(a == b){
            return false;
        }
        bool inv = false;
        if(a > b){
            swap(a, b);
            inv = true;
        }
        int sum = ps[b - 1];
        if(a - 1 >= 0){
            sum -= ps[a - 1];
        }
        bool ans = a > b;
        if(sum == 0){
            ans = true;
        }
        if(sum == b - a){
            ans = false;
        }
        if(inv){
            ans = !ans;
        }
        return ans;
    });

    sort(ind2.begin(), ind2.end(), [&](int a, int b){
        if(a == b){
            return false;
        }
        bool inv = false;
        if(a > b){
            swap(a, b);
            inv = true;
        }
        int sum = ps[b - 1];
        if(a - 1 >= 0){
            sum -= ps[a - 1];
        }
        bool ans = a < b;
        if(sum == 0){
            ans = true;
        }
        if(sum == b - a){
            ans = false;
        }
        if(inv){
            ans = !ans;
        }
        return ans;
    });

    vector<int> a1(n);
    vector<int> a2(n);
    for(int i = 0; i < n; i++){
        a1[ind1[i]] = i;
        a2[ind2[i]] = i;
    }
    for(int i = 0; i < n; i++){
        out << a1[i] + 1 << ' ';
    }
    out << endl;
    for(int i = 0; i < n; i++){
        out << a2[i] + 1 << ' ';
    }
    out << endl;
}

#include "../../libs/run_multi.h"