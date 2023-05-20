package farkleGUI;

import java.util.LinkedList;

/**
 * The class with the game logic
 * @author John Stromberg
 */
public class Farkle {

    /**
     * The current roll, 0's represent blanks
     */
    private Dice [] roll;

    /**
     * Your current hand, 0's represent blanks
     */
    private Dice [] hand;

    /**
     * The number of dice that you have left in your roll.
     * This is the number that you will roll if you choose to roll again
     */
    private int numDiceOnBoard;

    /**
     * One based indexing on the player number
     */
    private int player;

    /**
     * A boolean array that stores true if that player is an AI or false if they are a human
     */
    private boolean [] isAI;

    /**
     * The number of players playing the game
     */
    private int playerNum;

    /**
     * A List of all possible solutions for a given roll. This is updated everytime you take dice to your hand.
     */
    private LinkedList<Solution> possibleSolutions;

    /**
     * Number of points for player each player. The index is the player number
     */
    private int [] playerPoints;

    /**
     * Number of points for player each player. The index is the player number. You can lose these points if you farkle
     */
    private int [] playerRoundPoints;

    /**
     * A flag for if you farkled
     */
    private boolean didFarkle;

    /**
     * True if you are rolling the dice
     * False if you are taking dice into your hand
     * It is for not triggering a Farkle is you are just taking dice into your hand
     */
    private boolean firstTime;

    /**
     * Constructor for a new game
     */
    public Farkle(int numPlayers, int numAI) {
        roll = new Dice[6];
        hand = new Dice[6];
        numDiceOnBoard = 6;
        possibleSolutions = new LinkedList<Solution>();
        player = 1;
        playerNum = numPlayers + numAI;
        playerPoints = new int[numPlayers+ numAI + 1];
        playerRoundPoints = new int[numPlayers+ numAI + 1];
        didFarkle = false;
        firstTime = true;
        isAI = new boolean[playerNum+1];
        for(int i = 1; i <= playerNum; i++) {
            if(i > playerNum-numAI) {
                isAI[i] = true;
            } else {
                isAI[i] = false;
            }
        }
        for(int i = 0; i < hand.length; i++) {
            hand[i] = new Dice(0);
        }
        rollDice();
    }

    public boolean[] getIsAI() {
        return isAI;
    }

    /**
     * A getter for if it is the first time
     * @return firstTime
     */
    public boolean isFirstTime() {
        return firstTime;
    }
    
    /**
     * Sets the first time
     * This is used by the AI
     * @param firstTime if it is the first time or not
     */
    public void setFirstTime(boolean firstTime) {
    	this.firstTime = firstTime;
    }

    /**
     * A getter for the player points
     * @return the array of round points
     */
    public int [] getPlayerPoints() {
        return playerPoints;
    }

    /**
     * A getter for the array of round points
     * @return the array of round points
     */
    public int [] getPlayerRoundPoints() {
        return playerRoundPoints;
    }

    /**
     * A getter for the roll
     * @return the current roll
     */
    public Dice [] getRoll() {
        return roll;
    }

    /**
     * Gets the number of players in the game
     * @return the number of players in the game
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * A setter for the new roll
     * @param roll your new roll
     */
    public void setRoll(Dice[] roll) {
        possibleSolutions.clear();
        this.roll = roll;
        checkSolutions();
    }

    /**
     * Getter for the current hand
     * @return the current hand
     */
    public Dice [] getHand() {
        return hand;
    }

    /**
     * Getter for number of dice of the board
     * @return num dice on the board
     */
    public int getNumDiceOnBoard() {
        return numDiceOnBoard;
    }

    /**
     * Setter for num dice
     * @param numDice new number of dice
     */
    public void setNumDiceOnBoard(int numDice) {
        this.numDiceOnBoard = numDice;
    }

    /**
     * Getter for the player
     * @return the player
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Getter for did farkle
     * @return didFarkle
     */
    public boolean isDidFarkle() {
        return didFarkle;
    }

    /**
     * Getter for list of possible solutions
     * @return list of possible solutions
     */
    public LinkedList<Solution> getPossibleSolutions() {
        return possibleSolutions;
    }

    /**
     * Clears the hand back to 0's
     */
    public void clearHand() {
        for(int i = 0; i < hand.length; i++) {
            hand[i] = new Dice(0);
        }
    }

    /**
     * When a player is done with there turn, all of this must be done.
     * If the player farkled, they lose there round points
     */
    public void switchPlayer() {
        if(!didFarkle) {
            playerPoints[player] += playerRoundPoints[player];
        }
        playerRoundPoints[player] = 0;
        if(player != playerNum) {
            player++;
        } else {
            player = 1;
        }
        numDiceOnBoard = 6;
        firstTime = true;
        didFarkle = false;
        possibleSolutions.clear();
        clearHand();
        rollDice();

    }

    /**
     * Rolls the dice with a limit on only the num dice on the board
     */
    public void rollDice() {
        for(int i = 0; i < numDiceOnBoard; i++) {
            roll[i] = new Dice();
        }
        firstTime = true;
        checkSolutions();
    }

    /**
     * A method that checks if the dice are all empty (all zeros)
     * @param roll the dice you want to check
     * @return return true if they are all 0, false otherwise
     */
    public boolean allZeroDice(Dice [] roll) {
        int numZero = 0;
        for(int i = 0; i < roll.length; i++) {
            if(roll[i].getValue() == 0) {
                numZero++;
            }
        }
        if(numZero == roll.length) {
            return true;
        }
        return false;
    }


    /**
     * After the user picks which solution to take, add that to the hand and remove it from the roll
     * If it is a special case (roll, bank, hot dice), we do that condition and return
     * @param index
     * @return
     */
    public void addDiceToHand(int index) {
        firstTime = false;
        Solution temp = new Solution();
        temp = possibleSolutions.get(index);
        int tempIndex = 0;
        //view view = new view();

        //Special cases for roll and bank and hot dice
        if(possibleSolutions.get(index).getName().equals("Roll")) {
            //Since it is our "first roll", the user can't bank or roll
            possibleSolutions.clear();
            rollDice();
            //if(isDidFarkle()) {
            //    toString();
            //}
            return;
        } else if(possibleSolutions.get(index).getName().equals("Bank")) {
            switchPlayer();
            //view.printBankingScore();
            return;
        } else if(possibleSolutions.get(index).getName().equals("Hot Dice (Roll all of the dice again!)")) {
            //view.printHotDice();
            numDiceOnBoard = 6;
            possibleSolutions.clear();
            clearHand();
            rollDice();
            return;
        }

        // Moves the solution to the hand
        for(int i = 0; i < hand.length; i++) {
            if(hand[i].getValue() == 0) {
                for(int j = i; j < (i + temp.getSize()); j++) {
                    hand[j] = possibleSolutions.get(index).getDiceInSol()[tempIndex];
                    tempIndex++;
                }
                break;
            }
        }
        //Replaces the solution with 0's
        int size = possibleSolutions.get(index).getSize();
        for(int i = 0; i < roll.length; i++) {
            for(int j = 0; j < possibleSolutions.get(index).getSize(); j++) {
                if(roll[i].getValue() == possibleSolutions.get(index).getDiceInSol()[j].getValue()) {
                    if(size != 0) {
                        roll[i] = new Dice(0);
                        size--;
                        break;
                    }
                }
            }
        }
        //Moves the 0's to the end of the array
        Dice [] newRoll = new Dice[6];
        for(int i = 0; i < roll.length; i++) {
            newRoll[i] = new Dice(0);
        }
        tempIndex = 0;
        for(int i = 0; i < roll.length; i++) {
            if(roll[i].getValue() != 0) {
                newRoll[tempIndex] = roll[i];
                tempIndex++;
            }
        }
        roll = newRoll;
        //Gives out the points for that solution
        playerRoundPoints[player] += possibleSolutions.get(index).getPoints();
        numDiceOnBoard -= possibleSolutions.get(index).getSize();
        //Clears out the solutions and recalculates that solutions of the remaining dice, note that they can't farkle since it isn't a new roll.
        possibleSolutions.clear();
        checkSolutions();
    }

    /**
     * This method goes through and checks all of the solutions, if there is a match, add it to the list of possible solutions
     */
    public void checkSolutions() {
        //Special case for hot dice
        boolean hotdice = false; //If this is true, we don't add the normal roll to the linked list
        Solution hotDice = new Solution(0, 0, "Hot Dice (Roll all of the dice again!)");
        if(numDiceOnBoard == 0) {
            possibleSolutions.add(hotDice);
            hotdice = true;
        }

        Solution rullRun = new Solution();
        if(rullRun.CheckfullRun(roll)) {
            possibleSolutions.add(rullRun);
        }
        Solution three6 = new Solution();
        if(three6.CheckthreeSix(roll)) {
            possibleSolutions.add(three6);
        }
        Solution three5 = new Solution();
        if(three5.CheckthreeFive(roll)) {
            possibleSolutions.add(three5);
        }
        Solution three4 = new Solution();
        if(three4.CheckthreeFour(roll)) {
            possibleSolutions.add(three4);
        }
        Solution three3 = new Solution();
        if(three3.CheckthreeThree(roll)) {
            possibleSolutions.add(three3);
        }
        Solution three2 = new Solution();
        if(three2.CheckthreeTwo(roll)) {
            possibleSolutions.add(three2);
        }
        Solution three1 = new Solution();
        if(three1.CheckthreeOne(roll)) {
            possibleSolutions.add(three1);
        }
        Solution one = new Solution();
        if(one.CheckForOne(roll)) {
            possibleSolutions.add(one);
        }
        Solution five = new Solution();
        if(five.CheckForFive(roll)) {
            possibleSolutions.add(five);
        }
        Solution threePairs = new Solution();
        if(threePairs.threePairs(roll)) {
            possibleSolutions.add(threePairs);
        }
        Solution fourOfAKind = new Solution();
        if(fourOfAKind.fourKindAndPair(roll)) {
            possibleSolutions.add(fourOfAKind);
        }

        //If it isn't a roll, then you can roll and bank
        if(!firstTime) {
            if(!hotdice) {
                Solution roll = new Solution(0, 0, "Roll");
                possibleSolutions.add(roll);
            }
            Solution bank = new Solution(0, 0, "Bank");
            possibleSolutions.add(bank);
        }
        //Checks for a farkle
        if(possibleSolutions.size() == 0 && firstTime == true) {
            didFarkle = true;
        }
    }

    /**
     * This method goes through and checks all of the possible solutions against the selected dice
     */
    public int checkSelectedDice(Dice [] array) {
        for (int i = 0; i < possibleSolutions.size(); i++) { //Look through all solutions
            boolean doesNotMatch = false;
            int numDice = possibleSolutions.get(i).getSize(); //The number of dice in the solution
            for (int j = 0; j < array.length; j++) { //Go through the array
                if (array[j].getValue() != 0) {
                    if (array[j].getValue() == possibleSolutions.get(i).getDiceInSol()[j].getValue()) { //See if the value in the array is the same as the solution
                        numDice--; //Decrement the numDice left
                    } else {
                        doesNotMatch = true;
                        continue;
                    }
                }
            }
            if (numDice == 0 && !doesNotMatch) { //If they match. return the index
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Checks if the a player has won
     * @return returns the index of the player, -1 if no player has won
     */
    public int checkIfWon() {
        for(int i = 1; i <= playerNum; i++) {
            if(playerPoints[i] >= 10000) {
                return i;
            }
        }
        return -1;
    }
    
	/**
	 * A method to find the index of a solution Used when it find the solution if
	 * the user clicks the roll or bank button
	 * 
	 * @param name the name of the solution
	 * @return the index of the solution
	 */
	public int findIndex(String name) {
		for (int i = 0; i < getPossibleSolutions().size(); i++) {
			if (getPossibleSolutions().get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

}
