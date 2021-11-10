from PIL import Image
import struct

with open('output.raw', 'rb') as f:
    data=f.read()
width, height = struct.unpack('QQ',data[:16])
Image.frombuffer("RGB", (width, height), data[16:], "raw", "RGB", 0, 1).show()
