package ml.fli.models.weka;

public class VKAudio {

    private String artist;
    private String track;

    public String getArtist() {
        return artist;
    }

    public String getTrack() {
        return track;
    }

    public VKAudio(String artist, String track) {
        this.artist = artist;
        this.track = track;
    }

    @Override
    public String toString() {
        return this.artist + ":" + this.track;
    }
}
