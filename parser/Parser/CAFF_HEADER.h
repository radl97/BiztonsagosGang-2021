#pragma once
#include "Block.h"
#include <cstdint>

const uint8_t BLOCK_HEADER_ID = 0x1;

class CAFF_HEADER : public Block
{
public:
    std::string magic;
    uint64_t headerSize;
    uint64_t animationNumber;

    void read(Reader& r) {
        Block::readBlockHeaderWithCheck(r, 0x1);
        if (lengthOfBlock != 4 + sizeof(headerSize) + sizeof(animationNumber)) {
            throw ParsingException();
        }
        magic = r.readStringOfLength(4);
		if (magic[0] != 'C' || magic[1] != 'A' || magic[2] != 'F' || magic[3] != 'F') {
			throw ParsingException();
		}
        r.readPrimitive(headerSize);
        if (headerSize != magic.size() + sizeof(headerSize) + sizeof(animationNumber)) {
            throw ParsingException();
        }
        r.readPrimitive(animationNumber);
    }
};

