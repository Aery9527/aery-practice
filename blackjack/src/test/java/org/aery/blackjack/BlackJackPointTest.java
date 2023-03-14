package org.aery.blackjack;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BlackJackPointTest {

    @Test
    void sum2() {
        Assertions.assertThat(BlackJackPoint.sum(1, 1)).isEqualTo(12);
        Assertions.assertThat(BlackJackPoint.sum(1, 2)).isEqualTo(13);
        Assertions.assertThat(BlackJackPoint.sum(2, 1)).isEqualTo(13);
        Assertions.assertThat(BlackJackPoint.sum(2, 2)).isEqualTo(4);

        Assertions.assertThat(BlackJackPoint.sum(1, 9)).isEqualTo(20);
        Assertions.assertThat(BlackJackPoint.sum(1, 10)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 11)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 12)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 13)).isEqualTo(21);

        Assertions.assertThat(BlackJackPoint.sum(9, 1)).isEqualTo(20);
        Assertions.assertThat(BlackJackPoint.sum(10, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(11, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(12, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(13, 1)).isEqualTo(21);
    }

    @Test
    void sum3() {
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1)).isEqualTo(13);

        Assertions.assertThat(BlackJackPoint.sum(1, 1, 9)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 9, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(9, 1, 1)).isEqualTo(21);

        Assertions.assertThat(BlackJackPoint.sum(1, 1, 10)).isEqualTo(12);
        Assertions.assertThat(BlackJackPoint.sum(1, 10, 1)).isEqualTo(12);
        Assertions.assertThat(BlackJackPoint.sum(10, 1, 1)).isEqualTo(12);

        Assertions.assertThat(BlackJackPoint.sum(1, 5, 5)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(5, 1, 5)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(5, 5, 1)).isEqualTo(21);
    }

    @Test
    void sum4() {
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1, 1)).isEqualTo(14);

        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1, 8)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 8, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 8, 1, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(8, 1, 1, 1)).isEqualTo(21);

        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1, 9)).isEqualTo(12);
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 9, 1)).isEqualTo(12);
        Assertions.assertThat(BlackJackPoint.sum(1, 9, 1, 1)).isEqualTo(12);
        Assertions.assertThat(BlackJackPoint.sum(9, 1, 1, 1)).isEqualTo(12);

        Assertions.assertThat(BlackJackPoint.sum(1, 5, 6, 7)).isEqualTo(19);
        Assertions.assertThat(BlackJackPoint.sum(5, 1, 6, 7)).isEqualTo(19);
        Assertions.assertThat(BlackJackPoint.sum(5, 6, 1, 7)).isEqualTo(19);
        Assertions.assertThat(BlackJackPoint.sum(5, 6, 7, 1)).isEqualTo(19);
    }

    @Test
    void sum5() {
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1, 1, 1)).isEqualTo(15);

        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1, 1, 7)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1, 7, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 7, 1, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(1, 7, 1, 1, 1)).isEqualTo(21);
        Assertions.assertThat(BlackJackPoint.sum(7, 1, 1, 1, 1)).isEqualTo(21);

        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1, 1, 9)).isEqualTo(13);
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 1, 9, 1)).isEqualTo(13);
        Assertions.assertThat(BlackJackPoint.sum(1, 1, 9, 1, 1)).isEqualTo(13);
        Assertions.assertThat(BlackJackPoint.sum(1, 9, 1, 1, 1)).isEqualTo(13);
        Assertions.assertThat(BlackJackPoint.sum(9, 1, 1, 1, 1)).isEqualTo(13);

        Assertions.assertThat(BlackJackPoint.sum(1, 3, 4, 5, 6)).isEqualTo(19);
        Assertions.assertThat(BlackJackPoint.sum(3, 1, 4, 5, 6)).isEqualTo(19);
        Assertions.assertThat(BlackJackPoint.sum(3, 4, 1, 5, 6)).isEqualTo(19);
        Assertions.assertThat(BlackJackPoint.sum(3, 4, 5, 1, 6)).isEqualTo(19);
        Assertions.assertThat(BlackJackPoint.sum(3, 4, 5, 6, 1)).isEqualTo(19);
    }

    @Test
    void checkPoint() {
        Assertions.assertThat(BlackJackPoint.checkPoint(1)).isEqualTo(1);
        Assertions.assertThat(BlackJackPoint.checkPoint(2)).isEqualTo(2);
        Assertions.assertThat(BlackJackPoint.checkPoint(3)).isEqualTo(3);
        Assertions.assertThat(BlackJackPoint.checkPoint(4)).isEqualTo(4);
        Assertions.assertThat(BlackJackPoint.checkPoint(5)).isEqualTo(5);
        Assertions.assertThat(BlackJackPoint.checkPoint(6)).isEqualTo(6);
        Assertions.assertThat(BlackJackPoint.checkPoint(7)).isEqualTo(7);
        Assertions.assertThat(BlackJackPoint.checkPoint(8)).isEqualTo(8);
        Assertions.assertThat(BlackJackPoint.checkPoint(9)).isEqualTo(9);
        Assertions.assertThat(BlackJackPoint.checkPoint(10)).isEqualTo(10);
        Assertions.assertThat(BlackJackPoint.checkPoint(11)).isEqualTo(10);
        Assertions.assertThat(BlackJackPoint.checkPoint(12)).isEqualTo(10);
        Assertions.assertThat(BlackJackPoint.checkPoint(13)).isEqualTo(10);
    }

}
