package application.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Draw {
	private List<Route> routesList = new ArrayList<>();
	private ObservableList<Route> drawnRoutes = FXCollections.observableArrayList();
	private double kmSum =0;
	private double days=0;
	private double minKm=0;
	private double maxKm=0;
	private boolean includeDelta =false;
	
	public Draw() {
		clearDraw();
	}
	
	public Draw(double days, List<Route> routesList, boolean includeDelta) {
		this.days = days;
		this.routesList = routesList;
		this.includeDelta = includeDelta;
		
		drawDays();
	}
	
	public Draw(double minKm, double maxKm, List<Route> routesList, boolean includeDelta) {
		this.minKm = minKm;
		this.maxKm = maxKm;
		this.routesList = routesList;
		this.includeDelta = includeDelta;
		
		drawKm();
	}
	
	public void drawKm() {
		Random los = new Random();
		int index=0;
		int count=0;
			
		while(kmSum <minKm) {
			
				index = los.nextInt(routesList.size());
				Route drawnRoute = routesList.get(index);
				
				if (includeDelta == true)
					drawnRoute.addDelta(delta());

				drawnRoutes.add(drawnRoute);
				kmSum += drawnRoute.getLength().getValue();
				count++;
		}
		drawnRoutes.add(new Route("Suma km:", kmSum));
		drawnRoutes.add(new Route("Liczba tras:",count));
	}
	
	private void drawDays(){
		Random los = new Random();
		int index=0;
			
		for (int i = 0; i< days; i++) {
			
				index = los.nextInt(routesList.size());
				Route drawnRoute = routesList.get(index);
				
				if (includeDelta == true)
					drawnRoute.addDelta(delta());

				drawnRoutes.add(drawnRoute);
				kmSum += drawnRoute.getLength().getValue();
		}
		drawnRoutes.add(new Route("Suma km:", kmSum));
	}
	
	private double delta() {
		Random los = new Random();
		double delta = los.nextInt(20);
		delta = (delta-10)/100+1;
		return delta;
	}
	
	private void clearDraw() {
		drawnRoutes.clear();
	}

	public List<Route> getDrawnRoutes() {
		return drawnRoutes;
	}

	public double getKmSum() {
		return kmSum;
	}
	

}
