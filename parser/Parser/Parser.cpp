// Parser.cpp : This file contains the 'main' function. Program execution begins and ends there.

#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
#include <fstream>
#include "CAFF.h"


int main(int argc, char** argv)
{
    const char* input_file;
    if (argc <= 1) {
        input_file = "../caff_files/2.caff";
    } else {
        input_file = argv[1];
    }
    FILE* f = fopen(input_file, "rb");
    if (f == 0) {
        std::cerr << "Failed to open file [" << input_file << "] for reading" << std::endl;
        return 1;
    }
    CAFF caff;
    Reader r = Reader(f);
    try {
        caff.read(r);
    } catch (ParsingException&) {
        std::cerr << "Failed to parse CAFF!" << std::endl;
        return 1;
    }
   
    //std::cout << caff.blockNum << caff.credits[0].creatorName;
}
