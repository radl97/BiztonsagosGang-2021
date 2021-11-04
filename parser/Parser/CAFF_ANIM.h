#pragma once
#include "Block.h"
#include "CIFF.h"

const uint8_t BLOCK_HEADER_ID = 0x1;
const uint8_t BLOCK_ANIM_ID = 0x3;

class CAFF_ANIM :
    public Block
{
public:

    uint64_t duration;
    CIFF image;

    CAFF_ANIM(Block& block) : Block(block) {
        if(block.ID != BLOCK_ANIM_ID) {
            throw ParsingException();
        }
    }

    void read(Reader& r) {
        uint64_t characters_read_counter = r.getBytesRead();

        r.readPrimitive(duration);

        image.read(r);

        uint64_t characters_read_counter_after = r.getBytesRead();

        if (lengthOfBlock != characters_read_counter_after - characters_read_counter) {
            throw ParsingException();
        }
    }
};

