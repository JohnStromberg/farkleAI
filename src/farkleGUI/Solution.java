package farkleGUI;


/**
 * This class is for a solution to a given roll of dice
 * @author  John Stromberg
 */
public class Solution {

    /**
     * This is a array of dice that is needed for that solution
     */
    private Dice [] diceInSol;

    /**
     * The number of dice in the solution
     */
    private int size;

    /**
     * The number of points that solution is worth
     */
    private int points;

    /**
     * The name of the solution
     */
    private String name;

    /**
     * The constructor for a solution with no parameters
     */
    public Solution() {
        points = 0;
        size = 0;
        diceInSol = new Dice[6];
        for(int i = 0; i < diceInSol.length; i++) {
            diceInSol[i] = new Dice(0);
        }
    }

    /**
     * A constructor with parameters
     * @param points the number of points the solution is worth
     * @param size how many dice are in the solution
     * @param name the name of the solution
     */
    public Solution(int points, int size, String name) {
        this.points = points;
        this.size = size;
        this.name = name;
        diceInSol = new Dice[6];
        for(int i = 0; i < diceInSol.length; i++) {
            diceInSol[i] = new Dice(0);
        }
    }

    /**
     * Getter for the dice in the solution
     * @return the dice in the solution
     */
    public Dice[] getDiceInSol() {
        return diceInSol;
    }

    /**
     * Getter for the number of points
     * @return the number of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the size
     * @return the size of the array
     */
    public int getSize() {
        return size;
    }

    /**
     * Checks for a full run
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckfullRun(Dice [] roll) {
        int [] count = new int[7];
        for(Dice num : roll) {
            if(num.getValue() != 0) {
                count[num.getValue()]++;
            }
        }
        int checker = 0;
        for(int i = 0; i < count.length; i++) {
            if(count[i] == 1) {
                checker++;
            }
        }
        if(checker == 6) {
            for(int i = 1; i < count.length; i++) {
                diceInSol[i-1] = new Dice(i);
            }
            points += 3000;
            name = "Full run";
            size = 6;
            return true;
        }
        return false;
    }

    /**
     * Check for 3 6's
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckthreeSix(Dice [] roll) {
        int numSix = 0;
        for(int i = 0; i < roll.length; i++) {
            if(roll[i].getValue() == 6) {
                numSix++;
            }
        }
        if(numSix >= 3) {
            diceInSol[0] = new Dice(6);
            diceInSol[1] = new Dice(6);
            diceInSol[2] = new Dice(6);
            points = 600;
            name = "Three 6's";
            size = 3;
            return true;
        }
        return false;
    }

    /**
     * Check for 3 5's
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckthreeFive(Dice [] roll) {
        int numFive = 0;
        for(int i = 0; i < roll.length; i++) {
            if(roll[i].getValue() == 5) {
                numFive++;
            }
        }
        if(numFive >= 3) {
            diceInSol[0] = new Dice(5);
            diceInSol[1] = new Dice(5);
            diceInSol[2] = new Dice(5);
            points = 500;
            name = "Three 5's";
            size = 3;
            return true;
        }
        return false;
    }

    /**
     * Check for 3 4's
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckthreeFour(Dice [] roll) {
        int numFour = 0;
        for(int i = 0; i < roll.length; i++) {
            if(roll[i].getValue() == 4) {
                numFour++;
            }
        }
        if(numFour >= 3) {
            diceInSol[0] = new Dice(4);
            diceInSol[1] = new Dice(4);
            diceInSol[2] = new Dice(4);
            points = 400;
            name = "Three 4's";
            size = 3;
            return true;
        }
        return false;
    }

    /**
     * Check for 3 3's
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckthreeThree(Dice [] roll) {
        int numThree = 0;
        for(int i = 0; i < roll.length; i++) {
            if(roll[i].getValue() == 3) {
                numThree++;
            }
        }
        if(numThree >= 3) {
            diceInSol[0] = new Dice(3);
            diceInSol[1] = new Dice(3);
            diceInSol[2] = new Dice(3);
            points = 300;
            name = "Three 3's";
            size = 3;
            return true;
        }
        return false;
    }

    /**
     * Check for 3 2's
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckthreeTwo(Dice [] roll) {
        int numTwo = 0;
        for(int i = 0; i < roll.length; i++) {
            if(roll[i].getValue() == 2) {
                numTwo++;
            }
        }
        if(numTwo >= 3) {
            diceInSol[0] = new Dice(2);
            diceInSol[1] = new Dice(2);
            diceInSol[2] = new Dice(2);
            points = 200;
            name = "Three 2's";
            size = 3;
            return true;
        }
        return false;
    }

    /**
     * Check for 3 1's
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckthreeOne(Dice [] roll) {
        int numOne = 0;
        for(int i = 0; i < roll.length; i++) {
            if(roll[i].getValue() == 1) {
                numOne++;
            }
        }
        if(numOne >= 3) {
            diceInSol[0] = new Dice(1);
            diceInSol[1] = new Dice(1);
            diceInSol[2] = new Dice(1);
            points = 1000;
            name = "Three 1's";
            size = 3;
            return true;
        }
        return false;
    }

    /**
     * Check for a 1
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckForOne (Dice [] roll) {
        for(int i = 0; i < roll.length; i++) {
            if (roll[i].getValue() == 1) {
                diceInSol[0] = new Dice(1);
                points += 100;
                name = "1";
                size = 1;
                return true;
            }
        }
        return false;
    }

    /**
     * Check for a 5
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean CheckForFive (Dice [] roll) {
        for(int i = 0; i < roll.length; i++) {
            if (roll[i].getValue() == 5) {
                diceInSol[0] = new Dice(5);
                points += 50;
                name = "5";
                size = 1;
                return true;
            }
        }
        return false;
    }

    /**
     * Check for three pairs
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean threePairs(Dice [] roll) {
        int [] count = new int[7];
        for(Dice num : roll) {
            if(num.getValue() != 0) {
                count[num.getValue()]++;
            }
        }
        int numPairs = 0;
        for(int i = 0; i < count.length; i++) {
            if(count[i] == 2) {
                numPairs++;
            }
        }
        int tempIndex = 0;
        if(numPairs == 3) {
            for(int i = 0; i < count.length; i++) {
                if(count[i] != 0) {
                    diceInSol[tempIndex] = new Dice(i);
                    diceInSol[tempIndex = tempIndex + 1] = new Dice(i);
                    tempIndex++;
                }
            }
            points += 1500;
            name = "3 Pairs";
            size = 6;
            return true;
        }
       return false;
    }

    /**
     * Checks for four of a kind and a pair
     * @param roll the dice roll
     * @return true if this roll has the solution, false otherwise
     */
    public boolean fourKindAndPair(Dice [] roll) {
        int [] count = new int[7];
        for(Dice num : roll) {
            if(num.getValue() != 0) {
                count[num.getValue()]++;
            }
        }
        int numPairs = 0;
        int numFour = 0;
        for(int i = 0; i < count.length; i++) {
            if(count[i] == 2) {
                numPairs++;
            } else if(count[i] == 4) {
                numFour++;
            }
        }
        int tempIndex = 0;
        if(numPairs == 1 && numFour == 1) {
            for(int i = 0; i < count.length; i++) {
                if(count[i] != 0) {
                    if(count[i] == 2) {
                        diceInSol[tempIndex] = new Dice(i);
                        diceInSol[tempIndex = tempIndex + 1] = new Dice(i);
                        tempIndex++;
                    } else if(count[i] == 4) {
                        diceInSol[tempIndex] = new Dice(i);
                        diceInSol[tempIndex = tempIndex + 1] = new Dice(i);
                        diceInSol[tempIndex = tempIndex + 1] = new Dice(i);
                        diceInSol[tempIndex = tempIndex + 1] = new Dice(i);
                        tempIndex++;
                    }
                }
            }
            points += 1500;
            name = "Four of a kind and a pair";
            size = 6;
            return true;
        }
        return false;
    }


}



