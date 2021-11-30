#include "pch.h"
#include "../Parser/CAFF.h"

TEST(ParserTest, TestNegSize) {
    const char* input_file = "../caff_files/neg.caff";
    bool failed = false;
    FILE* f = fopen(input_file, "rb");
    if (f == nullptr) {
        std::cerr << "Failed to open file [" << input_file << "] for reading" << std::endl;
        return;
    }
    CAFF caff;
    Reader r(f);
    try {
        caff.read(r);
    }
    catch (ParsingException&) {
        failed = true;
    }
    EXPECT_TRUE(failed);
}

TEST(ParserTest, TestPoc64) {
    const char* input_file = "../caff_files/poc64.caff";
    bool failed = false;
    FILE* f = fopen(input_file, "rb");
    if (f == nullptr) {
        std::cerr << "Failed to open file [" << input_file << "] for reading" << std::endl;
        return;
    }
    CAFF caff;
    Reader r(f);
    try {
        caff.read(r);
    }
    catch (ParsingException&) {
        failed = true;
    }
    EXPECT_TRUE(failed);
}


