package IO;

import java.io.*;
import java.util.zip.Deflater;


/* java.io Provides for system input and output through data streams, serialization and the file system.
    Unless otherwise noted, passing a null argument to a constructor or method in any class or interface in this package
     will cause a NullPointerException to be thrown.
*/
//can we compress by taking the byte array -> store 8 bits in 1 byte through bit manip. -> compress the the compressed bytes?
/* String --> byte[] String.getBytes() --> OutputStream.write(byte[])
 *  we can override or append the date to file - the constructor decides which mode */

/**
 * Compresses & Writes a byte[] to an OutputObject (File)
 * */
public class MyCompressorOutputStream  extends OutputStream{
    private OutputStream outStream;
    //private static final Logger LOG = Logger.getLogger(MyCompressorOutputStream.class);
    //private FileChannel channel;
    //private BufferedOutputStream bufferOS;
    //private StringBuilder sbCompressedData;

    public MyCompressorOutputStream(OutputStream fileOS) {
             this.outStream = fileOS;
    }//ctor

    public void write(byte[] bytearray) throws IOException {
        if(bytearray == null){ return; }
        compress(bytearray);
    }


    private byte[] compress(byte[] bytearray) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(bytearray);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream(bytearray.length);
        deflater.finish();

        byte[] buffer = new byte[1024];// 1 kiB buffer
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            byteArrayOS.write(buffer, 0, count);
        }
        /* write the compressed bytes to file/Out Stream */
        byteArrayOS.writeTo(outStream);
        byteArrayOS.close();

        /* show compression rate */
        byte[] output = byteArrayOS.toByteArray();
        System.out.println("Original: "  + ((double)bytearray.length / 1024) +  "Kb");
        System.out.println("Compressed: "  + ((double)output.length / 1024) +  "Kb");
        if((bytearray.length / 1024)  != 0) {
            System.out.println("Compression Rate is: " + ((double)output.length / 1024) / ((double)bytearray.length / 1024) + "Kb");
        }
        //LOG.debug("Original: " + bytearray.length / 1024 + " Kb");
        // LOG.debug("Compressed: " + output.length / 1024 + " Kb");
        return output;
    }

    public void write(int b) throws IOException {  }



}//class MyCompressorOutputStream
