import java.util.ArrayList;


public class PowerSetBuilder {

	private ArrayList<Integer> set = new ArrayList<Integer>();
	
	public PowerSetBuilder(int countKnots) {
		for(int i = 0; i < countKnots; i++)
			this.set.add(i+1);
	}
	
	public ArrayList<Integer> getSet() {
		return new ArrayList<Integer>(this.set.subList(1, this.set.size()));
	}
	
	 /** Erzeugt die Potenzmenge (alle möglichen Kombinationen )aus einer gegebenen Menge
	 * @param set Menge aller zu durchlaufender Knoten
	 * @return Potenzmenge
	 */
	public static ArrayList<ArrayList<Integer>> buildPowerSet(ArrayList<Integer> set) {
		ArrayList<ArrayList<Integer>> powerSet = new ArrayList<ArrayList<Integer>>();
		powerSet.add(new ArrayList<Integer>());

		for (Integer item : set) {
			ArrayList<ArrayList<Integer>> newPowerSet = new ArrayList<ArrayList<Integer>>();
		 
			for (ArrayList<Integer> subSet : powerSet) {
				newPowerSet.add(subSet);

				ArrayList<Integer> newSubset = new ArrayList<Integer>(subSet);
				newSubset.add(item);
				newPowerSet.add(newSubset);
			}
			powerSet = newPowerSet;
		}
	return powerSet;
	}
	
	 /** Generiert aus einer Potenzmenge Teilmengen mit bestimmter Mächtigkeit 
	 * @param powerSet Potenzmenge
	 * @param iCard Kardinalität der zu erzeugenden Teilmenge
	 * @return Teilmenge(n) mit identischer Mächtigkeit
	 */
	public static ArrayList<ArrayList<Integer>> getSubSets(ArrayList<ArrayList<Integer>> powerSet, int iCard) {
		ArrayList<ArrayList<Integer>> subSets = new ArrayList<ArrayList<Integer>>();
		for(ArrayList<Integer> subSet : powerSet)
			if(subSet.size() == iCard)
				subSets.add(subSet);
		return subSets;
	}
	
	
}
