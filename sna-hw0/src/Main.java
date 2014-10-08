import java.io.File;
import java.util.List;
import java.util.Set;


public class Main {
	public static void main(String [] argv){
		UndirectedAnalyzer ua = new UndirectedAnalyzer(new File("partA_egofb.txt")); 
		System.out.println(ua.getShortestPath(0, 3));
		System.out.println(ua.getDegree(0));
		System.out.println(ua.getAlpha());
		List<Set<Integer> > components = ua.getConnectedComponents();
		//System.out.println(components.get(0).size());
		System.out.println(ua.getAverageShortestPathLength(components.get(0)));
		
		//System.out.println(components.size());
		
	}

}
