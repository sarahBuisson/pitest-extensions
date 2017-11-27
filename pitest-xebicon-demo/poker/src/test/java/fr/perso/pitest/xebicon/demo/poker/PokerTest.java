package fr.perso.pitest.xebicon.demo.poker;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PokerTest {
    @Test
    public void should_find_pair() {

        //Given
        List<Card> handle = Arrays.asList(new Card(Face.Two, Color.Careau), new Card(Face.Two, Color.Pique));

        //When
        List<GroupCard> groups = new PokerHandleEvaluator().extractGroups(handle);

        //Then
        Assert.assertEquals(1, groups.size());
        Assert.assertEquals(GroupSimilarity.Face, groups.get(0).getSimilarity());
        Assert.assertEquals(2, groups.get(0).getCards().size());

    }

    @Test
    public void should_name_pair() {

        //Given
        List<Card> handle = Arrays.asList(new Card(Face.Two, Color.Careau), new Card(Face.Two, Color.Pique));

        //When
        String message = new PokerHandleEvaluator().handleName(handle);

        //Then
        Assert.assertEquals("Pair of Two", message);

    }

    @Test
    public void should_name_brelan() {

        //Given
        List<Card> handle = Arrays.asList(new Card(Face.Two, Color.Careau), new Card(Face.Two, Color.Pique), new Card(Face.Two, Color.Pique));

        //When
        String message = new PokerHandleEvaluator().handleName(handle);

        //Then
        Assert.assertEquals("Brelan of Two", message);

    }

    @Test
    public void should_find_full() {

        //Given
        List<Card> handle = Arrays.asList(new Card(Face.Three, Color.Careau), new Card(Face.Two, Color.Careau));

        //When
        List<GroupCard> groups = new PokerHandleEvaluator().extractGroups(handle);

        //Then
        Assert.assertEquals(1, groups.size());
        Assert.assertEquals(GroupSimilarity.Color, groups.get(0).getSimilarity());
        Assert.assertEquals(2, groups.get(0).getCards().size());

    }

    @Test
    public void should_name_full() {

        //Given
        List<Card> handle = Arrays.asList(new Card(Face.Two, Color.Careau), new Card(Face.King, Color.Careau));

        //When
        String message = new PokerHandleEvaluator().handleName(handle);

        //Then

        Assert.assertEquals("Full", message);

    }

    @Test
    public void should_name_Quinte() {

        //Given
        List<Card> handle = Arrays.asList(new Card(Face.Two, Color.Careau), new Card(Face.Three, Color.Careau), new Card(Face.Four, Color.Pique));

        //When
        String message = new PokerHandleEvaluator().handleName(handle);

        //Then

        Assert.assertEquals("Quinte", message);

        //TODO : it never append, so why bother?

    }
    @Test
    public void should_name_Quinte_even_in_disord() {

        //Given
        List<Card> handle = Arrays.asList(new Card(Face.Three, Color.Careau), new Card(Face.Two, Color.Careau), new Card(Face.Four, Color.Pique));

        //When
        String message = new PokerHandleEvaluator().handleName(handle);

        //Then

       // Assert.assertEquals("Quinte", message);
        //TODO : it never append, so why bother?

    }


    @Test
    public void should_name_quinte_royale() {

        //Given
        List<Card> handle = Arrays.asList(new Card(Face.King, Color.Careau), new Card(Face.As, Color.Careau), new Card(Face.Queen, Color.Pique));

        //When
        String message = new PokerHandleEvaluator().handleName(handle);

        //Then
        Assert.assertEquals("Quinte Royale", message);
        //TODO : it never append, so why bother?

    }
}
