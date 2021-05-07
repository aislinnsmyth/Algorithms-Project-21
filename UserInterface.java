
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class UserInterface {
    public static final Scanner input = new Scanner(System.in);

    // User interface menu - allows user to chose from avaible functions
  public static boolean userMenu() throws IOException {
	  boolean bool = true;
	  while(bool = true) {
        System.out.print("Choose the options below or type '4' to exit the program:\n"
                + "Type 1  To find the shortest path between 2 bus stops \n"
                + "Type 2  To find full stop information on a stop \n"
                + "Type 3  To find all trips with a specific time of arrival sorted by trip ID\n");
        int inputValue= input.nextInt();
        boolean acceptInt = false;
        int n=0;
        try {
            n=inputValue;
            acceptInt = true;
        } catch (NumberFormatException e) {
        }
        if (inputValue==4) {
            return false;
        } else if (acceptInt) {
            if (inputValue >=1 && inputValue <=3) {
                switch (inputValue) {
                    case 1:
                        DijkstraShortestPath.main(null);
                        break;
                    case 2:
                        StopSearchPartTwo.main(null);
                        break;
                    case 3:
						ArrivalTime.main(null);
						break;
                }
            } else {
                System.out.println("Menu as above");
            }
        } else {
            System.out.println("Choose between 1 and 3 or press 4 to exit");
        }
        System.out.print("\n Press 'Enter' key to continue\n");
        bool = false;
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
	return bool; 
  }
    
    public static void main(String[] args) throws Exception {
        boolean run = false;
        do {
            run =userMenu();
        } while (run);
        System.out.println("\nSucessfully quit program");
    }
}