#include "CAFF.h"

#define PY_SSIZE_T_CLEAN
#include <Python.h>

// Based on: https://docs.python.org/3/extending/extending.html
static PyObject* PreviewCAFF(PyObject* self, PyObject* args) {
    const char *path = "";
    if (PyArg_ParseTuple(args, "s", &path) == 0) {
        return nullptr;
    }
    FILE* f = fopen(path, "rb");
    if (f == nullptr) {
        PyErr_SetString(PyExc_FileNotFoundError, "Failed to open file for reading");
        //std::cerr << "Failed to open file [" << path << "] for reading" << std::endl;
        return nullptr;
    }
    CAFF caff;
    Reader r = Reader(f);
    try {
        caff.read(r);
    } catch (ParsingException&) {
        PyErr_SetString(PyExc_IOError, "Failed to parse CAFF"); // TODO exact message can also be passed
        //std::cerr << "Failed to parse CAFF!" << std::endl;
        return nullptr;
    }
    PyObject* result = PyBytes_FromStringAndSize((char*)&(caff.animations[0].image.pixels[0]), caff.animations[0].image.content_size);
    PyObject* holder = PyTuple_New(3);
    if (PyTuple_SET_ITEM(holder, 0, result) == 0) {
        return nullptr;
    }
    if (PyTuple_SET_ITEM(holder, 1, PyLong_FromLong(caff.animations[0].image.width)) == 0) {
        return nullptr;
    }
    if (PyTuple_SET_ITEM(holder, 2, PyLong_FromLong(caff.animations[0].image.height)) == 0) {
        return nullptr;
    }
    return holder;
}

static PyMethodDef CAFF_StaticMethods[] = {
    { "preview", PreviewCAFF, METH_VARARGS, "Create a preview for a given CAFF file" },
    { NULL, NULL, 0, NULL }
};

static struct PyModuleDef caffmodule = {
    PyModuleDef_HEAD_INIT,
    "caff",   /* name of module */
    NULL, /* module documentation, may be NULL */
    -1,       /* size of per-interpreter state of the module,
                 or -1 if the module keeps state in global variables. */
    CAFF_StaticMethods
};

extern "C" {

PyObject *PyInit_caff(void) {
    PyObject *m;

    m = PyModule_Create(&caffmodule);
    if (m == NULL)
        return NULL;

    return m;
}

}
