package application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Route {
	private StringProperty name;
	private DoubleProperty length;
	
	public Route() {
		this(null,0);
	}
	
	public Route(String name, double length) {
		this.name = new SimpleStringProperty(name);
		this.length = new SimpleDoubleProperty(length);
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}

	public DoubleProperty getLength() {
		return length;
	}

	public void setLength(DoubleProperty length) {
		this.length = length;
	}


	public void addDelta(double delta) {
		double newValue = Math.round(this.length.getValue()*delta);
		this.length.setValue(newValue);
	}
	
	public String printRoute() {
		String line = this.name.getValue() + "," + this.length.getValue();
		return line;
	}
	
}
