#pragma once
#include "Block.h"
#include <cstdint>

class CAFF_HEADER : public Block
{
public:
    char magic[4];
    uint64_t headerSize;
    uint64_t animationNumber;

    void read(Reader& r) {
        Block::read(r);
        r.readArray(magic, 4);
		if (magic[0] != 'C' || magic[1] != 'A' || magic[2] != 'F' || magic[3] != 'F') {
			throw ParsingException();
		}
        r.readPrimitive(headerSize);
        if (headerSize != sizeof(magic) + sizeof(headerSize) + sizeof(animationNumber)) {
            throw ParsingException();
        }
        r.readPrimitive(animationNumber);
    }
};

