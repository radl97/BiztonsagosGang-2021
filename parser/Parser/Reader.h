#pragma once
#include <cstdio>
#include <string>

class ParsingException : public std::exception {

};

/// C++ wrapper for reading files
class Reader {
    FILE* f;
public:
    /// takes ownership of FILE* resource(!)
    Reader(FILE* f) : f(f) {}
    ~Reader() {
        fclose(f);
    }

    std::string readStringOfLength(uint64_t length) {
        static char* buffer = new char[100];
        static int bufferSize = 100;

        if (length+1 > bufferSize) {
            bufferSize = length;
            delete[] buffer;
            buffer = new char[bufferSize];
        }

        // No need for null-termination
        // Based on https://stackoverflow.com/a/8438709
        readArray(buffer, length);
        std::string result;
        result.assign(buffer, buffer+length);
        return result;
    }

    std::string readUntilChar(char end) {
        bool found = false;
        std::string result;
        do
        {
            char tmp;
            fread(&tmp, 1, 1, f);
            if (tmp != end) {
                result += tmp;
            }
            else {
                found = true;
            }
        } while (!found);
        return result;
    }

    template<typename T>
    void readPrimitive(T& data) {
        fread(&data, sizeof(T), 1, f);
    }

    // The array needs to be allocated beforehand!
    template<typename T>
    void readArray(T* array, int len) {
        fread(array, sizeof(T), len, f);
    }
};