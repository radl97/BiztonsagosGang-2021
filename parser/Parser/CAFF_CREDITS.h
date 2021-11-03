#pragma once
#include "Block.h"
class CAFF_CREDITS :
    public Block
{
public:
    uint16_t year;
    uint8_t month;
    uint8_t day;
    uint8_t hour;
    uint8_t minute;
    uint64_t creatorNameLen;
    std::string creatorName;

    ~CAFF_CREDITS() {
    }

    void read(Reader& r) {
        Block::read(r);
        r.readPrimitive(year);
        // The year in the link: https://www.crysys.hu/downloads/vihima06/2020/CAFF.txt
        // Probably the first CAFF file's *creation date* does not precede this date...
        if (year < 2020) {
            throw ParsingException();
        }
        r.readPrimitive(month);
        if (month < 1 || month > 12) {
            throw ParsingException();
        }
        r.readPrimitive(day);
        // TODO we should cross-check this with some time library
        if (day < 1 || day > 31) {
            throw ParsingException();
        }
        r.readPrimitive(hour);
        if (hour < 0 || hour >= 60) {
            throw ParsingException();
        }
        r.readPrimitive(minute);
        if (minute < 0 || minute >= 60) {
            throw ParsingException();
        }
        r.readPrimitive(creatorNameLen);

        // The earliest point to cross-check lengthOfBlock with the should-be size
        if (lengthOfBlock != sizeof(year) + sizeof(month) + sizeof(day) +
                sizeof(hour) + sizeof(minute) + sizeof(creatorNameLen) + creatorNameLen) {
            throw ParsingException();
        }

        creatorName = r.readStringOfLength(creatorNameLen);
    }
};

