package ru.alexkulikov.smartvalentine;

public enum Genre {

    ROCK(1, "Rock"),
    POP(2, "Pop"),
    RAP_HIP_HOP(3, "Rap & Hip-Hop"),
    EASY_LISTENING(4, "Easy Listening"),
    DANCE_HOUSE(5, "Dance & House"),
    INSTRUMENTAL(6, "Instrumental"),
    METAL(7, "Metal"),
    ALTERNATIVE(21, "Alternative"),
    DUBSTEP(8,"Dubstep"),
    JAZZ_BLUES(1001, "Jazz & Blues"),
    DNB(10, "Drum & Bass"),
    TRANCE(11, "Trance"),
    CHANSON(12, "Chanson"),
    ETHNIC(13, "Ethnic"),
    ACOUSTIC_VOCAL(14,"Acoustic & Vocal"),
    REGGAE(15, "Reggae"),
    CLASSICAL(16, "Classical"),
    INDIE_POP(17,"Indie Pop"),
    SPEECH(19, "Speech"),
    DISCO(22, "Electropop & Disco"),
    OTHER(18, "Other");

    private int id;
    private String name;

    Genre (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
