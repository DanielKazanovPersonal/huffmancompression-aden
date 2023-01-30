import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Class HuffCompGUI. This is the unified GUI for the ADEN Huffman Compression
 * assignment. Minimal changes will be required as the project progress to enable
 * certain features.
 * 
 * @author scott
 *
 */
public class HuffCompGUI extends Application {
	/** constants */
	private final int SOURCE_ROW = 0;
	private final int WEIGHTS_ROW = SOURCE_ROW+1;
	private final int ENCODE_ROW = WEIGHTS_ROW+1;
	private final int DECODE_ROW = ENCODE_ROW+1;
	private final int OPTIONS_HEIGHT = 40;
	private final int HEADER_HEIGHT = 60;
	private final double LABEL_COL_WIDTH = 120;
	private final double BUTTON_COL_WIDTH = 100;
	private final double TF_WIDTH = 300;
	private final int LABEL_COL = 0;
	private final int TF_COL = 1;
	private final int BUTTON_COL = 2;
	
	/** panes */
	private BorderPane pane = new BorderPane();
    private GridPane gp;
    
    /** boxes */
    private HBox optionBox = new HBox(15);
    
    /** buttons and checkboxes */
	private Button btnGenWeights;
	private Button btnEncode;
	private Button btnDecode;
	private Button btnClear;
    private CheckBox ckbSaveWeights;
    private CheckBox ckbOptimize;

    /** labels */
    private Label hdr;
    private Label lblSource;
    private Label lblWeights;
    private Label lblEncode;
    private Label lblDecode;
    
    /** text fields */
	private ComboBox<String> sourceFile;
	private TextField weightsFile;
	private TextField encodeFile;
	private TextField decodeFile;
    
    private boolean saveWeights = true;
    private boolean optimize = true;
    private GenWeights gw;
    private EncodeDecode encdec;
    private HuffCompAlerts hca;
    
	/**
	 * Instantiates a new huffman compression GUI.
	 */
	public HuffCompGUI() {
		// TODO Auto-generated constructor stub
		hca = new HuffCompAlerts(this);
		gw = new GenWeights(hca);
		gw.initWeights();
		encdec = new EncodeDecode(gw, hca);
	}
	
	/**
	 * Initializes all labels used by the GUI
	 */
	private void initLabels() {
		hdr = new Label("ADEN Huffman Compression");
		hdr.setFont(Font.font("Arial",FontWeight.BOLD,24));
		hdr.setPrefHeight(HEADER_HEIGHT);
		
		lblSource = new Label("Source File: ");
		lblSource.setFont(Font.font("Arial",FontWeight.BOLD,14));	
		lblWeights = new Label("Weights File: ");
		lblWeights.setFont(Font.font("Arial",FontWeight.BOLD,14));	
		lblEncode = new Label("Encoded File: ");
		lblEncode.setFont(Font.font("Arial",FontWeight.BOLD,14));	
		lblDecode = new Label("Decoded File: ");
		lblDecode.setFont(Font.font("Arial",FontWeight.BOLD,14));	
	}
	
	/**
	 * Initializes all TextFields and ComboBoxes used by the GUI
	 * Only the sourceFile (in the data/ directory) uses a ComboBox
	 * to enforce selecting a valid file. Adds a listener to 
	 * sourceFile to force an update to the list of available files
	 * if the selected value changes - in case any new .txt files 
	 * were added to the data/ directory (or deleted from it)
	 */
	private void initTextFieldsComboBoxes() {
		sourceFile = new ComboBox<String>();
		sourceFile.setPrefWidth(TF_WIDTH);
		sourceFile.setOnMouseClicked(e->getSourceFiles());
		sourceFile.getSelectionModel()
		          .selectedItemProperty()
		          .addListener((options,ov,nv) -> updateTextFieldDefaults(nv));
		weightsFile = new TextField();
		weightsFile.setPrefWidth(TF_WIDTH);
		encodeFile = new TextField();
		encodeFile.setPrefWidth(TF_WIDTH);
		decodeFile = new TextField();
		decodeFile.setPrefWidth(TF_WIDTH);
		
	}
	
	/**
	 * Initializes the buttons and check boxes used by the GUI.
	 * - instantiates the buttons
	 * - defines the set on actions.
	 * - makes the selected state match the default state
	 */
	private void initButtonsCheckBoxes() {
		btnGenWeights = new Button("Generate");
		btnEncode = new Button("Encode");
		btnDecode = new Button("Decode");
		btnClear = new Button("Clear Fields");
		ckbSaveWeights = new CheckBox("Save Weights");
		ckbSaveWeights.setSelected(saveWeights);
		ckbOptimize = new CheckBox("Optimize");
		ckbOptimize.setSelected(optimize);

		btnGenWeights.setOnAction(e -> generateWeights());
		btnClear.setOnAction(e -> clearAllFields());
		btnEncode.setOnAction(e->executeEncode());
		btnDecode.setOnAction(e->executeDecode());
		ckbSaveWeights.setOnAction(e -> saveWeights = ckbSaveWeights.isSelected());
		ckbOptimize.setOnAction(e -> {optimize = ckbOptimize.isSelected();
									  updateTextFieldDefaults(sourceFile.getValue());
		});
	}
	
	/**
	 * Adds a row with a TextField as the user input to the GridPane
	 *
	 * @param row the row to update
	 * @param lbl the Label describing the intent of the TextField
	 * @param tf the actual TextField user input for this row
	 * @param btn if not null, the Button to take an associated action
	 */
	private void gpAddTFRow(int row, Label lbl, TextField tf, Button btn) {
		gp.add(lbl, LABEL_COL, row);
		GridPane.setHalignment(lbl, HPos.RIGHT);
		gp.add(tf, TF_COL, row);
		if (btn != null) {
			gp.add(btn, BUTTON_COL, row);
			GridPane.setHalignment(btn, HPos.CENTER);
		}
	}
	
	/**
	 * Adds a row with a ComboBox as the user input to the GridPane
	 *
	 * @param row the row to update
	 * @param lbl the Label describing the intent of the TextField
	 * @param cb the actual ComboBox user input for this row
	 * @param btn if not null, the Button to take an associated action
	 */
	private void gpAddCBRow(int row, Label lbl, ComboBox<String> cb, Button btn) {
		gp.add(lbl, LABEL_COL, row);
		GridPane.setHalignment(lbl, HPos.RIGHT);
		gp.add(cb, TF_COL, row);
		if (btn != null) {
			gp.add(btn, BUTTON_COL, row);
			GridPane.setHalignment(btn, HPos.CENTER);
		}
	}

	/**
	 * Creates and initializes the GridPane
	 * - instantiates the GridPane and sets the default spacing 
	 *   between cells
	 * - adds rows to the GridPane
	 * - sets the ColumnConstraints for pleasing visual presentation
	 */
	private void initGridPane() {
		gp = new GridPane();
		gp.setVgap(15);
		gp.setHgap(2);
		gpAddCBRow(SOURCE_ROW,lblSource,sourceFile,btnGenWeights);
		gpAddTFRow(WEIGHTS_ROW,lblWeights,weightsFile,null);
		gpAddTFRow(ENCODE_ROW,lblEncode,encodeFile,btnEncode);
		gpAddTFRow(DECODE_ROW,lblDecode,decodeFile,btnDecode);
		gp.getColumnConstraints().addAll(new ColumnConstraints(LABEL_COL_WIDTH),
				                         new ColumnConstraints(),
				                         new ColumnConstraints(BUTTON_COL_WIDTH));
	}
	
	/**
	 * Instantiates and initializes an HBox with Buttons and CheckBoxes
	 * for layout purposes
	 */
	private void initHBox() {
		optionBox.setAlignment(Pos.CENTER);
		optionBox.setPrefHeight(OPTIONS_HEIGHT);
		optionBox.getChildren().addAll(btnClear,ckbSaveWeights,ckbOptimize);
		
	}
	
	/**
	 * Initializes the top, center, and bottom portions of the BorderPane pane.
	 * Forces the alignment of the hdr node to be centered.
	 */
	private void initBorderPane() {
		pane.setTop(hdr);
		BorderPane.setAlignment(hdr, Pos.CENTER);		
		pane.setCenter(gp);		
		pane.setBottom(optionBox);
	}
	
	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initLabels();
		initTextFieldsComboBoxes();
		initButtonsCheckBoxes();
		initGridPane();
		initHBox();
		initBorderPane();

		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	/**
	 * Update text fields based upon the value of the selected
	 * sourceFile. These MAY be overwritten, but usually the defaults are
	 * fine.
	 *
	 * @param nv the nv
	 */
	private void updateTextFieldDefaults(String nv ) {
		if (nv != null) {
			weightsFile.setText(nv.replaceAll(".txt", ".csv"));
			encodeFile.setText(nv.replaceAll(".txt", ".bin"));
			decodeFile.setText(nv);
		}
	}
	
	/**
	 * Clear all TextFields and ComboBoxes.
	 */
	private void clearAllFields() {
		sourceFile.setItems(null);
		weightsFile.setText(null);
		encodeFile.setText(null);
		decodeFile.setText(null);
	}
	
	/**
	 * Gets the list of files ending in ".txt" from the specified
	 * directory (in this case, dir is "data/")
	 *
	 * @param dir the directory to search in
	 * @return the Set of files that were found; not ordered
	 */
	private Set<String> getTextFiles(String dir) {
		try (Stream<Path> stream = Files.list(Paths.get(dir))) {
			return stream
					.filter(file -> !Files.isDirectory(file))
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(e -> e.endsWith(".txt"))
					.collect(Collectors.toSet());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	/**
	 * Sets the list of available text files in "data/" for the sourceFile ComboBox.
	 * This list is sorted before adding to the ComboBox for ease of use.
	 *
	 * @return the source files
	 */
	private void getSourceFiles() {
		Set<String> textFiles = getTextFiles("data");
		ObservableList<String> ov = FXCollections.observableArrayList(textFiles);
		FXCollections.sort(ov);
		sourceFile.setItems(ov);
	}
	
	/**
	 * Calls GenWeights.generateWeights with the selected sourceFile.
	 * If saveWeights is true, writes the weights to the specified file 
	 * the weights/ directory.
	 */
	private void generateWeights() {
		gw.generateWeights("data/"+sourceFile.getValue());
		if (saveWeights) 
			gw.saveWeightsToFile("weights/"+weightsFile.getText());
	}
	
	/**
	 * Encodes the specified text file using the specified weights file
	 * (if this file does not exist, encode will generate the weights
	 * from the text file) and writes the encoded data to the specified 
	 * file in the encode/ directory.
	 */
	private void executeEncode() {
		encdec.encode("data/"+sourceFile.getValue(),
				      "encode/"+encodeFile.getText(),
				      "weights/"+weightsFile.getText(),
				      optimize);
	}
	
	/**
	 * Decodes the specified encoded file using the specified weights file, 
	 * WHICH MUST EXIST. Writes the decoded data to the specified file in
	 * the decode/ directory.
	 */
	private void executeDecode() {
		encdec.decode("encode/"+encodeFile.getText(),
				      "decode/"+decodeFile.getText(),
				      "weights/"+weightsFile.getText(),
				      optimize);
	}
	
	/**
	 * Input alert - signals that an error was detected while trying to
	 *               read a file
	 *
	 * @param hdr the hdr
	 * @param errorMsg the error msg
	 */
	void inputAlert(String hdr, String errorMsg) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText(hdr);
			alert.setContentText(errorMsg);
			alert.showAndWait();
	}

	/**
	 * Output alert - signals that an error was detected while trying to
	 *                write a file
	 *
	 * @param hdr the hdr
	 * @param errorMsg the error msg
	 */
	void outputAlert(String hdr, String errorMsg) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText(hdr);
			alert.setContentText(errorMsg);
			alert.showAndWait();
	}

	/**
	 * Confirm alert - forces the user to acknowledge that an existing
	 *                 file is about to be overwritten. Provides option
	 *                 to cancel the operation.
	 *
	 * @param hdr the hdr
	 * @param msg the msg
	 * @return true if the user confirms the action; otherwise false.
	 */
	boolean confirmAlert(String hdr, String msg) {	
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText(hdr);
			alert.setContentText(msg);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent())
				return(result.get() == ButtonType.OK);
			return false;
	}

	/**
	 * Done alert - indication that the process finished successfully
	 *
	 * @param hdr the hdr
	 * @param success the success
	 */
	void doneAlert(String hdr, String success) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(hdr);
			alert.setContentText(success);
			alert.showAndWait();
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}
}
