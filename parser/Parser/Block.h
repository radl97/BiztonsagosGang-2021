#pragma once
#include <cstdint>
#include "Reader.h"

class Block
{
public:
	char ID;
	uint64_t lengthOfBlock;
	void read(Reader& r) {
		r.readPrimitive(ID);
		r.readPrimitive(lengthOfBlock);
	}
};

