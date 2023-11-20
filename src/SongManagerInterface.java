public interface SongManagerInterface {

    // SongManager should have one constructor, an empty one (no parameters)

    /**
     * Retrieves the count of release years
     * @return      count of release years
     */
    public int getYearCount();

    /**
     * Retrieves the number of songs in the specified release year (by index)
     * @param yearIndex       the index of the release year
     * @return                song count in that release year
     */
    public int getSongCount(int yearIndex);

    /**
     * Retrieves the number of songs in all release years
     * @return                song count in all release years
     */
    public int getSongCount();

    /**
     * Retrieves the release year at the specified index
     * @param yearIndex       index of the desired release year
     * @return                release year
     */
    public String getYearName(int yearIndex);

    /**
     * Retrieves the number of songs in the specified release year (by name)
     * @param year       the release year
     * @return           song count in that release year
     */
    public int getSongCount(String year);

    /**
     * Retrieves the song at the specific release year and song index
     * @param yearIndex       release year index
     * @param songIndex       song index
     * @return                song at that array position
     */
    public Song getSong(int yearIndex, int songIndex);

    /**
     * Retrieves a copy of the song array for the release year at the specified index
     * @param yearIndex       release year index
     * @return                copy of song array (not a reference to the internal one)
     */
    public Song[] getSongs(int yearIndex);

    /**
     * Retrieves the first release year index associated with the specified song's track name
     * @param trackName       the track name to search for
     * @return                the first release year index containing the specified song, or -1 if not found
     */
    public int findSongYear(String trackName);
}
