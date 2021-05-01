/* PROBLEM = "Searching for a bus stop by full name or by the first few characters in the name, using a
          ternary search tree (TST), returning the full stop information for each stop matching the
          search criteria (which can be zero, one or more stops)".

2). Add the data to a TST

3). Search for 0, 1 or more bus stops by full name of first few characters in the name, using a TST.

4). Return the full information of all the stops that match the given search criteria. 

Approach: Add the stop names to a TST, use this TST to search for stop names. 
          Whatever stop names match the search criteria, return their names and 
          the relevant information to do with these bus stops.
 */ 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    /** function to check if empty **/
    public boolean isEmpty()
    {
        return root == null;
    }
    /** function to clear **/
    public void makeEmpty()
    {
        root = null;
    }
    /** function to insert for a word **/
    public void insert(String word)
    {
        root = insert(root, word.toCharArray(), 0);
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
    /** function to delete a word **/
    public void delete(String word)
    {
        delete(root, word.toCharArray(), 0);
    }
    /** function to delete a word **/
    private void delete(TSTNode r, char[] word, int ptr)
    {
        if (r == null)
            return;
 
        if (word[ptr] < r.data)
            delete(r.left, word, ptr);
        else if (word[ptr] > r.data)
            delete(r.right, word, ptr);
        else
        {
            /** to delete a word just make isEnd false **/
            if (r.isEnd && ptr == word.length - 1)
                r.isEnd = false;
 
            else if (ptr + 1 < word.length)
                delete(r.middle, word, ptr + 1);
        }        
    }
 
    /** function to search for a word **/
    public boolean search(String word)
    {
        return search(root, word.toCharArray(), 0);
    }
    /** function to search for a word **/
    private boolean search(TSTNode r, char[] word, int ptr)
    {
        if (r == null)
            return false;
 
        if (word[ptr] < r.data)
            return search(r.left, word, ptr);
        else if (word[ptr] > r.data)
            return search(r.right, word, ptr);
        else
        {
            if (r.isEnd && ptr == word.length - 1)
                return true;
            else if (ptr == word.length - 1)
                return false;
            else
                return search(r.middle, word, ptr + 1);
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
	
	StopSearchPartTwo tst = new StopSearchPartTwo();
	
	public static int stop_id;

	public static void main(String[] args) throws IOException {
		String addressOfFile = "C:\\Users\\35389\\Downloads\\inputs\\stops.txt";
		File busStops = new File(addressOfFile);
		Scanner myReader = new Scanner(busStops);

		//Find out the number of rows we need to store in the array
		int rowCount = 0;
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			rowCount++;
			String[] stopDetails = data.split(",");
		}
		//Create the multidimensional array based on the number of rows we found and the fact that there are 10 data elements
		String[][] stopInfo = new String[rowCount][10];
		myReader.close();

		//Loop through the rows again storing the data in the new 2 dimensional array
		rowCount=0;
		boolean firstRow=true;
		Scanner myReader2 = new Scanner(busStops);
		while (myReader2.hasNextLine()) {
			String data = myReader2.nextLine();
			//the first heading row so we want to skip it
			if (firstRow) 
			{
				firstRow=false;
			}
			//we have a data row
			else {
				String[] stopDetails = data.split(",");
				for(int i = 0 ; i < 9 ; i++)
				{
					stopInfo[rowCount][i] = stopDetails[i];
				}

				// if name starts with FLAGSTOP then fix it up
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
		myReader2.close();
		for(int i = 0; i < stopInfo.length-1; i++) {
			for(int j = 0; j < 10; j++) {
			//System.out.println(stopInfo[i][j]);
			}
			//System.out.println("--------------------------------------");
		}
		System.out.println("");
		
		Scanner scan = new Scanner(System.in);
		 
        /* Creating object of TernarySearchTree */
        TernarySearchTree tst = new TernarySearchTree(); 
        System.out.println("Ternary Search Tree Test\n"); 
 
        char ch;
        /*  Perform tree operations  */
        do    
        {
            System.out.println("\nTernary Search Tree Operations\n");
            System.out.println("1. insert word");
            System.out.println("2. search word");
            System.out.println("3. delete word");
            System.out.println("4. check empty");
            System.out.println("5. make empty");
 
            int choice = scan.nextInt();            
            switch (choice)
            {
            case 1 : 
                System.out.println("Enter word to insert");
                tst.insert( scan.next() );                     
                break;                          
            case 2 : 
                System.out.println("Enter word to search");
                System.out.println("Search result : "+ tst.search( scan.next() ));
                break; 
            case 3 : 
                System.out.println("Enter word to delete");
                tst.delete( scan.next() );                     
                break; 
            case 4 : 
                System.out.println("Empty Status : "+ tst.isEmpty() );                
                break;    
            case 5 : 
                System.out.println("Ternary Search Tree cleared"); 
                tst.makeEmpty();               
                break;                                        
            default : 
                System.out.println("Wrong Entry \n ");
                break;   
            }
            System.out.println(tst);
 
            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);                        
        } while (ch == 'Y'|| ch == 'y');        
	}

	public static String[] readBusStops (File filename) throws IOException {
		BufferedReader buffer = new BufferedReader(new FileReader(filename));
		String st;
		String[] line ;
		while ((st = buffer.readLine()) != null) {
			line = st.split(",");
			System.out.println(line[0]);
		}
		buffer.close();
		return null;
	}
}
