import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class UserInterface {
    public static final Scanner input = new Scanner(System.in);

    // User interface menu - allows user to chose from avaible functions
  public static boolean userMenu() {
        System.out.print("Choose the options below or type '4' to exit the program:\n"
                + "type 1  To find the shortest path between 2 bus stops \n"
                + "type 2  To find full stop information on a stop \n"
                + "type 3  To find all trips with a specific time of arrival sorted by trip ID\n");
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
                        DijkstraShortestPath.main();
       
                    case 2:
             
                        //implement stopfind class part two
                        break;
                    case 3:
                    
                       ArrivalTime.main();
                        break;
                    default:
                        System.out.println("Menu as above");
                        return false;
                }
            } else {
                System.out.println("Menu as above");
            }
        } else {
            System.out.println("Choose between 1 and 3 or press 4 to exit");
        }
        System.out.print("Press 'Enter' key to continue");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    } 
    
    public static void main(String[] args) throws Exception {
        boolean run = false;
        do {
            run =userMenu();
        } while (run);
        System.out.println("\nSucessfully quit program");
    }
}
