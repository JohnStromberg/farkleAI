package farkleGUI;

import java.util.Random;

/**
 * Class for a dice
 * @author John Stromberg
 */
public class Dice implements Comparable<Dice>{

    /**
     * The value of the dice
      */
    private int value;

    /**
     * Constructs a dice object with a random value
     */
    public Dice() {
    	Random rand = new Random();
        value = rand.nextInt(6) + 1;
    }

    /**
     * Setter for the value
     * @param value the new value
     */
    public Dice(int value) {
        this.value = value;
    }

    /**
     * A getter for the value
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * A compareTo method that puts 0's last when sorting
     * This allows for the dice to look nicer in the GUI
     */
    @Override
    public int compareTo(Dice other) {
        int tempValue1 = this.value;
        int tempValue2 = other.value;
        if(this.value == 0) {
            tempValue1 = Integer.MAX_VALUE;
        }
        if(other.value == 0) {
            tempValue2 = Integer.MAX_VALUE;
        }

        if(tempValue1 > tempValue2) {
            return 1;
        } else if(tempValue1 < tempValue2) {
            return  -1;
        } else {
            return 1;
        }
    }
    

}
