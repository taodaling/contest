#ifndef PRIME_SIEVE_H
#define PRIME_SIEVE_H
#include "common.h"
namespace prime_sieve
{
template <size_t N>
void EulerSieve(vector<int> &primes, bitset<N> &isComp)
{
    primes.clear();
    isComp.reset();
    for (int i = 2; i < N; i++)
    {
        if (!isComp[i])
        {
            primes.push_back(i);
        }
        for (int p : primes)
        {
            if (p > (N - 1) / i)
            {
                break;
            }
            int pi = p * i;
            isComp[pi] = true;
            if (i % p == 0)
            {
                break;
            }
        }
    }
}

template <size_t N>
void EulerSieve(vector<int> &primes, bitset<N> &isComp, vector<int> &minPrimeFactor)
{
    primes.clear();
    isComp.reset();
    minPrimeFactor.clear();
    minPrimeFactor.resize(N);
    for (int i = 2; i < N; i++)
    {
        if (!isComp[i])
        {
            primes.push_back(i);
        }
        for (int p : primes)
        {
            if (p > (N - 1) / i)
            {
                break;
            }
            int pi = p * i;
            isComp[pi] = true;
            minPrimeFactor[pi] = p;
            if (i % p == 0)
            {
                break;
            }
        }
    }
}

/**
 * Find all primes in [2, n] and consume them.
 * The time complexity is O(n log log n) and the space complexity is O(sqrt(n))
 */
template<class C>
void EratosthenesSieve(C &consumer, int n){
    if(n <= 1){
        return;
    }
    int block = std::sqrt(n) + 0.5;
    vector<bool> isComp(block + 1);
    vector<int> primes;
    for(int i = 2; i <= block; i++){
        if(isComp[i]){
            continue;
        }
        primes.push_back(i);
        for(int j = i + i; j <= block; j += i){
            isComp[j] = true;
        }
    }
    for(int p : primes){
        consumer(p);
    }
    for(int l = block + 1; l <= n; l += block){
        int r = min(l + block - 1, n);
        fill(isComp.begin(), isComp.end(), false);
        for(int p : primes){
            if(r < p * p){
                break;
            }
            int top = max(0, l - p * p);
            int bot = p;
            for(int j = (top + bot - 1) / bot * p + p * p; j <= r; j += p){
                isComp[j - l] = true;
            }
        }
        for(int j = l; j <= r; j++){
            if(!isComp[j - l]){
                consumer(j);
            }
        }
    }
}
}; // namespace prime_sieve

#endif