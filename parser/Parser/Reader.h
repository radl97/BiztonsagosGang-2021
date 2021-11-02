#pragma once
#include <cstdio>
#include <string>

/// C++ wrapper for reading files
class Reader {
    FILE* f;
public:
    /// takes ownership of FILE* resource(!)
    Reader(FILE* f) : f(f) {}
    ~Reader() {
        fclose(f);
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