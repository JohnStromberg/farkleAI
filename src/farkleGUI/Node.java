package farkleGUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A class for a tree
 * @author John Stromberg
 *
 */
public class Node {
	
	/**
	 * The EV of a node
	 */
	private double EV;

	/**
	 * The node's children
	 */
	private List<Node> children;

	/**
	 * The dice that are in that solution
	 */
	private Dice[] dice;

	/**
	 * The linkedlist of possible solutions for this dice
	 * This is used to create the children
	 */
	private LinkedList<Solution> possibleSolution;

	/**
	 * The number of dice remaining that you can roll
	 */
	private int numDice;
	
	/**
	 * The name of the solution
	 * Used to see if its a roll or bank action
	 */
	private String name;
	
	/**
	 * The path from the root
	 */
	private ArrayList<Integer> path;

	public Node(double ev, List<Node> children, Dice[] dice, LinkedList<Solution> possibleSolutions, int numDice, String name, ArrayList<Integer> path) {
		this.EV = ev;
		this.children = children;
		this.dice = dice;
		this.possibleSolution = possibleSolutions;
		this.numDice = numDice;
		this.name = name;
		this.path = path;
	}
	
	public Node() {
		EV = 0;
		children = null;
		dice = null;
		possibleSolution = null;
		numDice = 0;
		name = null;
	}
	
	/**
	 * A getter for the path
	 * @return the path
	 */
	public ArrayList<Integer> getPath() {
		return path;
	}

	/**
	 * Sets the children of a node
	 * @param tree the node that you want to change the children of
	 */
	public void updateChildren(List<Node> tree) {
		this.children = tree;
	}

	/**
	 * Getter for the name
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for the number of dice
	 * @return the number of dice
	 */
	public int getNumDice() {
		return numDice;
	}

	/**
	 * Getter for the EV
	 * @return the EV
	 */
	public double getEV() {
		return EV;
	}

	/**
	 * Getter for the children of a node
	 * @return the children of a node
	 */
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * Getter for the dice
	 * @return the dice in a solution
	 */
	public Dice[] getDice() {
		return dice;
	}

	/**
	 * Getter for the linked list of possible solutions
	 * @return the possible solution
	 */
	public LinkedList<Solution> getPossibleSolutions() {
		return possibleSolution;
	}

	/**
	 * Creates a deep copy of the dice
	 * @return A new dice array that is a deep copy of this.dice
	 */
	public Dice [] copyDice() {
		Dice [] newArray = new Dice [this.dice.length];
		for(int i = 0; i < this.dice.length; i++) {
			newArray[i] = new Dice(this.dice[i].getValue());
		}
		return newArray;
	}
	
	public String toString() {
	   StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

	/**
	 * Used to print the tree
	 * @param buffer the string that you are building to return
	 * @param prefix A place to add the dashes
	 * @param childrenPrefix a place to add the vertical lines
	 */
	private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append("Name: " + name + " , EV: " + EV);
        buffer.append('\n');
        for (Iterator<Node> it = children.iterator(); it.hasNext();) {
            Node next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "|-- ", childrenPrefix + "|   ");
            } else {
                next.print(buffer, childrenPrefix + "|-- ", childrenPrefix + "    ");
            }
        }
    }
}
