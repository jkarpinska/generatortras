package application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Losowanie {
	private List<Trasa> listaTras = new ArrayList<>();
	private ObservableList<Trasa> wylosowaneTrasy = FXCollections.observableArrayList();
	private double sumaKm=0;
	private double liczbaDni=0;
	private double minKm=0;
	private double maxKm=0;
	private boolean czyDelta=false;
	
	public Losowanie() {
		wyczyscLosowanie();
	}
	
	public Losowanie(double liczbaDni, List<Trasa> listaTras, boolean czyDelta) {
		this.liczbaDni = liczbaDni;
		this.listaTras = listaTras;
		this.czyDelta = czyDelta;
		
		losujWgDni();
	}
	
	public Losowanie(double minKm, double maxKm, List<Trasa> listaTras, boolean czyDelta) {
		this.minKm = minKm;
		this.maxKm = maxKm;
		this.listaTras = listaTras;
		this.czyDelta = czyDelta;
		
		losujWgKm();
	}
	
	public void losujWgKm() {
		Random los = new Random();
		int index=0;
		int count=0;
			
		while(sumaKm<minKm) {
			
				index = los.nextInt(listaTras.size());
				Trasa wylosowanyOdcinek = listaTras.get(index);
				
				if (czyDelta == true)
					wylosowanyOdcinek.dodajDelte(delta());

				wylosowaneTrasy.add(wylosowanyOdcinek);
				sumaKm += wylosowanyOdcinek.getDlugosc().getValue();
				count++;
		}
		wylosowaneTrasy.add(new Trasa("Suma km:",sumaKm));
		wylosowaneTrasy.add(new Trasa("Liczba tras:",count));
	}
	
	private void losujWgDni(){
		Random los = new Random();
		int index=0;
			
		for (int i=0; i<liczbaDni; i++) {
			
				index = los.nextInt(listaTras.size());
				Trasa wylosowanyOdcinek = listaTras.get(index);
				
				if (czyDelta == true)
					wylosowanyOdcinek.dodajDelte(delta());

				wylosowaneTrasy.add(wylosowanyOdcinek);
				sumaKm += wylosowanyOdcinek.getDlugosc().getValue();
		}
		wylosowaneTrasy.add(new Trasa("Suma km:",sumaKm));
	}
	
	private double delta() {
		Random los = new Random();
		double delta = los.nextInt(10);
		delta = (delta-5)/100+1;
		return delta;
	}
	
	private void wyczyscLosowanie() {
		wylosowaneTrasy.clear();
	}

	public List<Trasa> getWylosowaneTrasy() {
		return wylosowaneTrasy;
	}

	public double getSumaKm() {
		return sumaKm;
	}
	

}
