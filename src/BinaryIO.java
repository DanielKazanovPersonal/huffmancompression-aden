import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * The Class BinaryIO.
 */
public class BinaryIO {
	
	/**
	 * Instantiates a new binary IO.
	 */
	public BinaryIO() {
	}
	
	/**
	 * Converts a string of eight 1's and 0's to one byte. 
	 * The caller MUST guarantee that the string of 1's 
	 * and 0's is 8 bits - no more, no less.
	 *
	 * @param binStr the incoming binary string for the current character
	 * @return the int generated from the binary string
	 */
	int convStrToBin(String binStr) {
		int result = 0; // the variable to return
		
		for (int i = 0; i < binStr.length(); i++) {
			if (binStr.charAt(i) == '1') {
				result += 1;
			}
			if (i < binStr.length() - 1) {
				result *= 2;
			}
		}
		return result;
	}
	
	/**
	 * Convert a byte value into a string of eight 1's and 0's (MSB to LSB).
	 *
	 * @param aByte the byte to convert
	 * @return the binary string of 1's and 0's that represents aByte
	 */
	String convBinToStr(int aByte) {
		String str = "";
		int mask = 0x80;
		for (int i = 0; i < 8; i++) { // 8 * 1 = 8
			str += ((mask & aByte) > 0) ? "1" : "0";
			mask = mask >>> 1; // shift by 1 digit
		}
		return str;
	}
	
	/**
	 * WriteBinStr - this method attempts to convert a binary string 
	 *               to one or more bytes, and write them to the binary
	 *               file specified. Any remaining unwritten bits in the
	 *               binary string are returned to the caller.
	 * Algorithm:	While the binary string has *more* than 8 bits
	 *                 - convert the first 8 bits to a byte value
	 *                 - write the converted value to the file
	 *                 - remove the first 8 bits from the binary Str
	 *                 
	 *              If the binary string has 8 bits
	 *                 - convert the string to a byte value
	 *                 - write the converted value to the file
	 *                 - return "";
	 *              else 
	 *                 - return the binary string
	 *
	 * @param bos - the binary file to be created
	 * @param binStr - the binary string of 1's and 0's to be written to the file
	 * @return the string of any unwritten bits...
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	 String writeBinString(BufferedOutputStream bos, String binStr) throws IOException {
		 while (binStr.length() > 8) {
			 int byteValue = convStrToBin(binStr.substring(0, 8));
			 bos.write(byteValue);
			 binStr = binStr.substring(8, binStr.length());
		 }
		 
		 if (binStr.length() == 8) {
			 int byteValue = convStrToBin(binStr);
			 bos.write(byteValue);
			 return "";
		 } else {
			 return binStr;
		 }
	 }
	
}

