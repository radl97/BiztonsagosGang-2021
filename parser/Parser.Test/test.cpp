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
    ASSERT_TRUE(failed);
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
    ASSERT_TRUE(failed);
}

TEST(ParserTest, TestCaff1) {
    const char* input_file = "../caff_files/1.caff";
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
    ASSERT_FALSE(failed);
    ASSERT_EQ(caff.blockNum, 3);
    ASSERT_EQ(caff.headers[0].animationNumber, 2);
    ASSERT_EQ(caff.headers[0].magic, "CAFF");

    ASSERT_EQ(caff.animations[0].image.magic, "CIFF");
    ASSERT_EQ(caff.animations[0].image.caption, "Beautiful scenery");
    ASSERT_EQ(caff.animations[0].image.tags.size(), 3);
    ASSERT_EQ(caff.animations[0].image.tags[2], "mountains");

    ASSERT_EQ(caff.credits[0].creatorName, "Test Creator");
    ASSERT_EQ(caff.credits[0].year,2020);
    ASSERT_EQ(caff.credits[0].month, 7);
    ASSERT_EQ(caff.credits[0].day, 2);



}

