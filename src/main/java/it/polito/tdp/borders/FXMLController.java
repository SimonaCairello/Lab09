package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtAnno;

	@FXML
	private ComboBox<Country> selectState;

	@FXML
	private Button btnVicini;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaConfini(ActionEvent event) {
		txtResult.clear();
		int anno;

		try {
			anno = Integer.parseInt(txtAnno.getText());
		} catch (NumberFormatException e) {
			txtResult.appendText("Inserire un numero intero!\n");
			return;
		}

		if (anno > 2016 || anno < 1816) {
			txtResult.appendText("Inserire un anno compreso tra il 1816 e il 2016\n");
			return;
		}

		model.generateGraph(anno);
		Set<Country> stati = model.getVertici();

		for (Country c : stati)
			txtResult.appendText(c.getName() + " " + model.getGrado(c) + "\n");

		txtResult.appendText(model.getComponenteConnessa() + "\n");
		
		selectState.getItems().setAll(stati);
	}

	@FXML
	void doVicini(ActionEvent event) {
		txtResult.clear();
		Country c = selectState.getValue();
		if(c==null) {
			txtResult.appendText("Scegli uno stato!\n");
			return;
		}
			
		List<Country> vicini = model.getVicini(c);
		
		for(Country co : vicini)
			txtResult.appendText(co.getName()+"\n");
	}

	@FXML
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert selectState != null : "fx:id=\"selectState\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnVicini != null : "fx:id=\"btnVicini\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
}
