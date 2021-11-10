#pragma once
#include <time.h>
#include "Block.h"

const uint8_t BLOCK_CREDITS_ID = 0x2;

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

    CAFF_CREDITS(Block& block) : Block(block) {
        if(block.ID != BLOCK_CREDITS_ID) {
            throw ParsingException();
        }
    }

    ~CAFF_CREDITS() {
    }

    void read(Reader& r) {
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
        if (day < 1 || day > 31) {
            throw ParsingException();
        }
        r.readPrimitive(hour);
        if (hour < 0 || hour >= 24) {
            throw ParsingException();
        }
        r.readPrimitive(minute);
        if (minute < 0 || minute >= 60) {
            throw ParsingException();
        }
        if (!isDateValid()) {
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

    bool isDateValid() {
        struct tm date;
        date.tm_year = year - 1900;
        date.tm_mon = month - 1;
        date.tm_mday = day;
        struct tm date2;
        date2.tm_year = year - 1900;
        date2.tm_mon = month - 1;
        date2.tm_mday = day;
        //sets fields to valid values, any difference in the two dates indicates that the original was invalid
        mktime(&date2);
        if(date.tm_mday != date2.tm_mday || date.tm_mon != date2.tm_mon || date.tm_year != date2.tm_year) {
            return false;
        }
        return true;
    }
};

