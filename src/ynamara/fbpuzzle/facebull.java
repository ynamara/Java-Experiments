package ynamara.fbpuzzle;

import java.io.*;
import java.util.*;

public class facebull {
	
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

	static class Index {
		Map<String, Integer> map;
		List<String> arr;
		
		Index() {
			map = new HashMap<String, Integer>();
			arr = new ArrayList<String>();
		}
		int indexOf(String s) {
			Integer i = map.get(s);
			if (i == null) {
				map.put(s, i = new Integer(map.size()));
				arr.add(s);
			}
			return i.intValue();
		}
		String at(int i) {
			return arr.get(i);
		}
	}
	Index cIdx = new Index();
	
	static class Machine {
		int label;
		int c1, c2;
		int w;
		int index;

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
	
	class BitSetMachineIterator implements Iterator<Machine> {
		int i = -1;
		BitSet set;
		
		BitSetMachineIterator(BitSet set) {
			this.set = set;
		}
		public boolean hasNext() {
			return (i < set.length() - 1);
		}
		public Machine next() {
			return machinesList.get(i = set.nextSetBit(i + 1));
		}
		public final void remove() {
		}
	}
	
	static Integer[] iter(BitSet set) {
		Integer[] ret = new Integer[set.cardinality()];
		for (int i = 0, v = set.nextSetBit(0); v >= 0; v = set.nextSetBit(v + 1)) {
			ret[i++] = v;
		}
		return ret;
	}
	
	class IterableBitSet extends BitSet implements Iterable<Machine> {
		public Iterator<Machine> iterator() {
			return new BitSetMachineIterator(this);
		}
	}
		
	String toString(IterableBitSet machines) {
		TreeSet<Integer> sorted = new TreeSet<Integer>();
		for (Machine m: machines) {
			sorted.add(m.label);
		}
		String labels = "";
		for (int i: sorted) {
			labels += " " + i;
		}
		return labels.trim();
	}
	
	int V;
	
	int totalCost(IterableBitSet machines) {
		int t = 0;
		for (Machine m: machines) {
			t += m.w;
		}
		return t;
	}
	
	int record = Integer.MAX_VALUE;
	IterableBitSet bestSet = null;
	
	IterableBitSet[] outgoing;
	
	Comparator<Integer> outdegreeComparator = new Comparator<Integer>() {
		public int compare(Integer v1, Integer v2) {
			if (outgoing[v1].cardinality() == 0) {
				return +1;
			}
			return outgoing[v1].cardinality() - outgoing[v2].cardinality();
		}
	};

	Comparator<Integer> weightComparator = new Comparator<Integer>() {
		public int compare(Integer m1, Integer m2) {
			return machinesList.get(m1).w - machinesList.get(m2).w;
		}
	};
	
	Integer[] sortedByOutdegree(BitSet vertices) {
		Integer[] o = iter(vertices);
		Arrays.sort(o, outdegreeComparator);
		return o;
	}
	
	Integer[] sortedByWeight(BitSet machines) {
		Integer[] o = iter(machines);
		Arrays.sort(o, weightComparator);
		return o;
	}
	
	void bruteDFS(int current, BitSet loopable, BitSet reachable, IterableBitSet machines, int totalCost, BitSet path) {
		if (totalCost > record) {
			return;
		}
		Integer[] orderedMachines = sortedByWeight(outgoing[current]);
		//logger.log("At " + cIdx.at(current) + ", dfs-ing!");
		//logger.log("Candidates are " + toString(outgoing[current]));
		for (int mIndex: orderedMachines) {
			if (machines.get(mIndex)) {
				//logger.log("Already using M" + machinesList.get(mIndex).label);
				continue;
			}
			Machine m = machinesList.get(mIndex);
			int next = m.c2;
			//logger.log("Trying M" + m.label + " " + toStringVertices(reachable));
			if (loopable.get(next)) {
				//logger.log("Finishing loop to " + cIdx.at(next));
				//logger.log("Path is " + toStringVertices(path));
				//logger.log("Reachable is " + toStringVertices(reachable));
				// finishing a loop
				BitSet pathNew = (BitSet) path.clone();
				pathNew.andNot(loopable);
				pathNew.set(next);
				//logger.log("Finishing loop to " + cIdx.at(next) + " " + pathNew.cardinality());
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
		//logger.log("At " + cIdx.at(current) + ", dfs-ing! DONE!");
	}
	
	String toStringVertices(BitSet v) {
		String s = "";
		for (int i: iter(v)) {
			s += " " + cIdx.at(i);
		}
		return s.trim();
	}
			
	void bruteAddCycle(BitSet loopable, BitSet reachable, IterableBitSet machines, int totalCost) {
		if (reachable.cardinality() == V) {
			//logger.log("COMPLETE!");
			if (totalCost < record) {
				record = totalCost;
				bestSet = (IterableBitSet) machines.clone();
			}
			return;
		}
		//logger.in(null);
		//logger.log("bruteAddCycle IN");
		//logger.log(toStringVertices(reachable) + " reachable by " + toString(machines));
		//logger.log(toStringVertices(loopable) + " are candidate starts");
		Integer[] cycleStart = sortedByOutdegree(loopable);
		Queue<IterableBitSet> copy = new LinkedList<IterableBitSet>();
		for (int start: cycleStart) {
			//logger.log("Trying to start cycle at " + cIdx.at(start));
			//logger.in(null);
			bruteDFS(start, loopable, reachable, machines, totalCost, new BitSet());
			//logger.out();
			copy.add((IterableBitSet) outgoing[start].clone());
			outgoing[start].clear();
			//logger.log("Trying to start cycle at " + cIdx.at(start) + " ends");
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
			int c1 = cIdx.indexOf(in.next());
			int c2 = cIdx.indexOf(in.next());
			int w = in.nextInt();
			machinesList.add(new Machine(label, c1, c2, w));
		}
		V = cIdx.map.size();
		IterableBitSet machines = new IterableBitSet();
		for (int i = 0; i < machinesList.size(); i++) {
			machinesList.get(i).index = i;
			machines.set(i);
		}
				
		outgoing = new IterableBitSet[V];
		for (Machine m: machines) {
			IterableBitSet out = outgoing[m.c1];
			if (out == null) {
				outgoing[m.c1] = out = new IterableBitSet();
			}
			out.set(m.index);
		}

		BitSet vertices = new BitSet();
		vertices.set(0, V);
		
		int startingVertex = sortedByOutdegree(vertices)[0];
		vertices.clear();
		vertices.set(startingVertex);
		
		//logger.log("Starting at " + cIdx.at(startingVertex));
				
		bruteAddCycle(vertices, new BitSet(), new IterableBitSet(), 0);
		
		System.out.println(record);
		System.out.println(toString(bestSet));
	}

	public static void main(String[] args) throws Exception {
		new facebull().solve(new Scanner(new File(args[0])));
	}
}