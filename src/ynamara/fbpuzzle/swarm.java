package ynamara.fbpuzzle;

import java.io.*;
import java.util.*;

public class swarm {
	static final int MAX_T = 1000;
	static final int MAX_Z = 1000;
	
	void solve(Scanner in) {
		int[] W = new int[MAX_T];
		int[] maxV = new int[MAX_Z + 1];

		ArrayList<Integer>[] pred = (ArrayList<Integer>[]) new ArrayList[MAX_Z + 1];
		for (int i = 0; i < pred.length; i++) {
			pred[i] = new ArrayList<Integer>();
		}
		
		for (int P = in.nextInt(); P --> 0;) {
			int T = in.nextInt();
			int Z = in.nextInt();
			for (int t = 0; t < T; t++) {
				int s = in.nextInt();
				int m = in.nextInt();
				W[t] = s * 3;
				for (int w = Z; w - W[t] >= 0; w--) {
					int v2 = maxV[w - W[t]] + m;
					if (v2 > maxV[w]) {
						maxV[w] = v2;
						pred[w].clear();
						pred[w].addAll(pred[w - W[t]]);
						pred[w].add(t);
					}
				}
			}
			while (Z > 0 && maxV[Z] == maxV[Z - 1]) {
				Z--;
			}
			System.out.println(Z + " " + maxV[Z]);
			String out = "";
			for (int t: pred[Z]) {
				out += t + " " + W[t] + " ";
			}
			System.out.println(out.trim());
			
			Arrays.fill(maxV, 0);			
			for (ArrayList<Integer> list: pred) {
				list.clear();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		new swarm().solve(new Scanner(new File(args[0])));
	}
}