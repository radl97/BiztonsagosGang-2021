#pragma once
#include <cstdint>
#include "Reader.h"

class Block
{
public:
	uint8_t ID;
	uint64_t lengthOfBlock;
	void readBlockHeaderWithCheck(Reader& r, uint8_t expected_ID) {
		r.readPrimitive(ID);
		if (ID != expected_ID) {
			throw ParsingException();
		}
		r.readPrimitive(lengthOfBlock);
	}

	void readBlockHeader(Reader& r) {
		r.readPrimitive(ID);
		r.readPrimitive(lengthOfBlock);
	}
};

