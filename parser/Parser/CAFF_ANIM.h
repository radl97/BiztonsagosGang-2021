#pragma once
#include "Block.h"
#include "CIFF.h"

class CAFF_ANIM :
    public Block
{
public:

    uint64_t duration;
    CIFF image;

    void read(Reader& r) {
        Block::readBlockHeader(r, 0x3);
        uint64_t characters_read_counter = r.getBytesRead();

        r.readPrimitive(duration);
        std::cout << "\nanimstart";

        image.read(r);

        uint64_t characters_read_counter_after = r.getBytesRead();

        if (lengthOfBlock != characters_read_counter_after - characters_read_counter) {
            throw ParsingException();
        }

    }
};

