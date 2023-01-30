package myfileio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Class MyFileIO.
 */
public class MyFileIO {
	
	/** The Constant FILE_OK. Indicates that the file can be opened for read or write */
	public static final int FILE_OK=0;
	
	/** The Constant EMPTY_NAME. */
	public static final int EMPTY_NAME=1;
	
	/** The Constant NOT_A_FILE. */
	public static final int NOT_A_FILE = 2;
	
	/** The Constant READ_EXIST_NOT. */
	public static final int FILE_DOES_NOT_EXIST=3;
	
	/** The Constant READ_ZERO_LENGTH. */
	public static final int READ_ZERO_LENGTH=4;
	
	/** The Constant NO_READ_ACCESS. */
	public static final int NO_READ_ACCESS=5;
	
	/** The Constant NO_WRITE_ACCESS. */
	public static final int NO_WRITE_ACCESS=6;
	
	/** The Constant WRITE_EXISTS. */
	public static final int WRITE_EXISTS=7;
	
	/**
	 * Gets the file handle.
	 *
	 * @param filename the filename
	 * @return the file handle
	 */
	public File getFileHandle (String filename) {
		if (filename == null) return null;
		return (new File(filename));
	}
	
	/**
	 * Creates an empty file. Checks for valid file name and that file does
	 * not already exist. Catches all exceptions and prints the stack trace.
	 *
	 * @param filename the filename. 
	 * @return true, if file was successfully created
	 */
	public boolean createEmptyFile(String filename) {
	    File fh = getFileHandle(filename);
	    boolean status = false;
	    if (fh == null) return status;
	    if ("".equals(filename) || fh.exists())
	    	return status; 
	    try {
	    	status = fh.createNewFile();
	    }
	    catch (IOException e) {
	    	System.out.println("An IOException occurred.");
	    	e.printStackTrace();
	    }
	    catch (SecurityException e) {
	    	System.out.println("A Security Exception occurred.");
	    	e.printStackTrace();
	    }
	    return status;
	}
	
	/**
	 * Delete file. Checks to see that the filename is valid, the file exists and
	 * is a file before attempting to delete. Catches all exceptions and prints the
	 * stack trace.
	 *
	 * @param filename the filename
	 * @return true, if successful
	 */
	public boolean deleteFile(String filename) {
	    File fh = getFileHandle(filename);
	    boolean status = false;
	    if (fh == null) return status;
	    if ("".equals(filename) || !fh.exists() || !fh.isFile())
	    	return status; 
	    try {
	    	status = fh.delete();
	    }
	    catch (SecurityException e) {
	    	System.out.println("A Security Exception occurred.");
	    	e.printStackTrace();
	    }
	    return status;	
	}	

	/**
	 * Gets the file status. Interprets the File information based upon whether
	 * the file is being checked for read or write. Returns an integer status based
	 * upon the information and access type
	 * 
	 * For read, information is checked IN THIS ORDER!:
	 *    - filename is "" ==> returns EMPTY_NAME
	 *    - file does not exist ==> returns READ_EXIST_NOT
	 *    - File does not represent a file - ie, directory or link - ==> returns NOT_A_FILE;
	 *    - file exists but has zero length ==> returns READ_ZERO_LENGTH
	 *    - file exists, has data, but cannot be accessed ==> returns NO_READ_ACCESS;
	 *    if all of these checks pass ==> return FILE_OK;
	 *    
	 *    For write:
	 *    - if file exists, but cannot be written ==> return NO_WRITE_ACCESS
	 *    - if file exists and is a file ==> return WRITE_EXISTS 
	 *      (this is possibly a passing condition, but might want to prompt to confirm)
	 *    - if file exists but is not a file ==> return NOT_A_FILE;
	 *    otherwise return FILE_OK; 
	 *
	 * @param file the file
	 * @param read the read
	 * @return the file status
	 */
	public int getFileStatus(File file, boolean read) {
		if ("".equals(file.getName())) 
			return EMPTY_NAME;
		if (read) {
			if (!file.exists())
				return FILE_DOES_NOT_EXIST;
			if (!file.isFile())
				return NOT_A_FILE;
			if (file.length() == 0)
				return READ_ZERO_LENGTH;
			if (!file.canRead()) 
				return NO_READ_ACCESS;
		} else {
			if (file.exists()) {
				if (!file.canWrite()) 
					return NO_WRITE_ACCESS;
				return (file.isFile() ? WRITE_EXISTS : NOT_A_FILE);
			}
		}
		return FILE_OK;
	}
	
	/**
	 * Open file reader. Catch all exceptions, and warn user.
	 *
	 * @param file the file
	 * @return the file reader - returns null if an error occured
	 */
	public FileReader openFileReader(File file) {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		}
		catch (FileNotFoundException e) {
			System.out.println("ERROR - Did not find file for reading!!");
		}
		return fr;
	}
	
	/**
	 * Open file input stream. Catch exceptions and warn user.
	 *
	 * @param file the file
	 * @return the file input stream - returns null if an error occured 
	 */
	public FileInputStream openFileInputStream(File file ) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR - Unable to read file or open as FileInputStream");
		}
		return fis;
	}
	
	/**
	 * Open file writer. Catch exceptions and warn user
	 *
	 * @param file the file
	 * @return the file writer - returns null if an error occured
	 */
	public FileWriter openFileWriter(File file) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		}
		catch (IOException e) {
			System.out.println("IO Exception error - could not open file for writing!!");
		}
		return fw;
	}
	
	/**
	 * Open file output stream. Catch exceptions and warn user
	 *
	 * @param file the file
	 * @return the file output stream - returns null if an error occured
	 */
	public FileOutputStream openFileOutputStream(File file ) {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR - Unable to write file or open as FileOutputStream");
		}
		return fos;
	}
	
	
	/**
	 * Open buffered reader. 
	 *
	 * @param file the file
	 * @return the buffered reader - returns null if an error occured
	 */
	public BufferedReader openBufferedReader(File file) {
		BufferedReader br = null;
		FileReader fr = openFileReader(file);
		if (fr != null)
			br = new BufferedReader(fr);
		return br;
	}
	
	/**
	 * Open buffered input stream.
	 *
	 * @param file the file
	 * @return the buffered input stream - returns null if an error occured
	 */
	public BufferedInputStream openBufferedInputStream(File file) {
		BufferedInputStream bis = null;
		FileInputStream fis = openFileInputStream(file);
		if (fis != null)
			bis = new BufferedInputStream(fis);
		return bis;
	}
	
	/**
	 * Open buffered writer.
	 *
	 * @param file the file
	 * @return the buffered writer - returns null if an error occured
	 */
	public BufferedWriter openBufferedWriter(File file) {
		BufferedWriter bw = null;
		FileWriter fw = openFileWriter(file);
		if (fw != null)
			bw = new BufferedWriter(fw);
		return bw;
	}
	
	/**
	 * Open buffered output stream.
	 *
	 * @param file the file
	 * @return the buffered output stream
	 */
	public BufferedOutputStream openBufferedOutputStream(File file) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = openFileOutputStream(file);
		if (fos != null)
			bos = new BufferedOutputStream(fos);
		return bos;
	}
	
	/**
	 * Close file. Catch exceptions, inform user and print stack trace
	 *
	 * @param fr the fr
	 */
	public void closeFile(FileReader fr) {
		try {
			fr.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to close FileReader failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * Close file. Catch exceptions, inform user and print stack trace
	 *
	 * @param fw the fw
	 */
	public void closeFile(FileWriter fw) {
		try {
			fw.flush();
			fw.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to flush and close FileWriter failed");
			e.printStackTrace();
		}
	}

	/**
	 * Close file. Catch exceptions, inform user and print stack trace
	 *
	 * @param br the br
	 */
	public void closeFile(BufferedReader br) {
		try {
			br.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to close BufferedReader failed");
			e.printStackTrace();
		}
	}

	/**
	 * Close file.
	 *
	 * @param bw the bw
	 */
	public void closeFile(BufferedWriter bw) {
		try {
			bw.flush();
			bw.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to flush and close BufferedWriter failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * Close stream. Catch exceptions, inform user and print stack trace
	 *
	 * @param bis the bis
	 */
	public void closeStream(BufferedInputStream bis) {
		try {
			bis.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to close BufferedInputStream failed");
			e.printStackTrace();
		}
	}

	/**
	 * Close stream. Catch exceptions, inform user and print stack trace
	 *
	 * @param bos the bos
	 */
	public void closeStream(BufferedOutputStream bos) {
		try {
			bos.flush();
			bos.close();
		}
		catch (IOException e) {
			System.out.println("Attempt to flush and close BufferedOutputStream failed");
			e.printStackTrace();
		}
	}

}
