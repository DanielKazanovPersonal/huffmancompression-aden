
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
	
	/**
	 * Constructor for the HuffCompAlerts class. Connects the class to the GUI
	 *
	 * @param gui the gui
	 */
	public HuffCompAlerts (HuffCompGUI gui) {
		this.gui = gui;
	}

	/**
	 * Based upon the alert type and the GUI is not null, forwards the hdr and 
	 * message to the appropriate alert method in the GUI. If the GUI is null,
	 * outputs the hdr and message to the Console.
	 *
	 * @param alert the alert
	 * @param hdr the hdr
	 * @param message the message
	 * @return true, if is sue alert
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
		}
		return result;
	}

}
