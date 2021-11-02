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
        r.readPrimitive(ID);
        r.readPrimitive(lengthOfBlock);
        r.readPrimitive(year);
        r.readPrimitive(month);
        r.readPrimitive(day);
        r.readPrimitive(hour);
        r.readPrimitive(minute);
        r.readPrimitive(creatorNameLen);
        char *tmp_creatorName = new char[creatorNameLen + 1];
        r.readArray(tmp_creatorName, creatorNameLen);
        tmp_creatorName[creatorNameLen] = '\0';
        creatorName.assign(tmp_creatorName);
        delete[] tmp_creatorName;
    }
};

