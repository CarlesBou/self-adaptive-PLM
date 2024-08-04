
public class AlgoResults {
	double hypervolume;
	double baseHypervolume;
	double gd;
	double igd;
	double igdPlus;
	double spread;
	
	AlgoResults(double basehv, double hv, double gd, double igd, double igdp, double spread) {
		this.baseHypervolume = basehv;
		this.hypervolume = hv;
		this.gd = gd;
		this.igd = igd;
		this.igdPlus = igdp;
		this.spread = spread;		
	}

	public double getHypervolume() {
		return hypervolume;
	}

	public double getBaseHypervolume() {
		return baseHypervolume;
	}

	public double getGd() {
		return gd;
	}

	public double getIgd() {
		return igd;
	}

	public double getIgdPlus() {
		return igdPlus;
	}

	public double getSpread() {
		return spread;
	}
}
