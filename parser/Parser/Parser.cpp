// Parser.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <fstream>
#include "CAFF_HEADER.h"
#include "CAFF_CREDITS.h"

int main()
{
    FILE* f;

    errno_t err = fopen_s(&f, "C:\\Users\\denes\\Git\\BiztonsagosGang\\parser\\caff_files\\2.CAFF", "r");
    if (f == 0) {
        return 1;
    }

    CAFF_HEADER h;
    Reader r = Reader(f);
    h.read(r);
    std::cout << h.ID <<std::endl<< h.lengthOfBlock << std::endl << h.magic << std::endl << h.headerSize << std::endl << h.animationNumber << std::endl;
    std::cout << "end header\n";

    CAFF_CREDITS cred;
    cred.read(r);
    std::cout << cred.ID << std::endl << cred.lengthOfBlock << std::endl << cred.creatorNameLen << std::endl << cred.year <<std::endl << cred.creatorName;
    std::cout << "\nend credits\n";
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
