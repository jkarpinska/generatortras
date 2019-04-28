package application.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Trasa {
	private StringProperty odcinek;
	private DoubleProperty dlugosc;
	private BooleanProperty czyDlugaTrasa;
	
	public Trasa() {
		this(null,0);
	}
	
	public Trasa(String odcinek, double dlugosc) {
		this.odcinek = new SimpleStringProperty(odcinek);
		this.dlugosc = new SimpleDoubleProperty(dlugosc);
	}

	public StringProperty getOdcinek() {
		return odcinek;
	}

	public void setOdcinek(StringProperty odcinek) {
		this.odcinek = odcinek;
	}

	public DoubleProperty getDlugosc() {
		return dlugosc;
	}

	public void setDlugosc(DoubleProperty dlugosc) {
		this.dlugosc = dlugosc;
	}

//	public BooleanProperty getCzyDlugaTrasa() {
//		return czyDlugaTrasa;
//	}

//	public void setCzyDlugaTrasa(BooleanProperty czyDlugaTrasa) {
//		this.czyDlugaTrasa = czyDlugaTrasa;
//	}

	public void dodajDelte (double delta) {

		double newValue = Math.round(this.dlugosc.getValue()*delta);
		this.dlugosc.setValue(newValue);
	}
	
	public String printTrasa() {
		String line = this.odcinek.getValue() + "," + this.dlugosc.getValue();
		return line;
	}
	
}
