import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
class HF_EOF_DebugTest {

	EncodeDecode enc_dec;
	HuffmanCompressionUtilities huffUtil;
	GenWeights gw;
	HuffCompAlerts hca;
	HuffCompTestLib hflib = new HuffCompTestLib();
	BinaryIO binUtils = new BinaryIO();
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
	void test_EOF_debug() {
		System.out.println("Test 1: Checking Write EOF and padding");
		boolean pass = true;
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
			System.out.println("Test iteration: "+i);
			System.out.println("   Writing Text File: "+testFile.getPath());
			writeTestTextFile(testFile,testStrings[i]);
			validateTestTextFile(testFile,testStrings[i]);
			enc_dec.encode(testFile.getPath(), binFile.getPath(), weightsFile, optimize);
			System.out.println("   Check Encoded File length:");
			System.out.println("       Expected: "+expectedSize[i]);
			System.out.println("         Actual: "+binFile.length());
			System.out.println("    Checking encoded file contents:");
			readBinFile(binFile);
			
			assertTrue(expectedSize[i] == binFile.length());
			assertTrue(hflib.checkBinaryFiles(binFile));
//			assertTrue(testFile.delete());
//			assertTrue(binFile.delete());
		}
	}
	
	private void readBinFile(File binFile) {
		BufferedInputStream bis = fio.openBufferedInputStream(binFile);
		int c;
		try {
			while ((c=bis.read()) != -1) {
				System.out.printf("     Read: %02x (%s)\n", c,binUtils.convBinToStr(c));
			}
			fio.closeStream(bis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the temporary test text file for checking EOF padding
	 *
	 * @param outf the outf
	 * @param str the str
	 */
	private boolean validateTestTextFile(File outf,String str) {
		boolean status = false;
		String line="";
		BufferedReader br = fio.openBufferedReader(outf);
		try {
			line = br.readLine();
			status = str.equals(line);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!status) {
			System.out.println("   Write text file contents do not match!");
			System.out.println("   Expected: "+str);
			System.out.println("   Actual:   "+line);
		}
		return status;
	}
	
	/**
	 * Write the temporary test text file for checking EOF padding
	 *
	 * @param outf the outf
	 * @param str the str
	 */
	private boolean writeTestTextFile(File outf,String str) {
		boolean status = false;
		if (outf.exists()) {
			outf.delete();
			if ( outf.exists()) 
				System.out.println("   Unable to delete file: "+outf.getPath());
		}
		
		BufferedWriter bw = fio.openBufferedWriter(outf);
		try {
			for (int i = 0; i < str.length(); i++) {
				bw.write(str.charAt(i));
			}
			fio.closeFile(bw);
			status = outf.exists();
			if (!status) {
				System.out.println("   Text File: "+outf.getPath()+" does not exist");
				return status;
			}
			status = (outf.length() == str.length());
			if (!status) 
				System.out.println("   Text File: "+outf.getPath()+" is not the expected size");
		    	
			return status;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
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
