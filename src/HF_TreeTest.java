
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.aden.hf_lib.HuffCompTestLib;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

// TODO: Auto-generated Javadoc
/**
 * The Class HF_TreeTest. This tests the building of the Huffman Tree methods in 
 * the HuffmanCompressionUtilities Class
 */
@TestMethodOrder(OrderAnnotation.class)
class HF_TreeTest {
	
	/** The hflib. The jar file used for analyzing the results and determining pass/fail*/
	HuffCompTestLib hflib = new HuffCompTestLib();
	
	/** The class being tested */
	HuffmanCompressionUtilities huffUtil;
	
	/** Required to generate the weights file on the fle. */
	GenWeights gw;
	
	/** The Alert interface between GenWeights and the GUI (which is null). */
	HuffCompAlerts hca;
    
    /** The no CR. Used to determine if the CR is present in the weights or not*/ 
    private static boolean noCR = false; 
    
    /** optimize indicates whether or not to exclude nodes with zero weight or not */
    private boolean optimize = false;
    
    /** The dir. */
    String dir = "data/";
    
    /**
     * Gets the operating system.
     *
     * @return the operating system
     */
    private static String getOperatingSystem() {
    	String os = System.getProperty("os.name");
    	return os;
    }
	
	/**
	 * Sets the up before class. Identifies the operating system
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Running on: "+getOperatingSystem());
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
	 * Overall test tree methodology:
	 * 1) set optimize to true (minimal tree including only characters actually present in the
	 *    input file) or false (tree generated with ALL characters).
	 * 2) initialize huffUtil, hca and gw instances and set the test name
	 * 3) recalculate the weights using GenWeights
	 * 4) determine if CR is present or not
	 * 5) set the weights in huffUtil
	 * 6) build the huffman tree according to the optimize flag
	 * 7) create the Huffman codes
	 * 8) print the huffman tree --> writes the tree to String str in huffUtil
	 * 9) huffUtil.toString() returns str, which is checked for correctness and determines pass/fail
	 * 
	 */
	
	/**
	 * Test tree simple optimize. This will generate the huffman tree with only characters 
	 * contained in simple.txt. 
	 */
	@Test
	@Order(1)
	void test_tree_simple_optimize() {
		optimize = true;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "simple.txt";

		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		huffUtil.printHuffmanTree(huffUtil.getTreeRoot(),0);
		System.out.println(huffUtil.toString());
		assertTrue(hflib.checkHuffmanTree(fname, noCR, optimize, huffUtil.toString()));
	}
	
	/**
	 * Test tree simple full. This will generate the huffman tree with all 128 
	 * ASCII characters 
	 */
	@Test
	@Order(2)
	void test_tree_simple_full() {
		optimize = false;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "simple.txt";

		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		huffUtil.printHuffmanTree(huffUtil.getTreeRoot(),0);
		System.out.println(huffUtil.toString());
		assertTrue(hflib.checkHuffmanTree(fname, noCR, optimize, huffUtil.toString()));
	}
	
	/**
	 * Test tree "Green Eggs and Ham" - optimized
	 */
	@Test
	@Order(3)
	void test_tree_GEAH_optimize() {
		optimize = true;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "Green Eggs and Ham.txt";
		
		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		huffUtil.printHuffmanTree(huffUtil.getTreeRoot(),0);
		assertTrue(hflib.checkHuffmanTree(fname, noCR, optimize, huffUtil.toString()));
	}

	/**
	 * Overall encodeMap test methodology:
	 * 1) set optimize to true (minimal tree including only characters actually present in the
	 *    input file) or false (tree generated with ALL characters).
	 * 2) initialize huffUtil, hca and gw instances and set the test name
	 * 3) recalculate the weights using GenWeights
	 * 4) determine if CR is present or not
	 * 5) set the weights in huffUtil
	 * 6) build the huffman tree according to the optimize flag
	 * 7) create the Huffman codes
	 * 9) huffUtil.checkEncodeMap() checks the encodeMap for correctness and determines pass/fail
	 * 
	 */

	/**
	 * Test encode map simple optimize.
	 */
	@Test
	@Order(4)
	void test_encodeMap_simple_optimize() {
		optimize = true;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "simple.txt";

		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		assertTrue(hflib.checkEncodeMap(fname, noCR, optimize, huffUtil.getEncodeMap()));
	}
	
	/**
	 * Test encode map simple full.
	 */
	@Test
	@Order(5)
	void test_encodeMap_simple_full() {
		optimize = false;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "simple.txt";

		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		assertTrue(hflib.checkEncodeMap(fname, noCR, optimize, huffUtil.getEncodeMap()));
	}
	
	/**
	 * Test encode map Green Eggs and Ham optimized.
	 */
	@Test
	@Order(6)
	void test_encodeMap_GEAH_optimize() {
		optimize = true;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "Green Eggs and Ham.txt";
		
		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		assertTrue(hflib.checkEncodeMap(fname, noCR, optimize, huffUtil.getEncodeMap()));
	}
	
	/**
	 * Test encode map The Cat in the Hat optimized.
	 */
	@Test
	@Order(7)
	void test_encodeMap_TCITH_optimize() {
		optimize = true;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "The Cat in the Hat.txt";
		
		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		assertTrue(hflib.checkEncodeMap(fname, noCR, optimize, huffUtil.getEncodeMap()));
	}
	
	/**
	 * Test encode map Harry Potter and the Sorcerer's Stone optimized
	 */
	@Test
	@Order(8)
	void test_encodeMap_HPATS_optimize() {
		optimize = true;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "Harry Potter and the Sorcerer.txt";
		
		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		assertTrue(hflib.checkEncodeMap(fname, noCR, optimize, huffUtil.getEncodeMap()));
	}
	
	/**
	 * Test encode map War and Peacie optimized.
	 */
	@Test
	@Order(9)
	void test_encodeMap_WAP_optimize() {
		optimize = true;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "warAndPeace.txt";
		
		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		assertTrue(hflib.checkEncodeMap(fname, noCR, optimize, huffUtil.getEncodeMap()));
	}
	
	/**
	 * Test encode map Harry Potter and the Sorcerer's Stone -  full encoding
	 */
	@Test
	@Order(10)
	void test_encodeMap_HPATS_full() {
		optimize = false;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "Harry Potter and the Sorcerer.txt";
		
		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		huffUtil.setWeights(weights);
		noCR = (weights[13] == 0);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		assertTrue(hflib.checkEncodeMap(fname, noCR, optimize, huffUtil.getEncodeMap()));
	}
	
	/**
	 * Test encode map for War and Peace - full encoding.
	 */
	@Test
	@Order(11)
	void test_encodeMap_WAP_full() {
		optimize = false;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "warAndPeace.txt";
		
		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		noCR = (weights[13] == 0);
		huffUtil.setWeights(weights);
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		assertTrue(hflib.checkEncodeMap(fname, noCR, optimize, huffUtil.getEncodeMap()));
	}

	/**
	 *  Test for the readFreqWeights method.  
	 *  This method uses the readInputFileAndReturnWeights method to both generate the
	 *  weights in GenWeights.java for Harry Potter and get the array of weights. Then 
	 *  the weights are saved to a temporary file, and the readFreqWeights method is used to 
	 *  read the weights file and return the results as the readWeights array. Each index of
	 *  weights[] and readWeights[] are compared for equality; any mismatch is a failure.
	 *  The temporary weights file is then deleted.
	 */
	@Test
	@Order(12)
	void test_readFreqWeights() {
		optimize = false;
		huffUtil = new HuffmanCompressionUtilities();
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		String fname = "Harry Potter and the Sorcerer.txt";
		File testWeights = new File("weights/readFreqWeights.csv");

		int[] weights = gw.readInputFileAndReturnWeights(dir+fname);
		gw.saveWeightsToFile(testWeights.getPath());
		
		int[] readWeights = huffUtil.readFreqWeights(testWeights);
		for (int i = 0; i < weights.length; i++) {
			assertTrue(weights[i] == readWeights[i]);
		}
		assertTrue(testWeights.delete());
	}

	
}
