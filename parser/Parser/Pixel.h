#pragma once
#include <cstdint>
#include "Reader.h"

class Pixel
{
public:
	uint8_t R, G, B;

    void read(Reader& r) {
        r.readPrimitive(R);
        r.readPrimitive(G);
        r.readPrimitive(B);
    }
};

