package org.aery.blackjack;

public class BlackJackPoint {

    public static int sum(int card1, int card2) {
        int sum = checkPoint(card1) + checkPoint(card2);
        return card1 == 1 || card2 == 1 ? sum + 10 : sum;
    }

    public static int sum(int card1, int card2, int card3) {
        int sum = checkPoint(card1) + checkPoint(card2) + checkPoint(card3);

        if (sum > 11) {
            return sum;
        }

        return card1 == 1 || card2 == 1 || card3 == 1 ? sum + 10 : sum;
    }

    public static int sum(int card1, int card2, int card3, int card4) {
        int sum = checkPoint(card1) + checkPoint(card2) + checkPoint(card3) + checkPoint(card4);

        if (sum > 11) {
            return sum;
        }

        return card1 == 1 || card2 == 1 || card3 == 1 || card4 == 1 ? sum + 10 : sum;
    }

    public static int sum(int card1, int card2, int card3, int card4, int card5) {
        int sum = checkPoint(card1) + checkPoint(card2) + checkPoint(card3) + checkPoint(card4) + checkPoint(card5);

        if (sum > 11) {
            return sum;
        }

        return card1 == 1 || card2 == 1 || card3 == 1 || card4 == 1 || card5 == 1 ? sum + 10 : sum;
    }

    public static int checkPoint(int card) {
        return Math.min(10, card);
    }


}
