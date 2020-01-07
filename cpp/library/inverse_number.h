#ifndef INVERSE_NUMBER
#define INVERSE_NUMBER

#include "common.h"
namespace dalt
{
class InverseNumber
{
private:
    vector<int> _inv;

public:
    InverseNumber(int limit, int mod)
    {
        _inv.resize(limit + 1);
        _inv[1] = 1;
        int p = mod;
        for (int i = 2; i <= limit; i++)
        {
            int k = p / i;
            int r = p % i;
            _inv[i] = ((ll)-k * _inv[r]) % mod;
            if (_inv[i] < 0)
            {
                _inv[i] += mod;
            }
        }
    }

    int inverse(int x){
        return _inv[x];
    }
};
} 
#endif