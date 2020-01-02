#ifndef SIEVE_H
#define SIEVE_H

#include "common.h"

namespace dalt
{
class EulerSieve
{
private:
    vector<int> _primes;
    vector<bool> _isComp;

public:
    EulerSieve(int limit)
    {
        _primes.reserve(limit + 1);
        _isComp.resize(limit + 1);
        for (int i = 2; i <= limit; i++)
        {
            if (!_isComp[i])
            {
                _primes.push_back(i);
            }
            for (int p : _primes)
            {
                if (p > limit / i)
                {
                    break;
                }
                int pi = p * i;
                _isComp[pi] = true;
                if (i % p == 0)
                {
                    break;
                }
            }
        }
    }

    vector<int> &primes(){
        return _primes;
    }

    bool isPrime(int x){
        return x >= 2 && !_isComp[x];
    }
};
} // namespace dalt

#endif