import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;


public class UndirectedAnalyzer {
	UndirectedSparseGraph<Integer, Integer> graph;
	DijkstraShortestPath<Integer,Integer> alg;
	public UndirectedAnalyzer(File src){
		graph = new UndirectedSparseGraph<Integer, Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(src));
			String line = reader.readLine();
			int edgeId = 0;
			while((line = reader.readLine()) != null){
				String [] nodes = line.split("\\s+");
				int nodeSrc = Integer.parseInt(nodes[0]);
				int nodeDest = Integer.parseInt(nodes[1]);
				if(!graph.containsVertex(nodeSrc))graph.addVertex(nodeSrc);
				if(!graph.containsVertex(nodeDest))graph.addVertex(nodeDest);
				graph.addEdge(edgeId++, nodeSrc, nodeDest);				
				
			}
			System.out.println(graph.getEdgeCount());
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getShortestPath(int nodeSrc, int nodeDest){
		if (alg == null) {
			alg = new DijkstraShortestPath<Integer, Integer>(graph);
		}
		return alg.getDistance(nodeSrc, nodeDest).intValue();
		//return alg.getPath(nodeSrc, nodeDest).size();
		
	}
	
	public int getDegree(int node){
		return graph.getNeighborCount(node);
		
	}
	
	public double getAlpha(){
		int n = graph.getVertexCount();
		double xMin = 1.0;
		double denominator = 0.0;
		for (int index = 0;index < n;index++){
			denominator += Math.log(getDegree(index)/xMin);
		}
		return 1 + n/denominator;		
		
	}
	
	private void recursiveAddNeighbor(Set <Integer> remainingVertices, Set <Integer> set, Integer startVertice){
		if (remainingVertices.size() == 0 )return;
		set.add(startVertice);
		remainingVertices.remove(startVertice);
		for (Integer neighbor : graph.getNeighbors(startVertice)){
			if (!set.contains(neighbor)){
				set.add(neighbor);
				remainingVertices.remove(neighbor);
				recursiveAddNeighbor(remainingVertices, set, neighbor);
			}
			
		}
		
	}
	
	public List<Set<Integer>> getConnectedComponents(){
		List <Set<Integer>> connectedComponents = new ArrayList<Set<Integer>>();
		Set <Integer> remainingVertices = new HashSet<Integer>();
		for (Integer vertice : graph.getVertices()){
			remainingVertices.add(vertice);
		}
		while (remainingVertices.size() > 0){
			Integer startVertice = remainingVertices.iterator().next();
			Set <Integer> set = new HashSet<Integer>();
			recursiveAddNeighbor(remainingVertices, set, startVertice);
			connectedComponents.add(set);
		}
		
		return connectedComponents;
		
	}
	
	public double getAverageShortestPathLength(Set<Integer> connectedComponent){
		if (alg == null) {
			alg = new DijkstraShortestPath<Integer, Integer>(graph);
		}
		List <Integer> ccl = new ArrayList<Integer>(connectedComponent.size());
		for (Integer vertice : connectedComponent){
			ccl.add(vertice);
		}
		int reachable = 0;
		int sumOfShortestPathLength = 0;
		int unreachable = 0;
		for (int i = 0;i < ccl.size();i++){
			for (int j = i+1;j < ccl.size();j++){
				Number distance = alg.getDistance(ccl.get(i), ccl.get(j));
				System.out.println(i + " " + j);
				if (distance == null){
					unreachable++;
				} else {
					reachable++;
					sumOfShortestPathLength += distance.intValue();
				}
			}
		}
		System.out.println("reachable:"+reachable);
		System.out.println("unreachable:"+unreachable);
		return 1.0*sumOfShortestPathLength/reachable;
		
	}
	

}
