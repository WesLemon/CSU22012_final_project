import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class timeSearch {

    private static String[] trips;
    private static String[] stopTimes;

    public timeSearch(String stopTimesPath) {

        try {
            Scanner inFile = new Scanner(new File(stopTimesPath));
            inFile.nextLine();
            inFile.useDelimiter(",");
            while (inFile.hasNextLine()) {
                String timesLine = inFile.nextLine();
                inFile.next();
                String time = inFile.next();
                if (!(time.charAt(0) == '2' && ((time.charAt(1) == '4') || (time.charAt(1) == '5')
                        || (time.charAt(1) == '6') || (time.charAt(1) == '7')
                        || (time.charAt(1) == '8') || (time.charAt(1) == '9')))
                        || time.charAt(1) == ':') {
                    stopTimes.add(time);
                    trips.add(timesLine);
                }
                else {
                    System.out.println("no" + time);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        test();
        System.out.println("test");
    }

    private void test() {
        System.out.println("test");
    }

    public static void main(String[] args) {
        timeSearch test = new timeSearch("stop_times.txt");
    }

}
