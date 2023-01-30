/*
 *  JUnit test for the GenWeights Class.
 */
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.aden.hf_lib.HuffCompTestLib;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class HF_GenWeightsTest {

	HuffCompTestLib hflib = new HuffCompTestLib();
	GenWeights gw;
	HuffCompAlerts hca;
    private static boolean ignoreChr13 = false; 
    String dir = "data/";
    
    /**
     * Gets the operating system from the System.
     *
     * @return the operating system from the 
     */
    private static String getOperatingSystem() {
    	String os = System.getProperty("os.name");
    	return os;
    }
	
	/**
	 * Determines which OS the test is running on, as it makes a difference
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println("Running on: "+getOperatingSystem());
		ignoreChr13 = !(getOperatingSystem().contains("Win"));
		if (ignoreChr13) {
			System.out.println("Non-Windows platform detected; setting ignoreChr13="+ignoreChr13);			
		} else {
			System.out.println("Windows platform detected; setting ignoreChr13="+ignoreChr13);			
		}
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
	 * Test GenWeights on simple.txt
	 */
	@Test
	@Order(1)
	void test_GenWeights_simple() {
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		
		int[] tstWeights = gw.readInputFileAndReturnWeights(dir+"simple.txt");
		boolean ignCHR13 = (tstWeights[13] == 0);
		assertTrue(hflib.checkWeights("simple.txt", ignCHR13, tstWeights));
	}
	
	/**
	 * Test GenWeights on "Green Eggs and Ham.txt".
	 */
	@Test
	@Order(2)
	void test_GenWeights_GEAH() {
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		
		int[] tstWeights = gw.readInputFileAndReturnWeights(dir+"Green Eggs and Ham.txt");
		boolean ignCHR13 = (tstWeights[13] == 0);
		assertTrue(hflib.checkWeights("Green Eggs and Ham.txt", ignCHR13, tstWeights));
	}

	/**
	 * Test GenWeights on "The Cat in the Hat.txt".
	 */
	@Test
	@Order(3)
	void test_GenWeights_TCITH() {
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		
		int[] tstWeights = gw.readInputFileAndReturnWeights(dir+"The Cat in the Hat.txt");
		boolean ignCHR13 = (tstWeights[13] == 0);
		assertTrue(hflib.checkWeights("The Cat in the Hat.txt", ignCHR13, tstWeights));
	}

	/**
	 * Test GenWeights on "Harry Potter and the Sorcerer's Stone.txt".
	 */
	@Test
	@Order(4)
	void test_GenWeights_HPATS() {
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		
		int[] tstWeights = gw.readInputFileAndReturnWeights(dir+"Harry Potter and the Sorcerer.txt");
		boolean ignCHR13 = (tstWeights[13] == 0);
		assertTrue(hflib.checkWeights("Harry Potter and the Sorcerer.txt", ignCHR13, tstWeights));
	}

	/**
	 * Test GenWeights on "warAndPeace.txt".
	 */
	@Test
	@Order(5)
	void test_GenWeights_WAP() {
		hca = new HuffCompAlerts(null);
		gw = new GenWeights(hca);
		
		int[] tstWeights = gw.readInputFileAndReturnWeights(dir+"warAndPeace.txt");
		boolean ignCHR13 = (tstWeights[13] == 0);
		assertTrue(hflib.checkWeights("warAndPeace.txt", ignCHR13, tstWeights));
	}

}
