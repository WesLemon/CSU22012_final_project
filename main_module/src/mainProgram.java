import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

public class mainProgram {

    public static final String STOPS_FILE = "stops.txt";
    public static final String STOP_TIMES_FILE = "stop_times.txt";
    public static final String TRANSFERS_FILE = "transfers.txt";

    public static void main(String[] args) {

        // Initialising files and data structures.
        System.out.print("Initialising.");
        tst<String> stopNames = new tst<>();
        System.out.print(".");
        dijsktra allStops = new dijsktra(STOPS_FILE, TRANSFERS_FILE, STOP_TIMES_FILE);
        System.out.print(".");
        populateTST(stopNames);
        System.out.println(".\nDone.");

        boolean quit = false;
        while (!quit) {

            System.out.println("\nPlease select one of the following options (Enter the respective number):\n" +
                    "1. Find the shortest path between two bus stops.\n" +
                    "2. Search for a bus stop by name.\n" +
                    "3. Search for all trips at a given arrival time.\n" +
                    "4. Exit the program.");

            Scanner inputScanner = new Scanner(System.in);
            while (!inputScanner.hasNextInt()) {
                System.out.println("Error: Please enter a valid number.");
                inputScanner.nextLine();
            }
            int userInput = inputScanner.nextInt();

            switch (userInput) {
                case 1:
                    System.out.println("Enter the starting bus stop: ");
                    while (!inputScanner.hasNextInt()) {
                        System.out.println("Error: Please enter a valid number.");
                        inputScanner.nextLine();
                    }
                    int stop1 = inputScanner.nextInt();
                    System.out.println("Enter the destination bus stop: ");
                    while (!inputScanner.hasNextInt()) {
                        System.out.println("Error: Please enter a valid number.");
                        inputScanner.nextLine();
                    }
                    int stop2 = inputScanner.nextInt();
                    if (stop1 >= 0 && stop1 <= dijsktra.numStops - 1 && stop2 >= 0 && stop2 <= dijsktra.numStops - 1) {
                        int dist = allStops.shortestPath(stop1, stop2);
                        if (dist == Integer.MAX_VALUE) {
                            System.out.println("There is no path between the stops you entered.");
                        } else {
                            System.out.println("The distance between these stops is: " + dist);
                        }
                    } else {
                        System.out.println("Invalid stop number entered. Try again.");
                    }
                    break;
                case 2:
                    System.out.println("Enter the full name or the first few characters of the stop you would like " +
                            "to search for. (ALL CAPS OR NO RESULTS WILL BE FOUND)");
                    String userSearch = inputScanner.next();
                    StdOut.println("Stop names containing your search term: ");
                    for (String s : stopNames.keysWithPrefix(userSearch))
                        StdOut.println(s);
                    break;
                case 3:
                    System.out.println("Enter the time you would like to search for in the format HH:MM:SS.");
                    String userTime = inputScanner.next();
                    timeSearch(userTime);
                    break;
                case 4:
                    quit = true;
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("You did not enter a valid number.");
                    break;
            }
        }

    }

    public static void populateTST(tst<String> TST) {
        try {
            Scanner inFile = new Scanner(new File(STOPS_FILE));
            inFile.useDelimiter(",");
            if (inFile.hasNextLine()) {
                inFile.nextLine();
            }
            int index = 0;
            while (inFile.hasNextLine()) {
                inFile.next();
                inFile.next();
                String stopName = inFile.next();
                if (stopName.startsWith("FLAGSTOP ")) {
                    stopName = stopName.substring(9) + stopName.substring(0, 9);
                }
                if (stopName.startsWith("WB ") || stopName.startsWith("NB ")
                        || stopName.startsWith("SB ") || stopName.startsWith("EB ")) {
                    stopName = stopName.substring(3) + stopName.substring(0, 3);
                }
                TST.put(stopName, String.valueOf(index));
                index++;
                inFile.nextLine();
            }
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void timeSearch(String time) {

        System.out.println("Searching..");
        ArrayList<String> stopTimes = new ArrayList<>();
        try {
            Scanner inFile = new Scanner(new File(STOP_TIMES_FILE));
            while (inFile.hasNextLine()) {
                String tempLine = inFile.nextLine();
                Scanner lineScanner = new Scanner(tempLine).useDelimiter(",");
                if (lineScanner.hasNext()) {
                    lineScanner.next();
                    String tempTime = lineScanner.next();
                    if(tempTime.startsWith("24") || tempTime.startsWith("25") || tempTime.startsWith("26")
                        || tempTime.startsWith("27") || tempTime.startsWith("28") || tempTime.startsWith("29")) {
                        System.out.println("Invalid time entered.");
                        return;
                    }
                    if (tempTime.startsWith(" ")) {
                        tempTime = tempTime.substring(1);
                    }
                    if (Objects.equals(tempTime, time)) {
                        stopTimes.add(tempLine);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Collections.sort(stopTimes);

        System.out.println("Results:");
        for (String stopTime : stopTimes) {
            System.out.println(stopTime);
        }
        if (stopTimes.isEmpty()) {
            System.out.println("No results found.");
        }
    }
}