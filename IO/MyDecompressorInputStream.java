package IO;


import java.io.*;
import java.io.IOException;
//import java.util.List;
//import java.util.Map;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class MyDecompressorInputStream extends InputStream {
    private InputStream inFile;

    public MyDecompressorInputStream(InputStream inFile) {
        this.inFile = inFile;//holds the compressed file.maze
    }

    public int read(byte[] bytearray) throws IOException {
        /* store bytes read from inStream in the bytearray that was passed to func  */
        Inflater inflater = new Inflater();
        InflaterInputStream inflaterIS = new InflaterInputStream(inFile, inflater ,bytearray.length);
        return inflaterIS.read(bytearray, 0, bytearray.length);
    }


    /**
     * empty implementation
     * @return -1 by default
     * */
    public int read() {
        return -1;
    }

/*
    private byte[] decompress(byte[] bytearray) throws IOException, DataFormatException {
        ByteArrayOutputStream byteArrOS = new ByteArrayOutputStream(bytearray.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            byteArrOS.write(buffer, 0, count);
        }
        byteArrOS.close();
        byte[] output = byteArrOS.toByteArray();
        LOG.debug("Original: " + bytearray.length);
        LOG.debug("Compressed: " + output.length);
        return output;
    }
*/


}//class
