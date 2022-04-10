import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class dijsktra {

    private static int[][] adjacencyMatrix;
    private static int numStops = 0;

    dijsktra(String stopsFile, String transfersFile, String stopTimesFile) {

        // Find how many distinct stops exist from the input file.
        setNumStops(stopsFile);

        // Initialising the adjacency matrix
        adjacencyMatrix = new int[numStops +1][numStops +1];
        for (int i = 0; i < numStops; i++) {
            for (int j = 0; j < numStops; j++) {
                adjacencyMatrix[i][j] = Integer.MAX_VALUE;
            }
        }

        // Private methods to populate the adjacency matrix using the input files.
        addCostsForBusRoutes(stopTimesFile);
        addTransferCosts(transfersFile);
    }

    private static void setNumStops(String stopsFile) {
        Scanner inFile;
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
                if(temp > numStops) {
                    numStops = temp;
                }
                inFile.nextLine();
            }
        } catch (NullPointerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addCostsForBusRoutes(String stopTimesFile) {
        Scanner inFile;
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
    }

    private static void addTransferCosts(String transfersFile) {
        Scanner inFile;
        try {
            inFile = new Scanner(new File(transfersFile));
            inFile.useDelimiter("[,\\n]");
            inFile.nextLine();

            while(inFile.hasNextLine()) {
                int startStop = inFile.nextInt();
                int destStop = inFile.nextInt();
                int check = inFile.nextInt();
                if(check == 0) {
                    adjacencyMatrix[startStop][destStop] = 2;
                }
                else if(check == 2) {
                    // Error checking in case there are stops with transfer tiles that have already been given a value
                    // between them because they come after one another on a bus route.
                    int temp = inFile.nextInt();
                    if(temp < adjacencyMatrix[startStop][destStop]) {
                        adjacencyMatrix[startStop][destStop] = temp;
                    }
                }
                inFile.nextLine();
            }
        } catch (NullPointerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int shortestPath(int stop1, int stop2) {

        int[] distTo = new int[numStops];
        boolean[] visited = new boolean[numStops];
        int[] parents = new int[numStops];

        // Initialise the arrays
        for (int i = 0; i < numStops; i++) {
            distTo[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }
        distTo[stop1] = 0;
        parents[stop1] = -1; // Because the starting stop has no parent.

        // Get the shortest paths from source to each vertex.
        for (int i = 0; i < numStops-1; i++) {
            int vertex1 = minDistance(distTo, visited);
            visited[vertex1] = true;
            for (int vertex2 = 0; vertex2 < numStops; vertex2++) {
                if(adjacencyMatrix[vertex1][vertex2] != Integer.MAX_VALUE && !visited[vertex2] && distTo[vertex1] != Integer.MAX_VALUE
                        && distTo[vertex1] + adjacencyMatrix[vertex1][vertex2] < distTo[vertex2]) {
                    distTo[vertex2] = distTo[vertex1] + adjacencyMatrix[vertex1][vertex2];
                    parents[vertex2] = vertex1;
                }
            }
        }
        printPath(parents, stop2, stop2);
        return distTo[stop2];
    }

    private int minDistance(int[] distTo, boolean[] visited) {

        int min = Integer.MAX_VALUE;
        int min_index = 0;

        for (int i = 0; i < numStops; i++) {
            if(!visited[i] && distTo[i] < min)
            {
                min = distTo[i];
                min_index = i;
            }
        }
        return min_index;
    }

    private void printPath(int[] parents , int vertex, int child) {
        if(vertex == -1) {
            return;
        }
        printPath(parents, parents[vertex], vertex);
        if(vertex == child) {
            System.out.println(vertex);
        }
        else{
            System.out.print(vertex + " -> ");
        }
    }

    public static void main(String[] args) {
        dijsktra test = new dijsktra("stops.txt", "transfers.txt", "stop_times.txt");
        int testStop1 = 1276;
        int testStop2 = 122;
        int dist = test.shortestPath(testStop1, testStop2);
        System.out.println("Dist from 1276 to 122 is: " + dist);
    }
}
