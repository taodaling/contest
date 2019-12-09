
#define MOD(a, b) a %= b; if(a < 0) a += b;


class IntModular
{
public:
	typedef long long ll;
	IntModular(int m): _m(m){}
	int operator()(int x) const {		MOD(x, _m);		return x;	}
	int operator()(ll x) const {		MOD(x, _m);		return x;} 
	int plus(int a, int b)const{		return (*this)(a + b);	}
	int plus(ll a, ll b)const{		return (*this)(a + b);	}
	int subtract(int a, int b)const{		return (*this)(a - b);	}
	int subtract(ll a, ll b)const{		return (*this)(a - b);	}
	int mul(int a, int b)const{		return (*this)((ll)a * b);	}
	int mul(ll a, ll b)const{		return mul((*this)(a), (*this)(b));	}
private:
	int _m;   
};


class LongModular
{
public:
	typedef long long ll;
	typedef long double ld;
	ll operator()(ll x)    {    	MOD(x, _m);    	return x;    }
    ll plus(ll a, ll b)    {    	return (*this)(a + b);    }
    ll subtract(ll a, ll b)    {    	return (*this)(a - b);    }
    ll mul(ll a, ll b)    {    	ll k = ll((ld) a * b / _m);    	ll ans = (a * b - k * _m);    	MOD(ans, _m);    	return ans;    }
private:
	ll _m;		
};

template<int M>
class Remainder
{
public:
	typedef long long ll;
	Remainder(): _v(0){}
	Remainder(int v): _v(v){MOD(_v, M)}
	Remainder(ll v)	{MOD(v, M);	_v = v;	}
	Remainder(const Remainder<M>& x): _v(x._v){}
	Remainder<M>& operator=(Remainder<M> &x){_v = x._v; return *this;}
	Remainder<M>& operator=(const Remainder<M> &x){_v = x._v; return *this;}
	Remainder<M> operator+(const Remainder<M> &x) const{return _v + x._v;}
	Remainder<M> operator-(const Remainder<M> &x) const{return _v - x._v;}
	Remainder<M> operator*(const Remainder<M> &x) const{return (ll)_v * x._v;}
	Remainder<M> &operator+=(Remainder<M> &x){_v += x._v; MOD(_v, M); return *this;}
	Remainder<M> &operator-=(Remainder<M> &x){_v -= x._v; MOD(_v, M); return *this;}
	Remainder<M> &operator+=(const Remainder<M> &x){_v += x._v; MOD(_v, M); return *this;}
	Remainder<M> &operator-=(const Remainder<M> &x){_v -= x._v; MOD(_v, M); return *this;}
	Remainder<M> &operator*=(Remainder<M> &x){ll tmp = (ll)_v * x._v; MOD(tmp, M); _v = tmp; return *this;}
	Remainder<M> &operator*=(const Remainder<M> &x){ll tmp = (ll)_v * x._v; MOD(tmp, M); _v = tmp; return *this;}
	Remainder<M> operator^(int n) const {return pow(_v, n);}
	Remainder<M> operator^(ll n) const {return pow(_v, n);}
	Remainder<M> inverseByFermat() const {return pow(_v, M - 2);}
	Remainder<M> inverseByExtgcd() const {return extgcd(M, _v).second;}
	Remainder<M> operator/(const Remainder<M> &x)const{return (*this) * x.inverseByExtgcd();}
	Remainder<M> &operator/=(Remainder<M> &x){ll tmp = (ll)_v * extgcd(M, _v).second; MOD(tmp, M); _v = tmp; return *this;}
	Remainder<M> &operator/=(const Remainder<M> &x){ll tmp = (ll)_v * extgcd(M, _v).second; MOD(tmp, M); _v = tmp; return *this;}
	int value() const {return _v;}
	bool operator==(const Remainder<M> &x) const {return _v == x._v;}
	bool operator!=(const Remainder<M> &x) const {return _v != x._v;}                                        
private:
	int _v;

	template<typename L>
	static int pow(int x, L n)
	{
		if(n == 0)
		{
			return 1 % M;
		}
		ll ans = pow(x, n >> 1);
		ans = ans * ans % M;
		if(n & 1)
		{
			ans = ans * x % M;
		}
		return ans;
	}
	static pair<ll, ll> extgcd(ll a, ll b)
	{
		if(a >= b){return extgcd0(a, b);}
		pair<ll, ll> xy = extgcd0(b, a);
		swap(xy.first, xy.second);
		return xy;				
	}
	static pair<ll, ll> extgcd0(ll a, ll b)
	{
		if(b == 0)
		{
			return {1, 0};
		}
		pair<ll, ll> xy = extgcd0(b, a % b);
		return {xy.second, xy.first - xy.second * (a / b)};
	}
};

template<int M>
inline ostream& operator<<(ostream &os, const Remainder<M> &x)
{
	return os << x.value();
}
template<int M>
inline istream& operator>>(istream &is, Remainder<M> &x)
{
	ll val;
	is >> val;
	x = val;
	return is;
}

#undef MOD
