#include "../../libs/common.h"
#include "../../libs/prime_sieve.h"

using prime_sieve::EratosthenesSieve;

int A, B, C, D;

int F(int x)
{
    return D + C * x + B * x * x + A * x * x * x;
}

int totalFactor(int x, int n)
{
    if (n == 0)
    {
        return 0;
    }
    return n / x + totalFactor(x, n / x);
}


struct handler{
    int sum;
    int n;
    void operator()(int p){
        sum += totalFactor(p, n) * F(p);
    }
};

void solve(int testId, istream &in, ostream &out)
{
    int n;
    in >> n >> A >> B >> C >> D;

    handler h;
    h.sum = 0;
    h.n = n;

    EratosthenesSieve(h, n);

    out << ui(h.sum);
}

#include "../../libs/run_once.h"