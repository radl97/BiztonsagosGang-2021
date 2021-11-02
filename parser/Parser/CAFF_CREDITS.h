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
    char* creatorName;

    ~CAFF_CREDITS() {
        delete[] creatorName;
    }

    void read(Reader& r) {
        r.readPrimitive(ID);
        r.readPrimitive(lengthOfBlock);
        r.readPrimitive(year);
        r.readPrimitive(month);
        r.readPrimitive(day);
        r.readPrimitive(hour);
        r.readPrimitive(minute);
        r.readPrimitive(creatorNameLen);
        creatorName = new char[creatorNameLen + 1];
        r.readArray(creatorName, creatorNameLen);
        creatorName[creatorNameLen] = '\0';
    }
};

