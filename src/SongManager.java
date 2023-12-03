import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SongManager implements SongManagerInterface {

    private static Song activeSong;

    private String[][] YearlySongAmount;
    private Song[][] SongRecords;
    private int SongCount = 0;
    private int YearCount = 0;

    public SongManager() {
        try {
            String track_name;
            String artist_name;
            String release_date;
            String total_streams;

            CSVReader songStatReader = new CSVReader(new FileReader("count-by-release-year.csv"));

            // Populate the list with all the years in the csv and YearCount
            String[] nextLine;
            songStatReader.readNext();

            while ((nextLine = songStatReader.readNext()) != null) {
                YearCount++;
            }

            songStatReader.close();

            // Reset csv
            songStatReader = new CSVReader(new FileReader("count-by-release-year.csv"));

            // Populates an array of arrays with the year and number of songs
            YearlySongAmount = new String[YearCount][2];
            int iterator = 0;
            songStatReader.readNext();

            while ((nextLine = songStatReader.readNext()) != null) {
                YearlySongAmount[iterator] = nextLine;
                iterator++;
            }

            songStatReader.close();

            CSVReader songReader = new CSVReader(new FileReader("spotify-2023.csv"));

            // Jagged array with songs
            SongRecords = new Song[iterator+2][];
            // Used to set the "y"/row of the 2d array. Starts at -1 due to loop starting with rowCount++.
            int rowCount = -1;
            String yearHolder = "";
            boolean firstRun = true;
            songReader.readNext();

            // Used to set the first song info in the last year after everything runs. Initialize with placeholder
            activeSong = new Song("","","", "", 0, 0, 0, 0);

            // Populate SongRecords and SongCount
            while ((nextLine = songReader.readNext()) != null) {

                // Sets the row width based on number of songs
                if (!Objects.equals(yearHolder, nextLine[3])) {
                    SongRecords[getYearIndex(nextLine[3])] = new Song[getSongCount(nextLine[3])];
                }

                track_name = nextLine[0];
                artist_name = nextLine[1];
                release_date = nextLine[5]+"/"+nextLine[4]+"/"+nextLine[3];
                total_streams = nextLine[8];

                // We need yearHolder to keep track of when to reset rowCount
                if (firstRun) {
                    yearHolder = nextLine[3];
                    firstRun = false;
                }

                // Reset rowcount if we've moved on to the next year
                try {
                    int index;
                    // If we're still on the same year, up the row count, if not reset it, and use the new year index
                    if (Objects.equals(yearHolder, nextLine[3])) {
                        rowCount++;

                        // Adds the new Song instance to the year index and appropriate row
                        index = getYearIndex(nextLine[3]);
                        SongRecords[index][rowCount] = new Song(track_name, artist_name, release_date, total_streams, Integer.parseInt(nextLine[3]), Long.parseLong(nextLine[6].replace(",", "")), Long.parseLong(nextLine[9].replace(",", "")), Long.parseLong(nextLine[11].replace(",", "")));

                    } else {
                        // Resets row count since we're now on a new year
                        rowCount = 0;

                        // Adds the new Song instance to the year index and appropriate row
                        index = getYearIndex(nextLine[3]);
                        activeSong = new Song(track_name, artist_name, release_date, total_streams, Integer.parseInt(nextLine[3]), Long.parseLong(nextLine[6].replace(",", "")), Long.parseLong(nextLine[9].replace(",", "")), Long.parseLong(nextLine[11].replace(",", "")));

                        // Adds the new Song instance to the year and appropriate row
                        SongRecords[index][rowCount] = activeSong;

                        // Saves the new year that's active
                        yearHolder = nextLine[3];
                    }
                } catch (Exception e) {
                    System.out.println();
                    System.out.println("error " + nextLine[6] + " " + nextLine[9] + " " + nextLine[11] + e);
                    rowCount++;
                }

                SongCount++;
            }

            songReader.close();

        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getYearCount() {
        return YearCount;
    }

    @Override
    public int getSongCount(int yearIndex) {
        return Integer.parseInt(YearlySongAmount[yearIndex][1]);
    }

    @Override
    public int getSongCount() {
        return SongCount;
    }

    @Override
    public String getYearName(int yearIndex) {
        return YearlySongAmount[yearIndex][0];
    }

    @Override
    public int getSongCount(String year) {
        for (int i = 0; i < YearCount; i++) {
            if (Objects.equals(YearlySongAmount[i][0], year)) {
                return Integer.parseInt(YearlySongAmount[i][1]);
            }
        }
        return -1;
    }

    @Override
    public Song getSong(int yearIndex, int songIndex) {
        // What's the point of yearIndex if I can just grap the song with songIndex?
        return SongRecords[yearIndex][songIndex];
    }

    @Override
    public Song[] getSongs(int yearIndex) {
        return SongRecords[yearIndex];
    }

    @Override
    public int findSongYear(String trackName) {
        // Loop through years, then loop through songs in that year
        for (int i = 0; i < YearCount; i++) {

            for (int x = 0; x < SongRecords[i].length; x++) {

                try {
                    // Check the first record
                    if (SongRecords[i][x] != null && Objects.equals(SongRecords[i][x].track_name(), trackName)) {
                        return x;
                    }
                } catch (Exception ArrayIndexOutOfBoundsException) {
                    System.out.print("Error ArrayIndexOutOfBoundsException");
                }

            }
        }

        return -1;
    }

    /***
     * Finds the index of the year requested in SongRecords
     *
     * @param year Year int
     * @return The index of the year
     */
    public int getYearIndex(int year) {
        for (int i = 0; i < YearCount; i++) {
            if (SongRecords[i].length > 0 && SongRecords[i][0] != null && Objects.equals(SongRecords[i][0].year(), year)) {
                return i;
            }
        }

        return -1;
    }

    public int getYearIndex(String year) {
        for (int i = 0; i < YearCount; i++) {
            if (Objects.equals(YearlySongAmount[i][0], year)) {
                return i;
            }
        }
        return -1;
    }

    /***
     *
     * @return Returns the list of years in the data
     */
    public String[] getYears() {
        String[] yearsList = new String[YearCount];

        for (int i = 0; i < YearCount; i++) {
            yearsList[i] = YearlySongAmount[i][0];
        }

        return yearsList;
    }

    /***
     *
     * @return The currently active song
     */
    public Song getActiveSong() {
        return activeSong;
    }

    /***
     * Takes a song, and stores it as the active song (static)
     *
     * @param activeSong A song instance
     */
    public void setActiveSong(Song activeSong) {
        SongManager.activeSong = activeSong;
    }
}
