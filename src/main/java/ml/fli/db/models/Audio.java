package ml.fli.db.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "audios")
public class Audio {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "audio")
    private String audio;

    public Audio(){ }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }



}
