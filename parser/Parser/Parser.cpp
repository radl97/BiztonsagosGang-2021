// Parser.cpp : This file contains the 'main' function. Program execution begins and ends there.

#define _CRT_SECURE_NO_WARNINGS

#include <iostream>
#include <fstream>
#include "CAFF.h"

int main(int argc, char** argv)
{
    const char* input_file;
    const char* output_file = "output.raw";
    if (argc <= 1) {
        input_file = "../caff_files/1.caff";
    } else {
        input_file = argv[1];
        if (argc >= 3) {
            output_file = argv[2];
        }
    }
    FILE* f = fopen(input_file, "rb");
    if (f == nullptr) {
        std::cerr << "Failed to open file [" << input_file << "] for reading" << std::endl;
        return 1;
    }
    CAFF caff;
    Reader r(f);
    try {
        caff.read(r);
    } catch (ParsingException&) {
        std::cerr << "Failed to parse CAFF!" << std::endl;
        return 1;
    }

    CIFF& ciff = caff.animations[0].image;

    std::ofstream basic_output(output_file, std::ios::binary);
    basic_output.write((char*)&ciff.width, sizeof(ciff.width));
    basic_output.write((char*)&ciff.height, sizeof(ciff.height));
    basic_output.write((char*)&ciff.pixels[0], ciff.content_size);
}
