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
        Block::readBlockHeaderWithCheck(r, 0x3);
        readCommon(r);
    }

    void readContent(Reader& r, Block containing) {
        ID = containing.ID;
        lengthOfBlock = containing.lengthOfBlock;
        readCommon(r);
    }

    void readCommon(Reader& r) {
        uint64_t characters_read_counter = r.getBytesRead();

        r.readPrimitive(duration);

        image.read(r);

        uint64_t characters_read_counter_after = r.getBytesRead();

        if (lengthOfBlock != characters_read_counter_after - characters_read_counter) {
            throw ParsingException();
        }
    }
};

