/* PROBLEM = "Searching for a bus stop by full name or by the first few characters in the name, using a
          ternary search tree (TST), returning the full stop information for each stop matching the
          search criteria (which can be zero, one or more stops)".

Approach: Add the stop names to a TST, use this TST to search for stop names. 
          Whatever stop names match the search criteria, return their names and 
          the relevant information to do with these bus stops.
 */ 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

class TSTNode
{
	char data;
	boolean isEnd;
	TSTNode left, middle, right;

	/** Constructor **/
	public TSTNode(char data)
	{
		this.data = data;
		this.isEnd = false;
		this.left = null;
		this.middle = null;
		this.right = null;
	}        
}

/** class TernarySearchTree **/
class TernarySearchTree
{
	private TSTNode root;
	private ArrayList<String> al;

	/** Constructor **/
	public TernarySearchTree()
	{
		root = null;
	}

	public void insert(char[] word)
	{
		root = insert(root, word, 0);
	}

	/** function to insert for a word **/
	public TSTNode insert(TSTNode r, char[] word, int ptr)
	{
		if (r == null)
			r = new TSTNode(word[ptr]);

		if (word[ptr] < r.data)
			r.left = insert(r.left, word, ptr);
		else if (word[ptr] > r.data)
			r.right = insert(r.right, word, ptr);
		else
		{
			if (ptr + 1 < word.length)
				r.middle = insert(r.middle, word, ptr + 1);
			else
				r.isEnd = true;
		}
		return r;
	}

	/** function to search for a word **/
	public String search(String word)
	{
		StringBuilder sb = new StringBuilder();
		TSTNode prefixRoot = search(root, word.toCharArray(), 0);
		findAllSuggestions(prefixRoot, "", sb, word);
		if (sb.length() < 1) {
			return "No Matching String Found";
		}			
		return sb.toString();
	}
	/** function to search for a word **/
	private TSTNode search(TSTNode r, char[] word, int ptr)
	{
		if (r == null) 
			return null;
		if (word[ptr] < r.data) 
			return search(r.left, word, ptr);
		else if (word[ptr] > r.data) 
			return search(r.right, word, ptr);
		else {
			if (ptr == word.length - 1)	
				return r;
			else
				return search(r.middle, word, ptr + 1);
		}        
	}

	private void findAllSuggestions(TSTNode r, String str, StringBuilder sb, String word)

	{
		if (r != null) {
			findAllSuggestions(r.left, str, sb, word);
			str = str + r.data;
			if (r.isEnd) {
				sb.append(word + str.substring(1) + "\n");
			}
			findAllSuggestions(r.middle, str, sb, word);
			str = str.substring(0, str.length() - 1);
			findAllSuggestions(r.right, str, sb, word);
		}

	}
	/** function to print tree **/
	public String toString()
	{
		al = new ArrayList<String>();
		traverse(root, "");
		return "\nTernary Search Tree : "+ al;
	}
	/** function to traverse tree **/
	private void traverse(TSTNode r, String str)

	{
		if (r != null)
		{
			traverse(r.left, str);

			str = str + r.data;
			if (r.isEnd)
				al.add(str);

			traverse(r.middle, str);
			str = str.substring(0, str.length() - 1);

			traverse(r.right, str);
		}
	}


}

public class StopSearchPartTwo {

	public static int rowCount (String file) throws IOException {
		int size = 0;
		File busStops = new File(file);
		Scanner myReader = new Scanner(busStops);
		while (myReader.hasNextLine()) {
			myReader.nextLine();
			size++;
		}	
		return size;
	}

	public static String[][] stopDetails (String file) throws IOException {
		int rowCount = rowCount(file);
		File busStops = new File(file);
		boolean firstRow = true;
		int columnCount = 10;
		String[][] stopInfo = new String[rowCount][columnCount];
		rowCount = 0;
		Scanner myReader = new Scanner(busStops);
		while(myReader.hasNextLine()) {
			String data = myReader.nextLine();
			//the first heading row so we want to skip it
			if (firstRow) {
				firstRow=false;
			}
			//we have a data row
			else {
				String[] stopDetails = data.split(",");
				for (int i = 0; i < 9; i++ ) {
					stopInfo[rowCount][i] = stopDetails[i];
				}
				if (stopInfo[rowCount][2].substring(0,8).equals("FLAGSTOP"))
				{
					String prefix = stopInfo[rowCount][2].substring(0,11);
					stopInfo[rowCount][2] = stopInfo[rowCount][2].substring(12,stopInfo[rowCount][2].length());
					stopInfo[rowCount][2] += " " + prefix;
				}

				String temp = stopInfo[rowCount][2].substring(0,2);
				if (temp.equals("NB") || temp.equals("SB") || temp.equals("EB") || temp.equals("WB")) {
					stopInfo[rowCount][2] = stopInfo[rowCount][2].substring(3, stopInfo[rowCount][2].length());
					stopInfo[rowCount][2] += " " + temp;
				}
				rowCount++;
			}
		}
		return stopInfo;
	}

	public static void main(String[] args) throws IOException {

		String addressOfFile = "C:\\Users\\35389\\Downloads\\inputs\\stops.txt";
		int rowCount = rowCount(addressOfFile);
		String[][] stopInfo = stopDetails(addressOfFile);
		Scanner scan = new Scanner(System.in);
		TernarySearchTree tst = new TernarySearchTree(); 
		// Inputting the stop names into the TernarySearchTree
		for (int i = 0; i < rowCount; i++) {
			String temp = stopInfo[i][2];
			if(temp != null) {
				char[] current = temp.toCharArray();
				tst.insert(current);
			}
		}
		boolean bool = true;
		String searchTerm = "";
		do {
			System.out.println("Enter stop name to search");
			searchTerm = scan.nextLine().toUpperCase();
			if (tst.search(searchTerm)!= "No Matching String Found") {
				System.out.println("Search result: Success");
				// Add way of returning the stop information 
				for (int i = 0; i < stopInfo.length - 1 ; i++) {
					try {
						if (stopInfo[i][2].substring(0,searchTerm.length()).equals(searchTerm)) {
							System.out.println("The stop ID is: " + stopInfo[i][0]);
							System.out.println("The stop code is: " + stopInfo[i][1]);
							System.out.println("The stop name is: " + stopInfo[i][2]);
							System.out.println("The stop description is: " + stopInfo[i][3]);
							System.out.println("The stop latitude is: " + stopInfo[i][4]);
							System.out.println("The stop longitude is: " + stopInfo[i][5]);
							System.out.println("The stop zone ID is: " + stopInfo[i][6]);
							System.out.println("The stop url is: " + stopInfo[i][7]);
							System.out.println("The stop location type is: " + stopInfo[i][8]);
							System.out.println("The stop parent station is: " + stopInfo[i][9]);
							System.out.println("******************************************");
						}
					}catch (NullPointerException e) {
						System.out.println("NullPointer caught");
					}
				}
				bool = false;
			}
			else if (tst.search(searchTerm) == "No Matching String Found") {
				System.out.println("Search Result: Failure");
				System.out.println("Please enter a valid bus stop name.");
				bool = true;
			}
		} while (bool == true);
	}
}
