package io;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Test {
// --------------------------- main() method ---------------------------

    /**
     * demonstratrate how to compute an MD5 message digest requires Java 1.4+
     *
     * @param args not used
     *
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static void main( String[] args ) throws UnsupportedEncodingException, NoSuchAlgorithmException
        {
        byte[] theTextToDigestAsBytes =
                "The quick brown fox jumped over the lazy dog's back"
                        .getBytes( "8859_1" /* encoding */ );
        MessageDigest md = MessageDigest.getInstance( "MD5" );
        md.update( theTextToDigestAsBytes );
        byte[] digest = md.digest();

        // will print MD5
        System.out.println( "Algorithm used:" + md.getAlgorithm() );

        // should be 16 bytes, 128 bits long
        System.out.println( digest.length );

        // dump out the hash
        for (int i = 0; i < digest.length; i++)
            {
        	byte b = digest[i];
            System.out.print( Integer.toHexString( b & 0xff ) + " " );
            }
        }
}