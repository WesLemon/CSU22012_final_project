import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HelloWorld {

    private static final ArrayList<Integer> stopIDs = new ArrayList<>();
    private static final ArrayList<String> stopNames = new ArrayList<>();
    private static final ArrayList<String> stopTimes = new ArrayList<>();
    private static final ArrayList<String> stops = new ArrayList<>();
    private static final ArrayList<String> trips = new ArrayList<>();


    public static void main(String[] args) {
        System.out.println("Hello world.");

        String stopsPath = "stops.txt";
        String stopTimesPath = "stop_times.txt";
        String transfersPath = "transfers.txt";

        Scanner lineScanner;

        try {
            Scanner inFile = new Scanner(new File(stopsPath));
            inFile.nextLine();
            while (inFile.hasNextLine()) {
                String stopsLine = inFile.nextLine();
                stops.add(stopsLine);
                lineScanner = new Scanner(stopsLine);
                lineScanner.useDelimiter(",");
                if (lineScanner.hasNextInt()) {
                    stopIDs.add(lineScanner.nextInt());
                }
                lineScanner.next();
                stopNames.add(lineScanner.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Scanner inFile = new Scanner(new File(stopTimesPath));
            inFile.nextLine();
            while (inFile.hasNextLine()) {
                String timesLine = inFile.nextLine();
                trips.add(timesLine);
                lineScanner = new Scanner(timesLine);
                lineScanner.useDelimiter(",");
                lineScanner.next();
                String time = lineScanner.next();
                if (!(time.charAt(0) == '2' && (time.charAt(1) == '4') || (time.charAt(1) == '5')
                        || (time.charAt(1) == '6') || (time.charAt(1) == '7')
                        || (time.charAt(1) == '8') || (time.charAt(1) == '9'))) {
                    stopTimes.add(time);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
