import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import myfileio.MyFileIO;

@TestMethodOrder(OrderAnnotation.class)
class HF_BinaryIO_OutputTest {

	/** The buffered output stream for writing binary data to */
	private BufferedOutputStream bOutS;
	
	/** The buffered input stream for reading binary data from */
	private BufferedInputStream bInS;
	
	/** The File connected to a binary file for read or write. */
	private File binFile;
	
	/** Pointer to access the protected methods in BinaryIO.java */
	private BinaryIO binUtils;
	
	/** String for holding binary data to convert to/from a byte */
	private String binStr;
	
	/** Pointer to access the file access methods in MyFileIO */
	MyFileIO fio = new MyFileIO();
	
	/** lookup array to more easily generate binary strings from an integer value */
	private String[] bin2nib = {"0000","0001","0010","0011","0100","0101","0110","0111",
            "1000","1001","1010","1011","1100","1101","1110","1111",};
	
	/**
	 * Any initialization that needs to be done before starting any testing
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Any cleanup that needs to be done after the end of all testing
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Any setup/initialization that must be run before each test
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		binUtils = new BinaryIO();
	}

	/**
	 * Any cleanup that must be run at the end of each test.
	 *
	 * @throws Exception the exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Completely tests all 256 binary strings are correctly converted to in
	 * Methodology:
	 *   - for i = 0 to 255:
	 *   - generate the expected binary string from the loop variable i
	 *   - convert the loop variable i using binUtils.convBinToStr
	 *   - compare the expected value vs output from binUtils.
	 */
	@Test
	@Order(1)
	void test_convBinToStr() {
		System.out.println("Test 1: Checking Conversion - Binary to Strings");
				
		for (int i = 0; i < 256; i++ ) {
			int nhi = (i >> 4) & 0x0F;
			int nlo = (i & 0xF);
			String expect = bin2nib[nhi]+bin2nib[nlo];
			System.out.println("Checking Conversion of "+i+" to binary String "+expect+".");
			String convStr = binUtils.convBinToStr((byte) ( i & 0xff));
			assertTrue(expect.equals(convStr));
		}
	}

	/**
	 * Completely tests all 256 binary strings are correctly converted to in
	 * Methodology:
	 *   - for i = 0 to 255:
	 *   - generate the input binary string from the loop variable i
	 *   - convert the input binary string  to an integer using binUtils.convStrToBin
	 *   - compare i to the generated value;
	 */	@Test
	@Order(2)
	void test_convStrToBin() {
		System.out.println("Test 2: Checking Conversion - String to binary");
				
		for (int i = 0; i < 256; i++ ) {
			int nhi = (i >> 4) & 0x0F;
			int nlo = (i & 0xF);
			String input = bin2nib[nhi]+bin2nib[nlo];
			System.out.println("Checking Conversion of binary String "+input+" to "+i+".");
			int convInt = binUtils.convStrToBin(input);
			assertTrue(i==convInt);
		}
	}

	/**
	 * Test that writeBinString does not write anything to the binary file
	 * if the binStr is less than 8 bits long, and that it returns the original 
	 * binary string.
	 * 
	 * Methodology:
	 *    - generate a series of binary strings of length 1 to 7
	 *    - open the buffered binary output stream
	 *    - use the convStrToBin helper method to convert the string;
	 *      validate that the returned string matches the original string
	 *    - close the binary file and validate that it has a length of 0
	 *    - delete the binary file
	 */
	@Test
	@Order(3)
	void test_bstrNotByte() {
		System.out.println("Test 3: Checking that convStrToBin does not write < 8 bits to a file");
		// create new binary output file
		binFile = new File("encode/test_bstrNotByte.bin");
		// create a test string of 7 bits
		String bits = "0101100";
	
		bOutS = fio.openBufferedOutputStream(binFile);
		
		binStr = "";
		// add all bits, one at a time
		for (int i = 0; i < bits.length(); i++) {
			convStrToBin(bits.substring(0,i+1));
			assertTrue(bits.substring(0,i+1).equals(binStr));
		}
		fio.closeStream(bOutS);
		assertTrue(binFile.length() == 0);
		binFile.delete();
		
	}
	

	/**
	 * Test that writeBinString can successfully write individual bytes to 
	 * the file (ie, the binary string is 8 bits for each write)
	 * 
	 * Methodology:
	 *    - open the buffered binary output stream
	 *    - write 4 bytes individually to the file using the convStrToBin
	 *      helper method to convert the string;
	 *      validate that the string returned after each conversion is ""
	 *    - close the binary file and validate that it has a length of 4
	 *    - read the binary file and verify that the contents were written in
	 *      correct order.
	 *    - delete the binary file
	 */
	@Test
	@Order(4)
	void test_bstrWriteOneByte() {
		System.out.println("Test 4: Checking that convStrToBin can write individual bytes to the file");
		byte[] b = new byte[4];
		// test array of 4 bytes
		b[0] = 0x60;
		b[1] = 0x0d;
		b[2] = (byte) 0xac;
		b[3] = (byte) 0xed;
		
		// create new binary output file
		binFile = new File("encode/test_bstrOneByte.bin");
		bOutS = fio.openBufferedOutputStream(binFile);
		
		// check that binStr is ""
		binStr = "";
		for (int bCnt = 0; bCnt < b.length; bCnt++ ) {
			int bin1 = (b[bCnt] >>> 4)&0xf;
			int bin2 = b[bCnt] & 0xf;
			convStrToBin(bin2nib[bin1] +bin2nib[bin2]);
			assertTrue("".equals(binStr));
		}
		fio.closeStream(bOutS);
		assertTrue(binFile.length() == 4);
		
		bInS = fio.openBufferedInputStream(binFile);
		for (int bCnt = 0; bCnt < b.length; bCnt++) {
			assertTrue(b[bCnt] == readByteFromInStream(bInS));
			System.out.println("   Byte "+bCnt+" matched expected value of "+(b[bCnt] & 0x0ff));
		}
		binFile.delete();
	}
	
	/**
	 * Test that writeBinString can successfully write 2 bytes at a time to 
	 * the file (ie, the binary string is 16 bits for each call)
	 * 
	 * Methodology:
	 *    - open the buffered binary output stream
	 *    - write 4 bytes, 2 bytes at at time, to the file using the convStrToBin
	 *      helper method to convert the string;
	 *      validate that the string returned after each conversion is ""
	 *    - close the binary file and validate that it has a length of 4
	 *    - read the binary file and verify that the contents were written in
	 *      correct order.
	 *    - delete the binary file
	 */	
	@Test
	@Order(5)
	void test_bstrWriteTwoBytes() {
		System.out.println("Test 5: Checking that convStrToBin can write multiple bytes to the file");
		byte[] b = new byte[4];
		// test array of 4 bytes
		b[0] = 0x60;
		b[1] = 0x0d;
		b[2] = (byte) 0xac;
		b[3] = (byte) 0xed;
		
		// create new binary output file
		binFile = new File("encode/test_bstrTwoByte.bin");
		bOutS = fio.openBufferedOutputStream(binFile);

		// check that binStr is ""
		binStr = "";
		
		for (int bCnt = 0; bCnt < b.length; bCnt+=2 ) {
			int bin1 = (b[bCnt] >>> 4)&0xf;
			int bin2 = b[bCnt] & 0xf;
			int bin3 = (b[bCnt+1] >>> 4)&0xf;
			int bin4 = b[bCnt+1] & 0xf;
			convStrToBin(bin2nib[bin1] +bin2nib[bin2]+bin2nib[bin3] +bin2nib[bin4]);
			assertTrue("".equals(binStr));
		}
		
		fio.closeStream(bOutS);
		assertTrue(binFile.length() == 4);
		
		bInS = fio.openBufferedInputStream(binFile);
		for (int bCnt = 0; bCnt < b.length; bCnt++) {
			assertTrue(b[bCnt] == readByteFromInStream(bInS));
			System.out.println("   Byte "+bCnt+" matched expected value of "+(b[bCnt] & 0x0ff));
		}
		binFile.delete();
	}

	/**
	 * Test that writeBinString can handle binary strings of variable
	 * length properly:
	 * - binary strings that are < 8 bits do nothing and return the original string
	 * - binary strings that are >= 8 bits and < 16 bits write 1 byte to the
	 *   file, and return any unwritten bits 
	 * - binary strings that are >= 16 bits and < 24 bits write 2 bytes to the
	 *   file, and return any unwritten bits 
	 * 
	 * Methodology:
	 * 	  - generate a string of 32 bits
	 *    - open the buffered binary output stream
	 *    - attempt to write substrings of various lengths to the file;
	 *      verify that the returned string matches expectations 
	 *    - close the binary file and validate that it has a length of 4
	 *    - read the binary file and verify that the contents were written in
	 *      correct order.
	 *    - delete the binary file
	 */		
	@Test
	@Order(6)
	void test_bstrWriteOneByteWithRemainder() {
		System.out.println("Test 6: Checking that convStrToBin can write a single byte but perserve the remainder to the file");
		byte[] b = new byte[4];
		b[0] = 0X58;
		b[1] = 0x59;
		b[2] = 0x5a;
		b[3] = 0x5b;
		
		String bits = generateBitString(b);
		// create new binary output file
		binFile = new File("encode/test_bstrOneByteRemainder.bin");
		bOutS = fio.openBufferedOutputStream(binFile);
		
		// check that binStr is ""
		binStr = "";
		convStrToBin(bits.substring(0,6));  // should not write anything
		System.out.println("BinStr = "+binStr);
		assertTrue(bits.substring(0,6).equals(binStr));    //binStr should match what was sent in!
		convStrToBin(bits.substring(0,11));  // should write one byte
		System.out.println("BinStr = "+binStr);
		assertTrue(bits.substring(8,11).equals(binStr));  
		convStrToBin(bits.substring(8,26));    // should write two bytes
		System.out.println("BinStr = "+binStr);
		assertTrue(bits.substring(24,26).equals(binStr));
		convStrToBin(bits.substring(24,32));    // should write one byte, no remainder
		System.out.println("BinStr = "+binStr);
		assertTrue("".equals(binStr));		
		
		fio.closeStream(bOutS);
		assertTrue(binFile.length() == 4);
		
		bInS = fio.openBufferedInputStream(binFile);
		for (int bCnt = 0; bCnt < b.length; bCnt++) {
			assertTrue(b[bCnt] == readByteFromInStream(bInS));
			System.out.println("   Byte "+bCnt+" matched expected value of "+(b[bCnt] & 0x0ff));
		}
		binFile.delete();
	}
	
	/**
	 * Test that writeBinString can handle binary strings of variable
	 * length properly:
	 * - binary strings that are < 8 bits do nothing and return the original string
	 * - binary strings that are >= 8 bits and < 16 bits write 1 byte to the
	 *   file, and return any unwritten bits 
	 * - binary strings that are >= 16 bits and < 24 bits write 2 bytes to the
	 *   file, and return any unwritten bits 
	 * 
	 * Methodology:
	 *    - generate an array of 1024 random bytes
	 * 	  - generate a string from the above random bytes
	 *    - open the buffered binary output stream
	 *    - while not all 1024 bytes have been written:
	 *      - attempt to write substrings of various lengths to the file;
	 *        verify that the returned string matches expectations
	 *    - close the binary file and validate that it has a length of 1024
	 *    - read the binary file and verify that the contents were written in
	 *      correct order.
	 *    - delete the binary file
	 */			
	@Test
	@Order(7)
	void test_random1024Bytes() {
		System.out.println("Test 7: Checking 1024 randomly generated bytes for randomly generated accesses");
		byte[] b = random1KBytes();
		String bits = generateBitString(b);
		// create new binary output file
		binFile = new File("encode/test_randBytes.bin");
		bOutS = fio.openBufferedOutputStream(binFile);
		
		Random rnd = new Random();
		// check that binStr is ""
		binStr = "";
		
		int base = 0; 
		int rndLen = rnd.nextInt(16)+3;  // append random # of bits between 3-18
		int bitsLen = bits.length();
		int i = 0;
		while ((base+rndLen) < bitsLen) {
			i++;
			System.out.println("   Conversion #"+i+":  "+rndLen+" bits: "+base+" to "+(base+rndLen)+ ".  bit string="+bits.substring(base,(base+rndLen)));
			convStrToBin(bits.substring(base,base+rndLen)); // base must always be a multiple of 8.
			int byteBase = ((base+rndLen)>>3)<<3;
			System.out.println("   byteBase = "+byteBase+"   BaseBinStr = "+binStr);
			if (byteBase != (base+rndLen))  // there should be a remainder
				assertTrue(bits.substring(byteBase,(base+rndLen)).equals(binStr));    //binStr should match what was sent in!
			else   // no remainder - binStr should be ""
				assertTrue("".equals(binStr));
			int fragment = base+rndLen-byteBase;
			base = byteBase;
			rndLen = rnd.nextInt(16)+3+fragment;  // append random # of bits between 3-18 to existing fragment
		}

		if (base != bitsLen) 
			convStrToBin(bits.substring(base));    // should write final byte, no remainder
		System.out.println("BinStr = "+binStr);
		assertTrue("".equals(binStr));		
		
		fio.closeStream(bOutS);
		assertTrue(binFile.length() == 1024);
		
		bInS = fio.openBufferedInputStream(binFile);
		for (int bCnt = 0; bCnt < b.length; bCnt++) {
			assertTrue(b[bCnt] == readByteFromInStream(bInS));
			System.out.println("   Byte "+bCnt+" matched expected value of "+(b[bCnt] & 0x0ff));
		}
		binFile.delete();
	}
	
	/**
	 * Generates an array of 1K random values between 0 and 255.
	 *
	 * @return the byte[]
	 */
	private byte[] random1KBytes( ) {
		byte[] rndBytes = new byte[1024];
		Random random = new Random();
		for (int i = 0; i < rndBytes.length; i++ ) {
			rndBytes[i] = (byte) (random.nextInt(256));
			System.out.println("    Generated byte "+i+" value = "+rndBytes[i]);
		}
		return rndBytes;
	}
	
	/**
	 * Generate binary string from an array of bytes.
	 * bytes[0] will the be first 8 bits of the string, 
	 * and will be written in MSB..LSB. So a byte value of
	 * 0xAC will be converted to "10101100"...
	 *
	 * @param bytes the bytes
	 * @return the string
	 */
	private String generateBitString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int nhi = ((bytes[i]>>>4)&0x0f);
			int nlo = ((bytes[i])&0x0f);
			sb.append(bin2nib[nhi]);
			sb.append(bin2nib[nlo]);
		}
		System.out.println("Generated Binary String: "+sb.toString());
		return sb.toString();

	}
	
	/**
	 * Read a byte from thei input stream
	 *
	 * @param bInS the b in S
	 * @return the byte
	 */
	private byte readByteFromInStream(BufferedInputStream bInS) {
		byte readByte=0;
		try {
			 readByte = (byte) bInS.read();
		} catch (IOException e) {
			System.out.println("Caught Exception while trying to read a byte");
			e.printStackTrace();
		}
		return readByte;
	}
	
	/**
	 * Helper method that calls binUtils.writeBinString() to:
	 * a) write all possible bytes to bOutS
	 * b) set binStr to any left over bits
	 *
	 * @param bits the bits
	 */
	private void convStrToBin(String bits) {
		System.out.println("   ConvStrToBin bits="+bits);
		try {
			binStr = binUtils.writeBinString(bOutS,bits);
		} catch (IOException e) {
			System.out.println("Exception while trying to write bits to file");
	        e.printStackTrace();
		}
		
	}
		
}
