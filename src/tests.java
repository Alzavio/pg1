import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class tests {
    SongManager SongManager = new SongManager();

    /***
     * Test the getYearCount method
     */
    @Test
    public void testGetYearCount() {
        assertEquals(50, SongManager.getYearCount());
    }

    /***
     * Test the 3 overloaded getSongCount methods
     */
    @Test
    public void testGetSongCount() {
        // Tests total number of songs
        assertEquals(953, SongManager.getSongCount());

        // Tests by year
        assertEquals(1, SongManager.getSongCount("1930"));
        assertEquals(2, SongManager.getSongCount("1982"));
        assertEquals(13, SongManager.getSongCount("2013"));
        assertEquals(119, SongManager.getSongCount("2021"));

        // Tests by index
        assertEquals(1, SongManager.getSongCount(0));
        assertEquals(37, SongManager.getSongCount(46));
    }

    /***
     * Test the getYearName returns the right index's
     */
    @Test
    public void testGetYearName() {
        assertEquals(1, SongManager.getSongCount("1930"));
        assertEquals(119, SongManager.getSongCount("2021"));
    }

    /***
     * Check if the Song instance is correctly returned
     */
    @Test
    public void testGetSong() {
        assertEquals(
                new Song(
                        "The Christmas Song (Merry Christmas To You) - Remastered 1999",
                        "Nat King Cole",
                        "1/11/1946",
                        "389771964",
                        1946,
                        11500,
                        140,
                        251
                ),
                SongManager.getSong(2, 0)
        );
    }

    /***
     * See if getSongs correctly returns an array with Song
     */
    @Test
    public void testGetSongs() {
        Song[] test = new Song[1];
        test[0] = new Song(
                "The Christmas Song (Merry Christmas To You) - Remastered 1999",
                "Nat King Cole",
                "1/11/1946",
                "389771964",
                1946,
                11500,
                140,
                251
        );

        assertEquals(test, SongManager.getSongs(2));
    }

    /***
     * Check to see if the correct year index is returned
     */
    @Test
    public void testFindSongYear() {
        assertEquals(5, SongManager.findSongYear("Holly Jolly Christmas"));
    }

    /***
     * See if the correct year index is returned
     */
    @Test
    public void testGetYearIndex() {
        assertEquals(0, SongManager.getYearIndex(1930));
    }

    /***
     * Checks if the list of years is accurate
     */
    @Test
    public void testGetYears() {
        String[] test = new String[]{"1930", "1942", "1946", "1950", "1952", "1957", "1958", "1959", "1963", "1968", "1970", "1971", "1973", "1975", "1979", "1982", "1983", "1984", "1985", "1986", "1987", "1991", "1992", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2002", "2003", "2004", "2005", "2007", "2008", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023"};

        assertEquals(test, SongManager.getYears());
    }

    /***
     * Check if the default song (first song of the most recent year) matches up
     */
    @Test
    public void testGetActiveSong() {
        assertEquals(
                new Song(
                        "Seven (feat. Latto) (Explicit Ver.)",
                        "Latto, Jung Kook",
                        "14/7/2023",
                        "141381703",
                        2023,
                        553,
                        43,
                        45
                ),
                SongManager.getActiveSong()
        );
    }

    /***
     * Check if changing the active song works by testing the setActiveSong, and getActiveSong methods
     */
    @Test
    public void testSetActiveSong() {
        SongManager.setActiveSong(new Song(
                "test",
                "test",
                "3/3/2023",
                "539230903",
                2023,
                0,
                0,
                0
        ));
        assertEquals(
                new Song(
                        "test",
                        "test",
                        "3/3/2023",
                        "539230903",
                        2023,
                        0,
                        0,
                        0
                ),
                SongManager.getActiveSong()
        );
    }
}
