package com.example.kyl3g.sunhacksnov2018.Objects;

public class DietPattern {
    private boolean whiteMeat;
    private boolean redMeat;
    private boolean seafood;

    public DietPattern()
    {

    }
    public DietPattern(boolean whiteMeat, boolean redMeat, boolean seafood)
    {
        this.whiteMeat = whiteMeat;
        this.redMeat = redMeat;
        this.seafood = seafood;
    }

    public boolean isWhiteMeat() {
        return whiteMeat;
    }

    public void setWhiteMeat(boolean whiteMeat) {
        this.whiteMeat = whiteMeat;
    }

    public boolean isRedMeat() {
        return redMeat;
    }

    public void setRedMeat(boolean redMeat) {
        this.redMeat = redMeat;
    }

    public boolean isSeafood() {
        return seafood;
    }

    public void setSeafood(boolean seafood) {
        this.seafood = seafood;
    }
}
