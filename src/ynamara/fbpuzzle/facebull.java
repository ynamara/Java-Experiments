package ynamara.fbpuzzle;
import java.io.*;
import java.util.*;

public class facebull {
	/*
	static class Logger {
		int level = 0;	
		String spacer(int length) {
			String s = "";
			while (s.length() < length) {
				s += " ";
			}
			return s;
		}
		void log(String msg) {
			System.out.println(spacer(level) + " " + msg);
		}
		void in(String msg) {
			level++;
		}
		void out() {
			level--;
		}
	};
	Logger logger = new Logger();
	*/	
	static class Index {
		Map<String, Integer> map;
		List<String> list;
		
		Index() {
			map = new HashMap<String, Integer>();
			list = new ArrayList<String>();
		}
		int indexOf(String s) {
			Integer ii = map.get(s);
			if (ii == null) {
				map.put(s, ii = map.size());
				list.add(s);
			}
			return ii;
		}
		String at(int i) {
			return list.get(i);
		}
	}
	Index compoundsIndex = new Index();
	
	static class Machine {
		int label;
		int c1, c2;
		int w;

		Machine(int label, int c1, int c2, int w) {
			this.label = label;
			this.c1 = c1;
			this.c2 = c2;
			this.w = w;
		}
		public String toString() {
			return label + " " + c1 + " " + c2 + " " + w;
		}
	}
	List<Machine> machinesList = new ArrayList<Machine>();
	
	static Integer[] asArray(BitSet set) {
		Integer[] ret = new Integer[set.cardinality()];
		for (int i = 0, v = set.nextSetBit(0); v >= 0; v = set.nextSetBit(v + 1)) {
			ret[i++] = v;
		}
		return ret;
	}

	String toStringMachines(BitSet set) {
		BitSet labels = new BitSet();
		for (int i : asArray(set)) {
			labels.set(machinesList.get(i).label);
		}
		return labels.toString().replaceAll("[^0-9 ]", "");		
	}

	String toStringCompounds(BitSet compounds) {
		String s = "";
		for (int i: asArray(compounds)) {
			s += " " + compoundsIndex.at(i);
		}
		return s.trim();
	}
	
	int NUM_C;
	
	int record = Integer.MAX_VALUE;
	BitSet bestSet = null;
	
	BitSet[] outgoing;
	
	Comparator<Integer> outdegreeComparator = new Comparator<Integer>() {
		public int compare(Integer c1, Integer c2) {
			if (outgoing[c1].cardinality() == 0) {
				return +1;
			}
			return outgoing[c1].cardinality() - outgoing[c2].cardinality();
		}
	};

	Comparator<Integer> weightComparator = new Comparator<Integer>() {
		public int compare(Integer m1, Integer m2) {
			return machinesList.get(m1).w - machinesList.get(m2).w;
		}
	};
	
	static Integer[] sortedBy(BitSet set, Comparator<Integer> comparator) {
		Integer[] o = asArray(set);
		Arrays.sort(o, comparator);
		return o;
	}
	
	void bruteDFS(int current, BitSet loopable, BitSet reachable, BitSet machines, int totalCost, BitSet path) {
		if (totalCost > record) {
			return;
		}
		Integer[] orderedMachines = sortedBy(outgoing[current], weightComparator);
		//logger.log("At " + compoundsIndex.at(current) + ", dfs-ing!");
		//logger.log("Candidates are " + toStringMachines(outgoing[current]));
		for (int mIndex: orderedMachines) {
			if (machines.get(mIndex)) {
				//logger.log("Already using M" + machinesList.get(mIndex).label);
				continue;
			}
			Machine m = machinesList.get(mIndex);
			int next = m.c2;
			//logger.log("Trying M" + m.label + " " + toStringCompounds(reachable));
			if (loopable.get(next)) {
				//logger.log("Finishing loop to " + compoundsIndex.at(next));
				//logger.log("Path is " + toStringCompounds(path));
				//logger.log("Reachable is " + toStringCompounds(reachable));
				BitSet pathNew = (BitSet) path.clone();
				pathNew.andNot(loopable);
				pathNew.set(next);
				//logger.log("Finishing loop to " + compoundsIndex.at(next) + " " + pathNew.cardinality());
				if (pathNew.cardinality() > 0) {
					boolean newReachable = !reachable.get(next);
					if (newReachable) {
						reachable.set(next);
					}
					BitSet loopableCopy = (BitSet) loopable.clone();
					loopable.or(path);
					machines.set(mIndex);
					//logger.in(null);
					bruteAddCycle(loopable, reachable, machines, totalCost + m.w);
					//logger.out();
					machines.clear(mIndex);
					loopable.clear();
					loopable.or(loopableCopy);
					if (newReachable) {
						reachable.clear(next);
					}
				}
			} else {
				// continuing a path, must be an unreachable
				if (!reachable.get(next)) {
					reachable.set(next);
					machines.set(mIndex);
					path.set(next);
					//logger.in(null);
					bruteDFS(next, loopable, reachable, machines, totalCost + m.w, path);
					//logger.out();
					path.clear(next);
					machines.clear(mIndex);
					reachable.clear(next);
				}
			}
		}
		//logger.log("At " + compoundsIndex.at(current) + ", dfs-ing! DONE!");
	}
				
	void bruteAddCycle(BitSet loopable, BitSet reachable, BitSet machines, int totalCost) {
		if (reachable.cardinality() == NUM_C) {
			//logger.log("COMPLETE!");
			if (totalCost < record) {
				record = totalCost;
				bestSet = (BitSet) machines.clone();
			}
			return;
		}
		//logger.in(null);
		//logger.log("bruteAddCycle IN");
		//logger.log(toStringCompounds(reachable) + " reachable by " + toStringMachines(machines));
		//logger.log(toStringCompounds(loopable) + " are candidate starts");
		Integer[] cycleStart = sortedBy(loopable, outdegreeComparator);
		Queue<BitSet> copy = new LinkedList<BitSet>();
		for (int start: cycleStart) {
			//logger.log("Trying to start cycle at " + compoundsIndex.at(start));
			//logger.in(null);
			bruteDFS(start, loopable, reachable, machines, totalCost, new BitSet());
			//logger.out();
			copy.add((BitSet) outgoing[start].clone());
			outgoing[start].clear();
			//logger.log("Trying to start cycle at " + compoundsIndex.at(start) + " ends");
		}
		for (int start: cycleStart) {
			outgoing[start].or(copy.remove());
		}
		//logger.log("bruteAddCycle OUT");
		//logger.out();
	}
	
	void solve(Scanner in) {
		while (in.hasNext()) {
			int label = Integer.parseInt(in.next().substring(1));
			int c1 = compoundsIndex.indexOf(in.next());
			int c2 = compoundsIndex.indexOf(in.next());
			int w = in.nextInt();
			machinesList.add(new Machine(label, c1, c2, w));
		}
		NUM_C = compoundsIndex.map.size();
		BitSet machines = new BitSet();
		machines.set(0, machinesList.size());

		outgoing = new BitSet[NUM_C];
		for (Machine m: machinesList) {
			BitSet out = outgoing[m.c1];
			if (out == null) {
				outgoing[m.c1] = out = new BitSet();
			}
			out.set(machinesList.indexOf(m));
		}

		BitSet compounds = new BitSet();
		compounds.set(0, NUM_C);

		int startingCompound = sortedBy(compounds, outdegreeComparator)[0];

		compounds.clear();
		compounds.set(startingCompound);
		
		//logger.log("Starting at " + compoundsIndex.at(startingCompound));
				
		bruteAddCycle(compounds, new BitSet(), new BitSet(), 0);
		
		System.out.println(record);
		System.out.println(toStringMachines(bestSet));
	}

	public static void main(String[] args) throws Exception {
		new facebull().solve(new Scanner(new File(args[0])));
	}
}