package fr.perso.pitest.xebicon.demo.poker;

import java.util.ArrayList;
import java.util.List;


public class GroupCard {
    List<Card> cards=new ArrayList<Card>();
    GroupSimilarity similarity;


    public List<Card> getCards() {
        return cards;
    }

    public GroupSimilarity getSimilarity() {
        return similarity;
    }

    public void setSimilarity(GroupSimilarity similarity) {
        this.similarity = similarity;
    }
}
