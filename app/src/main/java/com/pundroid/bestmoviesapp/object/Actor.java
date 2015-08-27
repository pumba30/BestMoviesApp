package com.pundroid.bestmoviesapp.object;

/**
 * Created by pumba30 on 26.08.2015.
 */
public class Actor {
    private String pathToImageActor;
    private String nameActor;
    private String characterActor;

    public Actor() {
    }

    public String getCharacterActor() {
        return characterActor;
    }

    public String getPathToImageActor() {
        return pathToImageActor;
    }

    public String getNameActor() {
        return nameActor;
    }

    public void setCharacterActor(String characterActor) {
        this.characterActor = characterActor;
    }

    public void setNameActor(String nameActor) {
        this.nameActor = nameActor;
    }

    public void setPathToImageActor(String pathToImageActor) {
        this.pathToImageActor = pathToImageActor;
    }
}
