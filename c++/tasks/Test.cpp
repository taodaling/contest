#include "../library/common.h"
#include "../library/modular.h"

class Test {
public:
	void solve(std::istream& in, std::ostream& out) {
        Remainder<(int)1e9 + 7> x;
        in >> x;
        out << x;
	}
};
