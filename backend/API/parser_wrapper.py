from PIL import Image
import struct
import subprocess
import tempfile

def save_preview(caff_place, output_filename):
    raw_output_filename = tempfile.mktemp()
    subprocess.run(['Parser.exe', caff_place, raw_output_filename])

    with open(raw_output_filename, 'rb') as f:
        data=f.read()
    width, height = struct.unpack('QQ',data[:16])
    Image.frombuffer("RGB", (width, height), data[16:], "raw", "RGB", 0, 1).save(output_filename)


