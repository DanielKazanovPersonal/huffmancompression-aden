import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.aden.hf_lib.HuffCompTestLib;
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
class HF_Encode_Test {

	EncodeDecode enc_dec;
	HuffmanCompressionUtilities huffUtil;
	GenWeights gw;
	HuffCompAlerts hca;
	HuffCompTestLib hflib = new HuffCompTestLib();
	private final int NUM_ASCII = 128;
//    private static boolean noCR = false; 
    private boolean optimize = false;
    int[] weights = new int[NUM_ASCII];
    private static String os;
    private String textFile;
    private String weightsFile;
    private String encodeFile;
    MyFileIO fio = new MyFileIO();
    
	private File binOutf;
	
    /**
     * Gets the operating system.
     *
     * @return the operating system
     */
    private static String getOperatingSystem() {
    	os = System.getProperty("os.name");
    	return os;
    }
    
    /**
     * generates the default path and filenames from a base string
     *
     * @param base the base
     */
    private void updateFileNames(String base) {
    	textFile = "data/"+base+".txt";
    	weightsFile = "weights/"+base+".csv";
    	encodeFile = "encode/"+base+".bin";
    	if (!optimize)
    		encodeFile = encodeFile.replaceAll(".bin","_full.bin");
    }
    
    /**
     * Sets the up before class.
     *
     * @throws Exception the exception
     */
    @BeforeAll
	static void setUpBeforeClass() throws Exception {
    	getOperatingSystem();
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {

	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}
	
	/**
	 * Encode Test Methodology:
	 * - Initial setup: configure strings for file names, regenerate weights, remove encoded file
	 * - encode the file
	 * - compare the first 8 bytes against the expected values based upon operating system
	 *   
	 */
	
	/**
	 * Test encode simple.txt with optimized tree.
	 */
	@Test
	@Order(1)
	void test_encode_simple_optimize() {
		optimize = true;
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		enc_dec = new EncodeDecode(gw,hca);
		huffUtil = new HuffmanCompressionUtilities();
		String base = "simple";
		updateFileNames(base);
		checkWeightsFile();
		huffUtil.setWeights(new int[NUM_ASCII]);
		removeBinaryFile();
		enc_dec.encode(textFile,encodeFile,weightsFile,optimize);
		// recreate file handle to test existence of binary file
		binOutf = new File(encodeFile);
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 17));
			expectData = new int[]{189,163,114,143,129,123,78,202};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 16));
			expectData = new int[] {191,201,178,176,139,127,159,202};
		} 
		BufferedInputStream bis = fio.openBufferedInputStream(binOutf);
		assertTrue(read8Bytes(bis,expectData));
	}
		
	/**
	 * Test encode simple.text, full huffman tree.
	 */
	@Test
	@Order(2)
	void test_encode_simple_full() {
		optimize = false;
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		enc_dec = new EncodeDecode(gw,hca);
		huffUtil = new HuffmanCompressionUtilities();
		String base = "simple";
		updateFileNames(base);
		
		checkWeightsFile();
		huffUtil.setWeights(new int[NUM_ASCII]);
		removeBinaryFile();
		enc_dec.encode(textFile,encodeFile,weightsFile,optimize);
		// recreate file handle to test existence of binary file
		binOutf = new File(encodeFile);
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 17));
			expectData = new int[]{189,163,114,143,129,123,78,202};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 16));
			expectData = new int[] {191,201,178,176,139,127,159,202};
		} 
		BufferedInputStream bis = fio.openBufferedInputStream(binOutf);
		assertTrue(read8Bytes(bis,expectData));
	}
	
	/**
	 * Test encode Green Eggs and Ham.txt - optimized Huffman tree.
	 */
	@Test
	@Order(3)
	void test_encode_GEAH_optimize() {
		optimize = true;
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		enc_dec = new EncodeDecode(gw,hca);
		huffUtil = new HuffmanCompressionUtilities();
		String base = "Green Eggs and Ham";
		updateFileNames(base);
		
		checkWeightsFile();
		huffUtil.setWeights(new int[NUM_ASCII]);
		removeBinaryFile();
		enc_dec.encode(textFile,encodeFile,weightsFile,optimize);
		// recreate file handle to test existence of binary file
		binOutf = new File(encodeFile);
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 2047));
			expectData = new int[]{87,104,240,173,20,48,173,30};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 1920));
			expectData = new int[] {96,243,61,94,107,234,243,24};
		} 
		BufferedInputStream bis = fio.openBufferedInputStream(binOutf);
		assertTrue(read8Bytes(bis,expectData));
		
	}
	
	/**
	 * Test encode The Cat in the Hat.txt - optimized Huffman Tree.
	 */
	@Test
	@Order(4)
	void test_encode_TCITH_optimize() {
		optimize = true;
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		enc_dec = new EncodeDecode(gw,hca);
		huffUtil = new HuffmanCompressionUtilities();
		String base = "The Cat in the Hat";
		updateFileNames(base);
		
		checkWeightsFile();
		huffUtil.setWeights(new int[NUM_ASCII]);
		removeBinaryFile();
		enc_dec.encode(textFile,encodeFile,weightsFile,optimize);
		// recreate file handle to test existence of binary file
		binOutf = new File(encodeFile);
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 4297));
			expectData = new int[]{201,127,173,203,196,227,202,179};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 4047));
			expectData = new int[] {218,142,3,63,52,159,59,222};
		} 
		BufferedInputStream bis = fio.openBufferedInputStream(binOutf);
		assertTrue(read8Bytes(bis,expectData));		
	}
	
	/**
	 * Test encode Harry Potter and the Sorcerer.txt - optimized Huffman Tree.
	 */
	@Test
	@Order(5)
	void test_encode_HPATS_optimize() {
		optimize = true;
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		enc_dec = new EncodeDecode(gw,hca);
		huffUtil = new HuffmanCompressionUtilities();
		String base = "Harry Potter and the Sorcerer";
		updateFileNames(base);
		
		checkWeightsFile();
		huffUtil.setWeights(new int[NUM_ASCII]);
		removeBinaryFile();
		enc_dec.encode(textFile,encodeFile,weightsFile,optimize);
		// recreate file handle to test existence of binary file
		binOutf = new File(encodeFile);
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 259670));
			expectData = new int[]{97,6,119,242,131,204,131,240};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 253916));
			expectData = new int[] {101,6,120,117,3,204,131,240};
		} 
		BufferedInputStream bis = fio.openBufferedInputStream(binOutf);
		assertTrue(read8Bytes(bis,expectData));

	}
	
	/**
	 * Test encode War And Peace.txt - optimized Huffman Tree.
	 */
	@Test
	@Order(6)
	void test_encode_WAP_optimize() {
		optimize = true;
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		enc_dec = new EncodeDecode(gw,hca);
		huffUtil = new HuffmanCompressionUtilities();
		String base = "warAndPeace";
		updateFileNames(base);
		
		checkWeightsFile();
		huffUtil.setWeights(new int[NUM_ASCII]);
		removeBinaryFile();
		enc_dec.encode(textFile,encodeFile,weightsFile,optimize);
		// recreate file handle to test existence of binary file
		binOutf = new File(encodeFile);
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 1875089));
			expectData = new int[]{154,152,204,255,59,34,47,172};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 1816470));
			expectData = new int[] {178,152,203,255,186,226,57,172};
		} 
		BufferedInputStream bis = fio.openBufferedInputStream(binOutf);
		assertTrue(read8Bytes(bis,expectData));
		
	}
	
	/**
	 * Test encode War and Peace - full Huffman Tree.
	 */
	@Test
	@Order(7)
	void test_encode_WAP_full() {
		optimize = false;
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		enc_dec = new EncodeDecode(gw,hca);
		huffUtil = new HuffmanCompressionUtilities();
		String base = "warAndPeace";
		updateFileNames(base);
		
		checkWeightsFile();
		huffUtil.setWeights(new int[NUM_ASCII]);
		removeBinaryFile();
		enc_dec.encode(textFile,encodeFile,weightsFile,optimize);
		// recreate file handle to test existence of binary file
		binOutf = new File(encodeFile);
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 1875089));
			expectData = new int[]{154,152,204,255,59,34,47,172};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 1816470));
			expectData = new int[] {178,152,203,255,186,226,57,172};
		} 
		BufferedInputStream bis = fio.openBufferedInputStream(binOutf);
		assertTrue(read8Bytes(bis,expectData));
	}
	
	/**
	 * Tests that writeEOF is correctly appended and padded
	 *  
	 * Methodology:
	 * - generate the encode for simpler.txt
	 * - based upon those encodings, create strings that will encode as 2-12 bits (testStrings)
	 * - for each string:
	 * 		- write the string to a temporary file
	 * 		- encode the file (this will append the EOF and padding)
	 * 		- check that the file has the correct length
	 *      - use HuffCompTestLib to check that the contents match expectations
	 *      - cleanup the files.
	 */	
	@Test
	@Order(8)
	void test_writeEOFPadding() {
		System.out.println("Test 8: Checking Write EOF and padding");
		optimize = false;
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		enc_dec = new EncodeDecode(gw,hca);
		huffUtil = new HuffmanCompressionUtilities();
		String base = "simpler";
		updateFileNames(base);
		checkWeightsFile();
		huffUtil.setWeights(new int[NUM_ASCII]);
		removeBinaryFile();
		enc_dec.encode(textFile,encodeFile,weightsFile,optimize);
		removeBinaryFile();
        int[]   expectedSize = new int[]    {1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3};
		String[] testStrings = new String[] {"t","h"," t","th","he","it","an","tnt","in","hen","ta ta"};
		
		for (int i = 0; i < testStrings.length; i++ ) {
			File binFile = new File("encode/test_writeEOFandPadding_"+i+".bin");
			File testFile = new File("data/test_writeEOFandPadding_"+i+".txt");
			writeTestTextFile(testFile,testStrings[i]);
			enc_dec.encode(testFile.getPath(), binFile.getPath(), weightsFile, optimize);

			assertTrue(expectedSize[i] == binFile.length());
			assertTrue(hflib.checkBinaryFiles(binFile));
			assertTrue(testFile.delete());
			assertTrue(binFile.delete());
		}
	}

	/**
	 * Write the temporary test text file for checking EOF padding
	 *
	 * @param outf the outf
	 * @param str the str
	 */
	private void writeTestTextFile(File outf,String str) {
		BufferedWriter bw = fio.openBufferedWriter(outf);
		try {
			for (int i = 0; i < str.length(); i++) {
				bw.write(str.charAt(i));
			}
			fio.closeFile(bw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Read 8 bytes.
	 *
	 * @param bis the bufferedInputStream
	 * @param expect - the expected values of the first 8 bytes
	 * @return true, if successful
	 */
	private boolean read8Bytes(BufferedInputStream bis, int[] expect) {
		int [] actual = new int[8];
		boolean status = true;
		try {
			for (int i = 0; i < 8; i++) {
				actual[i] = (bis.read() & 0xff);
				status = status && (actual[i] == expect[i]);
//				System.out.printf("%d,",actual[i]);
			}
			bis.close();
		} catch (IOException e) {
			System.out.println("IO Exception occurred while reading binary file\n");
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Check binary output.
	 *
	 * @param binOutf the bin outf
	 * @param expectedSize the expected size
	 * @return true, if successful
	 */
	private boolean checkBinaryOutput(File binOutf, int expectedSize) {
		if (!binOutf.exists()) {
			System.out.println("   Encode did not create file "+binOutf.getPath());
			return false;
		}
		long bfLen = binOutf.length();
		if (bfLen == 0) {
			System.out.println("   Encode created empty file "+binOutf.getPath());
			return false;			
		} else if (bfLen != expectedSize) {
			System.out.println("   Encode created file "+binOutf.getPath()+", but file size does not match expectations");
			System.out.println("   Expected Length: "+expectedSize);
			System.out.println("   Actual Length:   "+bfLen);
			return false;			
		}
		return true;
	}
	
	/**
	 * Checks to see if the weights file exists, and if it does, deletes it
	 * Then it regenerates the weights and checks that it was generated as expected
	 *
	 * @return true, if new weights file exists
	 */
	private boolean checkWeightsFile() {
		File wtsFh = new File(weightsFile);
		System.out.println("Generating weights file: "+ weightsFile);

		System.out.println("Checking if file exists: "+wtsFh.getPath());
		if (wtsFh.exists()) 
			wtsFh.delete();
		
		weights = gw.readInputFileAndReturnWeights(textFile);
		writeWeightsFile(wtsFh);
		return  (wtsFh.exists()); 
	}	
	
	/**
	 * Write weights file - avoiding the use of GenWeights method
	 *
	 * @param outf the outf
	 */
	private void writeWeightsFile(File outf) {
		String line;
		try {
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outf)));
			for (int i = 0; i < weights.length; i++ ) {
					line = i+","+weights[i]+",\n";
				output.writeBytes(line);
			}
			output.flush();
			output.close();
		} catch (IOException e) {
			System.err.println("Error in writing file: "+outf.getPath());
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the encoded binary, it it exists
	 *
	 * @return true, if successful
	 */
	private boolean removeBinaryFile() {
		binOutf = new File(encodeFile);

		System.out.println("Checking if file exists: "+binOutf.getPath());
		if (binOutf.exists()) {
			if (binOutf.delete()) {
				System.out.println("Deleted file: "+binOutf.getPath());
				return true;
			} else {
				return false;
			}
		}
		return true;
	}	
		
}
