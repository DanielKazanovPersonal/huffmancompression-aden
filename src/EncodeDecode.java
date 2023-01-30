import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;


// TODO: Auto-generated Javadoc
/**
 * The Class EncodeDecode. This is a temporary placeholder to support compilation.
 *                         It will be replaced later in the project
 */
public class EncodeDecode {
	
	/** Link to the GenWeights instance */
	private GenWeights gw;
	
	/** Link to the HuffCompAlerts instance */
	private HuffCompAlerts hca;
	
	/**
	 * Instantiates a new encode decode.
	 *
	 * @param gw the gw
	 * @param hca the hca
	 */
	public EncodeDecode (GenWeights gw, HuffCompAlerts hca) {
		this.gw = gw;
		this.hca = hca;
	}
	
	/**
	 * Encode. Uses Huffman Compression to encode the specified source textfile
	 *         based upon the weight information supplied.
	 *         - error checks all files, generates weights if weights file does
	 *           not exist
	 *         - creates the Huffman Tree for compression
	 *         - compresses the file
	 *
	 * @param fName the filename of the text file being encoded
	 * @param bfName the filename of the encoded file being created.
	 * @param freqWts the filename of the frequency weights used to encode the file
	 * @param optimize - only generate tree nodes for characters with non-zero weights.
	 */
	void encode(String fName,String bfName, String freqWts, boolean optimize) {
		System.out.println("Encoding is not enabled.....");
	}
	
	/**
	 * Decode. Uses Huffman Compression to decode the specified source textfile
	 *         based upon the weight information supplied.
	 *         - error checks all files
	 *         - creates the Huffman Tree for decompression
	 *         - decompresses the file
	 *
	 * @param bfName the filename of the encoded file being read.
	 * @param ofName the filename of the decoded file being created
	 * @param freqWts the filename of the frequency weights used to decode the file
	 * @param optimize - only generate tree nodes for characters with non-zero weights.	 *
	 */
	public void decode(String bfName, String ofName, String freqWts,boolean optimize) {
		System.out.println("Decoding is not enabled.....");
	}
}
