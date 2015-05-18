import java.util.ArrayList;

public class PermutationBuilder {

	ArrayList<ArrayList<ArrayList<Integer>>> subPermuts = new ArrayList<ArrayList<ArrayList<Integer>>>();
	ArrayList<Integer> newSub = new ArrayList<Integer>();

	public PermutationBuilder() {
		
		subPermuts.add(new ArrayList<ArrayList<Integer>>());
		subPermuts.add(new ArrayList<ArrayList<Integer>>());
		subPermuts.get(0).add(new ArrayList<Integer>());
		subPermuts.get(0).get(0).add(1);
	}
	
	public ArrayList<ArrayList<Integer>> BuildList(int size, boolean sym) {
		
		for (int n=2; n<=size; n++) {
			for (ArrayList<Integer> sub : subPermuts.get(0)) {
				for (int i=0; i<n; i++) {
					
					newSub = new ArrayList<Integer>();
					newSub.addAll(sub);
					newSub.add(i,n);
					subPermuts.get(1).add(newSub); 
					if(sym && (n==2)) break; //Linksseitiger Permutationsbaum
				}
			}
			subPermuts.remove(0);
			subPermuts.add(new ArrayList<ArrayList<Integer>>());
		}
		return subPermuts.get(0);
	}
}
