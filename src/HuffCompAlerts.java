import java.util.ArrayList;

/**
 * This class is provided to insulate the EncodeDecode and GenWeights classes from
 * having to know anything about the GUI when raising alerts. This makes JUnit testing
 * much easier and less fraught with peril. YOU SHOULD NOT HAVE TO TOUCH THIS CODE!
 * 
 * @author scott
 *
 */
public class HuffCompAlerts {
	
	/** The gui. */
	private HuffCompGUI gui;	
	
	/** The last alert type - used by the JUnit to ensure that alerts are appropriately tested. */
	private ArrayList<String> lastAlertType;
	
	/**
	 * Constructor for the HuffCompAlerts class. Connects the class to the GUI
	 *
	 * @param gui the gui
	 */
	public HuffCompAlerts (HuffCompGUI gui) {
		this.gui = gui;
		lastAlertType = new ArrayList<String>();
	}

	/**
	 * Based upon the alert type and the GUI is not null, forwards the hdr and 
	 * message to the appropriate alert method in the GUI. If the GUI is null,
	 * outputs the hdr and message to the Console.
	 *
	 * @param alert the alert type
	 * @param hdr the header message (a JavaFX Alert has both a header to indicate
	 *            general information ("Input Error") and a message that provides 
	 *            the details
	 * @param message the detailed error message
	 * @return true, with the exception of GUI enabled CONFIRM alerts, in which
	 *            case it returns true if the user clicked OK and false if the user
	 *            clicked Cancel.
	 */
	boolean issueAlert(HuffAlerts alert, String hdr, String message) {
		boolean result = true;
		if (gui != null) {
			switch (alert) {
			case INPUT: gui.inputAlert(hdr,message);break;
			case OUTPUT: gui.outputAlert(hdr,message);break;
			case CONFIRM: result = gui.confirmAlert(hdr,message);break;
			case DONE: gui.doneAlert(hdr,message);break;
			}
		} else {
			System.out.println("HuffCompAlert: "+hdr+" ==> "+message);
			lastAlertType.add(alert.name());
		}
		return result;
	}
	
	/**
	 * Gets the ArrayList of all recorded alerts.
	 *
	 * @return the last alert type
	 */
	ArrayList<String> getLastAlertType() {
		return lastAlertType;
	}

	/**
	 * Clears the lastAlertType ArrayList.
	 */
	void resetLastAlertType() {
		lastAlertType.clear();
	}
}
