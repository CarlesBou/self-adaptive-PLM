import java.io.FileWriter;
import java.util.Locale;

public class AlgoTest {

	public static void main(String[] args) throws Exception {
		//String[] problems = {"ZDT1", "ZDT2", "ZDT3", "ZDT4", "ZDT6", 
		//					"DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "DTLZ5", "DTLZ6", "DTLZ7"};
		//String[] problems = {"DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "DTLZ5", "DTLZ6", "DTLZ7"};
		//String[] problems = {"ZDT1", "ZDT2", "ZDT3", "ZDT4", "ZDT6"};
		//String[] problems = {"ZDT1"};
		//String[] problems = {"Schaffer", "Tanaka", "Srinivas", "Kursawe"};
		//String[] problems = {"WFG1", "WFG2", "WFG3", "WFG4", "WFG5", "WFG6", "WFG7", "WFG8", "WFG9"};
		String[] problems = {"ZDT1", "ZDT2", "ZDT3", "ZDT4", "ZDT6", 
								"DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "DTLZ5", "DTLZ6", "DTLZ7", 
								"WFG1", "WFG2", "WFG3", "WFG4", "WFG5", "WFG6", "WFG7", "WFG8", "WFG9",
								"Schaffer", "Tanaka", "Srinivas", "Kursawe"};
		//String[] problems = {"DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "DTLZ5", "DTLZ6", "DTLZ7", 
		//						"WFG1", "WFG2", "WFG3", "WFG4", "WFG5", "WFG6", "WFG7", "WFG8", "WFG9",
		//						"Schaffer", "Tanaka", "Srinivas", "Kursawe"};
		//String[] problems = {"ZDT1"};
		String results;

		Locale loc = Locale.ENGLISH;
		AlgoResults ar;
		
		try(FileWriter os = new FileWriter("results.csv", false)) {
			os.write("Problema;Algoritmo;bhv;hv;gd;igd;igdp;bspread;spread\n");
			for (String problem : problems) {
				for (int runs = 0; runs < 30; runs++) {
					System.out.printf("  MyNSGAII over " + problem + " ... ");
					MyNSGAIIRunner myNSGAIIRunner = new MyNSGAIIRunner(problem);
					results = myNSGAIIRunner.getResults();
					System.out.printf("OK - %s\n", results);
					ar = myNSGAIIRunner.ar;
					os.write(String.format(loc, "%s;MyNSGAII;%.10f;%.10f;%.10f;%.10f;%.10f;%.10f;%.10f\n", problem, ar.baseHypervolume,
							ar.hypervolume, ar.gd, ar.igd, ar.igdPlus, 0.0, ar.spread));

					System.out.printf("OrigNSGAII over " + problem + " ... ");
					OrigNSGAIIRunner origNSGAIIRunner = new OrigNSGAIIRunner(problem);
					results = origNSGAIIRunner.getResults();
					System.out.printf("OK - %s\n", results);
					ar = origNSGAIIRunner.ar;
					os.write(String.format(loc, "%s;NSGAII;%.10f;%.10f;%.10f;%.10f;%.10f;%.10f;%.10f\n", problem,ar.baseHypervolume,
							ar.hypervolume, ar.gd, ar.igd, ar.igdPlus, 0.0, ar.spread));
				}
			}	
		}
	}
}
