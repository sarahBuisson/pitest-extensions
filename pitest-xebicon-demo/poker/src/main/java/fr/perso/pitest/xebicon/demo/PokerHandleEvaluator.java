package fr.perso.pitest.xebicon.demo.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class PokerHandleEvaluator {
    public String handleName(List<Card> handle) {
        List<GroupCard> groups = extractGroups(handle);
        String message = "";
        sortGroups(groups);
        for (GroupCard group : groups) {

            if (GroupSimilarity.Face.equals(group.getSimilarity())) {
                switch (group.getCards().size()) {
                    case 2:
                        message += "Pair";
                        break;
                    case 3:
                        message += "Brelan";
                        break;
                }
                message += " of " + group.getCards().get(0).face.name();
            }
            if (GroupSimilarity.Color.equals(group.getSimilarity()) && group.getCards().size() == handle.size()) {
                message = "Full" + message;

            }

        }


        if (isAQuinte(handle)) {
            message += "Quinte";
            if (lastCard(handle).face == Face.As) {
                message += " Royale";
            }
        }

        return message;

    }

    private boolean isAQuinte(List<Card> handle) {
        //If the 1 is an 1
        sortHandle(handle, 1);
        boolean isAQuinte = true;
        for (int i = 1; i < handle.size(); i++) {
            if (!isConsecutive(handle, i)) {
                isAQuinte = false;
            }
        }
        //If the 1 is an As
        if (!isAQuinte) {
            sortHandle(handle, Face.King.index + 1);
            isAQuinte = true;
            for (int i = 1; i < handle.size(); i++) {
                if (!isConsecutive(handle, i)) {
                    isAQuinte = false;
                }
            }

        }
        return isAQuinte;
    }

    private Card lastCard(List<Card> handle) {
        return handle.get(handle.size() - 1);
    }

    private void sortGroups(List<GroupCard> groups) {
        Collections.sort(groups, (GroupCard o1, GroupCard o2) -> {
                    return o2.getCards().size() - o1.getCards().size();
                }
        );
    }

    private void sortHandle(List<Card> handle, int faceAsIndex) {
        Face.As.index = faceAsIndex;
        Collections.sort(handle, (Card o1, Card o2) -> {
            return o1.face.index - o2.face.index;
        });
    }

    private boolean isConsecutive(List<Card> handle, int i) {
        return handle.get(i - 1).face.index + 1 == handle.get(i).face.index;
    }

    public List<GroupCard> extractGroups(List<Card> handle) {

        List<GroupCard> groups = new ArrayList<GroupCard>();

        for (Face face : Face.values()) {
            GroupCard group = new GroupCard();
            group.setSimilarity(GroupSimilarity.Face);
            groups.add(group);
            for (int i = 0; i < handle.size(); i++) {
                if (face.equals(handle.get(i).face)) {
                    group.getCards().add(handle.get(i));
                }
            }
        }
        for (Color color : Color.values()) {
            GroupCard group = new GroupCard();
            group.setSimilarity(GroupSimilarity.Color);
            groups.add(group);
            for (int i = 0; i < handle.size(); i++) {
                if (color.equals(handle.get(i).color)) {
                    group.getCards().add(handle.get(i));
                }
            }
        }
        return groups.stream().filter((group) -> group.getCards().size() > 1).collect(Collectors.toList());
    }

}
