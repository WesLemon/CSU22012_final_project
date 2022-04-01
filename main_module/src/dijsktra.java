import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class dijsktra {

    private final int[][] adjacencyMatrix;
    private static final ArrayList<String> trips = new ArrayList<>();
    private int maxStops = 0;
    private int numTimes = 0;

    dijsktra(String stopsFile, String transfersFile, String stopTimesFile) {

        Scanner inFile;
        Scanner lineScanner;

        // Get number of stops (vertices)
        try {
            inFile = new Scanner(new File(stopsFile));
            inFile.useDelimiter(",");
            if(inFile.hasNextLine()) {
                inFile.nextLine();
            }
            while(inFile.hasNextLine()) {
                int temp = -1;
                if(inFile.hasNextInt()) {
                    temp = inFile.nextInt();
                }
                if(temp > maxStops) {
                    maxStops = temp;
                }
                inFile.nextLine();
            }
        } catch (NullPointerException | FileNotFoundException e) {
            e.printStackTrace();
        }

        // Initialising the adjacency matrix
        adjacencyMatrix = new int[maxStops+1][maxStops+1];
        for (int i = 0; i < maxStops; i++) {
            for (int j = 0; j < maxStops; j++) {
                adjacencyMatrix[i][j] = Integer.MAX_VALUE;
            }
        }

        System.out.println("Populating adjacency matrix.");

        // Adding costs for individual trips
        try {
            inFile = new Scanner(new File(stopTimesFile));
            inFile.useDelimiter(",");
            inFile.nextLine();
            int currentTripID = inFile.nextInt();
            inFile.next();
            inFile.next();
            int currentStop = inFile.nextInt();
            inFile.nextLine();
            while(inFile.hasNextLine()) {
                int newTripID = inFile.nextInt();
                inFile.next();
                inFile.next();
                int newStop = inFile.nextInt();
                if(currentTripID == newTripID) {
                    adjacencyMatrix[currentStop][newStop] = 1;
                }
                currentStop = newStop;
                currentTripID = newTripID;
                inFile.nextLine();
            }
        } catch (NullPointerException | FileNotFoundException e) {
            e.printStackTrace();
        }

        // Adding costs for transfers
        try {
            inFile = new Scanner(new File(transfersFile));
            inFile.useDelimiter(",|\\n");
            inFile.nextLine();

            while(inFile.hasNextLine()) {
                int startStop = inFile.nextInt();
                int destStop = inFile.nextInt();
                int check = inFile.nextInt();
                if(check == 0) {
                    adjacencyMatrix[startStop][destStop] = 2;
                }
                else if(check == 2) {
                    adjacencyMatrix[startStop][destStop] = inFile.nextInt();
                }
                inFile.nextLine();
            }
        } catch (NullPointerException | FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }

    public static void main(String[] args) {
        dijsktra test = new dijsktra("stops.txt", "transfers.txt", "stop_times.txt");
    }
}
