#include <cstdio>

/// C++ wrapper for reading files
class Reader {
    FILE* f;
public:
    /// takes ownership of FILE* resource(!)
    Reader(FILE* f) : f(f) {}
    ~Reader() {
        fclose(f);
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