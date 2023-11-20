import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static int activeSongYear;
    static int activeSongIndex = 0;

    public static void main(String[] args) {
        SongManager importer = new SongManager();

        // Buttons
        JFrame f = new JFrame("Frame title");
        f.setSize(400, 800);
        f.setLayout(null);

        JButton b1 = new JButton("Load Data");
        b1.setBounds(20, 40, 100, 30);
        f.add(b1);

        JButton b2 = new JButton("Next");
        b2.setBounds(130, 40, 100, 30);
        b2.setEnabled(false);
        f.add(b2);

        JButton b3 = new JButton("Prev");
        b3.setBounds(240, 40, 100, 30);
        b3.setEnabled(false);
        f.add(b3);

        // Year dropdown
        JComboBox<String> cb1 = new JComboBox<String>();
        cb1.setBounds(20,100, 100, 30);
        cb1.setEnabled(false);
        cb1.setEditable(true);
        f.add(cb1);

        // Label with % of songs viewable in active year
        JLabel l0 = new JLabel("");
        l0.setBounds(130,100, 100, 30);
        f.add(l0);

        // Song labels and textfields for song data
        JLabel l1 = new JLabel("Track name: ");
        JLabel l2 = new JLabel("Artist(s): ");
        JLabel l3 = new JLabel("Release Date: ");
        JLabel l4 = new JLabel("Total streams: ");

        l1.setBounds(20,150, 100, 30);
        l2.setBounds(20,190, 100, 30);
        l3.setBounds(20,230, 100, 30);
        l4.setBounds(20,270, 100, 30);

        f.add(l1);
        f.add(l2);
        f.add(l3);
        f.add(l4);

        JTextField t1 = new JTextField();
        JTextField t2 = new JTextField();
        JTextField t3 = new JTextField();
        JTextField t4 = new JTextField();

        t1.setBounds(120,150, 200, 30);
        t2.setBounds(120,190, 200, 30);
        t3.setBounds(120,230, 200, 30);
        t4.setBounds(120,270, 200, 30);

        f.add(t1);
        f.add(t2);
        f.add(t3);
        f.add(t4);

        // The playlist bars
        JLabel br0 = new JLabel("Which playlists is it on?");
        JLabel br1 = new JLabel("Spotify:  -");
        JLabel br2 = new JLabel("Apple:    -");
        JLabel br3 = new JLabel("Deezer: -");

        br0.setBounds(20,330, 200, 30);
        br1.setBounds(20,360, 200, 30);
        br2.setBounds(20,390, 200, 30);
        br3.setBounds(20,420, 200, 30);

        f.add(br0);
        f.add(br1);
        f.add(br2);
        f.add(br3);

        b1.addActionListener(new ActionListener() {
            /***
             * Load the data
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                Song song = importer.getActiveSong();

                // Set the active song, ie, the earliest one in the most recent year
                t1.setText(song.track_name());
                t2.setText(song.artist_name());
                t3.setText(song.release_date());
                t4.setText(song.total_streams());

                // Add all the years in the data to the dropdown
                String[] years = importer.getYears();
                for (String year : years) {
                    cb1.addItem(year);
                    activeSongYear = Integer.parseInt(year);
                }

                // Set the most recent year
                cb1.setSelectedItem(years[years.length-1]);

                // Enable song navigation buttons, disable load button
                b1.setEnabled(false);
                cb1.setEnabled(true);
                b2.setEnabled(true);
                b3.setEnabled(true);

                // Shows the amount of songs in that year vs total
                l0.setText(
                        String.valueOf(Math.round(((double)importer.getSongCount(importer.getYearIndex(activeSongYear)) / (double)importer.getSongCount()) *100)) + "% | " +
                                importer.getSongCount(importer.getYearIndex(activeSongYear)) + " of " + importer.getSongCount()
                );

            }
        });

        b2.addActionListener(new ActionListener() {
            /***
             * Move to the next song
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Find the song with the previous index in the same year
                Song newSong = importer.getSong(importer.getYearIndex(activeSongYear), activeSongIndex+1);

                // Set as the active song
                importer.setActiveSong(newSong);
                activeSongIndex++;

                // Set the first song of that year
                t1.setText(newSong.track_name());
                t2.setText(newSong.artist_name());
                t3.setText(newSong.release_date());
                t4.setText(newSong.total_streams());

                // Reruns the playlist bars
                barSetup(importer, br1, br2, br3);
            }
        });

        b3.addActionListener(new ActionListener() {
            /***
             * Move to the previous song
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Find the song with the previous index in the same year
                Song newSong = importer.getSong(importer.getYearIndex(activeSongYear), activeSongIndex-1);

                // Set as the active song
                importer.setActiveSong(newSong);
                activeSongIndex--;

                // Set the first song of that year
                t1.setText(newSong.track_name());
                t2.setText(newSong.artist_name());
                t3.setText(newSong.release_date());
                t4.setText(newSong.total_streams());

                // Reruns the playlist bars
                barSetup(importer, br1, br2, br3);
            }
        });

        cb1.addActionListener(new ActionListener() {
            /***
             * Switch to another year
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Find the year the user switched to
                activeSongYear = Integer.parseInt((String) Objects.requireNonNull(cb1.getSelectedItem()));

                // Find the first song for the year being set
                Song newSong = importer.getSong(importer.getYearIndex(activeSongYear), 0);
                importer.setActiveSong(newSong);

                // Set the first song of that year
                t1.setText(newSong.track_name());
                t2.setText(newSong.artist_name());
                t3.setText(newSong.release_date());
                t4.setText(newSong.total_streams());

                // Reset the song index for the new year
                activeSongIndex = 0;

                // Shows the amount of songs in that year vs total
                l0.setText(
                        String.valueOf(Math.round(((double)importer.getSongCount(importer.getYearIndex(activeSongYear)) / (double)importer.getSongCount()) *100)) + "% | " +
                                importer.getSongCount(importer.getYearIndex(activeSongYear)) + " of " + importer.getSongCount()
                );

                // Reruns the playlist bars
                barSetup(importer, br1, br2, br3);
            }
        });

        f.setVisible(true);
    }

    /***
     * Returns the length of each bar in unicode squares
     * @param percent % of total playlists
     * @return String of squares
     */
    private static String bars(double percent) {
        StringBuilder barGenerator = new StringBuilder();

        for (int i = 0; i < Math.round(percent * 100 / 10); i++) {
            barGenerator.append("◼");
        }
        return barGenerator + " " + Double.toString(Math.round(percent * 100)) + "%";
    }

    /***
     * Recalculates the bars
     *
     * @param importer SongManager Instance
     * @param br1 JLabel for Spotify
     * @param br2 JLabel for Apple
     * @param br3 JLabel for Deezer
     */
    private static void barSetup(SongManager importer, JLabel br1, JLabel br2, JLabel br3) {
        long total_in_playlists = importer.getActiveSong().spotify_playlists() + importer.getActiveSong().apple_playlists() + importer.getActiveSong().deezer_playlists();
        br1.setText("Spotify:  " + bars((double) importer.getActiveSong().spotify_playlists() / total_in_playlists));
        br2.setText("Apple:    " + bars((double) importer.getActiveSong().apple_playlists() / total_in_playlists));
        br3.setText("Deezer: " + bars((double) importer.getActiveSong().deezer_playlists() / total_in_playlists));
    }


}