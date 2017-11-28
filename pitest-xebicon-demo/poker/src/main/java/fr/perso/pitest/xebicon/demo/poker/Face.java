package fr.perso.pitest.xebicon.demo.poker;

/**
 * Created by sbuisson on 22/03/2017.
 */
public enum Face {
    As(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Six(6),
    Seven(7),
    Eight(8),
    Nine(9),
    Ten(10),
    Knight(11),
    Queen(12),
    King(13);
    int index;
    static int indexIterable = 0;

    Face(int index) {
        this.index = index;
    }
}
