package farkleGUI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This is the aiAgent. It creates the a tree for all of the possible solutions
 * and picks the leaf node with the highest EV
 * 
 * @author John Stromberg
 */
public class aiAgent {

	/**
	 * An array that stores the expected values of rolling with i number of dice
	 */
	private double[] expectedValueRoll;

	/**
	 * This is the root of the tree for one roll of a dice
	 */
	private Node root;

	/**
	 * This is the game that we are going to be looking at
	 */
	private Farkle game;

	/**
	 * This is our EV that stays between rolls Ex. Dice we have already picked that
	 * we need our EV to account for
	 */
	private double EV;

	/**
	 * This is the path to get to the node with the highest EV
	 */
	private ArrayList<Integer> path;

	/**
	 * A copy of the node that has the highest EV
	 */
	private Node highestEV;

	public aiAgent(Farkle game) {
		expectedValueRoll = new double[7];
		this.game = game;
		EV = 0.0;
		path = new ArrayList<Integer>();
		highestEV = new Node();
		calcRollEV();
		// evAlgorithm();
	}

	/**
	 * Calculates the ev of rolling n number of dice
	 */
	public void calcRollEV() {
		expectedValueRoll[0] = 0; // never used
		expectedValueRoll[1] = Math.round(((1.0 / 6.0) * 50.0 + (1.0 / 6.0) * 100.0)); // =25
		expectedValueRoll[2] = (200.0 * (1.0 / 36.0)) + (150.0 * (2.0 / 36.0)) + (100.0 * (1.0 / 36.0))
				+ ((8 / 36.0) * (50.0 + expectedValueRoll[1])) + ((8 / 36.0) * (100.0 + expectedValueRoll[1])); // =~61.1111
		expectedValueRoll[3] = Math
				.round((1.0 / 216 * 1000.0) + (1.0 / 216 * 200.0) + (1.0 / 216 * 300.0) + (1.0 / 216 * 400.0)
						+ (1.0 / 216 * 500.0) + (1.0 / 216 * 600.0) + (1.0 / 216 * 300.0) + (3.0 / 216 * 250.0)
						+ (3.0 / 216 * 200.0) + (29.0 / 216 * 150.0) + (12.0 / 216 * (50.0 + expectedValueRoll[2]))
						+ (12.0 / 216 * (100.0 + expectedValueRoll[2])) + (30.0 / 216 * (200.0 + expectedValueRoll[1]))
						+ (60.0 / 216 * (150.0 + expectedValueRoll[1])) + (30.0 / 216 * (100.0 + expectedValueRoll[1])))
				+ 50; // =154
		expectedValueRoll[4] = (8.0 / 1296 * (1000.0 + 25)) + (8.0 / 1296 * (200.0 + 25)) + (8.0 / 1296 * (300.0 + 25))
				+ (8.0 / 1296 * (400.0 + 25)) + (8.0 / 1296 * (500.0 + 25)) + (8.0 / 1296 * (600.0 + 25))
				+ (1.0 / 1296 * (1000.0 + 100)) + (1.0 / 1296 * (1000.0 + 50)) + (1.0 / 1296 * (200.0 + 100))
				+ (1.0 / 1296 * (200.0 + 50)) + (1.0 / 1296 * (300.0 + 100)) + (1.0 / 1296 * (300.0 + 50))
				+ (1.0 / 1296 * (400.0 + 100)) + (1.0 / 1296 * (400.0 + 50)) + (1.0 / 1296 * (500.0 + 100))
				+ (1.0 / 1296 * (500.0 + 50)) + (1.0 / 1296 * (600.0 + 100)) + (1.0 / 1296 * (600.0 + 50))
				+ (1.0 / 1296 * 400) + (4.0 / 1296 * 350) + (6.0 / 1296 * 300) + (4.0 / 1296 * 250) + (1.0 / 1296 * 200)
				+ (212.0 / 1296 * (50 + 154)) + (212.0 / 1296 * (100 + 154)) + (71.0 / 1296 * (200 + 62))
				+ (71.0 / 1296 * (150.0 + 62)) + (71.0 / 1296 * (100 + 62)) + (36.0 / 1296 * (300 + 25))
				+ (70.0 / 1296 * (250 + 25)) + (70.0 / 1296 * (300 + 25)) + (36.0 / 1296 * (150 + 25)) + 127; //=~351
		expectedValueRoll[5] = 351; // I realized that the EV from doing the math is way too low, I needed to increase it for the AI to have any chance of winning.
		expectedValueRoll[6] = 5000; // EV is sort of irrelevant since its only a 2% chance to farkle

		//System.out.println(Arrays.toString(expectedValueRoll));
		// endGameStrat();
	}

	/**
	 * An attempt at making an end game strategy It activates when a player is above
	 * 9000 points
	 */
	public void endGameStrat() {
		int highestScore = 0;
		for (int i = 0; i < game.getPlayerNum(); i++) {
			if (game.getPlayerPoints()[i] > highestScore && i != game.getPlayerNum()) {
				highestScore = game.getPlayerPoints()[i];
			}
		}
		if (highestScore >= 9000) {
			int scoreToGet = highestScore - game.getPlayerPoints()[game.getPlayer()];
			if (scoreToGet > 0) {
				System.out.println("Doing end game strat with a point difference of " + scoreToGet);
				expectedValueRoll[1] = 1500;
				expectedValueRoll[2] = scoreToGet;
				expectedValueRoll[3] = scoreToGet;
				expectedValueRoll[4] = scoreToGet + 400;
				expectedValueRoll[5] = scoreToGet + 400;
			}

		}
	}

	/**
	 * This is the method where the ai doesn't stop playing until they have either
	 * banked or farkled
	 */
	public int AIPlays() {
		if (game.isFirstTime()) {
			if(game.getNumDiceOnBoard() == 6) {
				System.out.println("Player " + game.getPlayer() + " (AI)'s turn!");
			}
			for (int i = 0; i < game.getRoll().length; i++) {
				System.out.print(game.getRoll()[i].getValue() + " ");
			}
			System.out.println();
			constructTree();
			System.out.println(root);
			List<Node> list = findLeaf();
			highestEV = findHighestLeafEV(list);
			path = highestEV.getPath();
		}
		if (game.isFirstTime() && highestEV.getName().equals("root")) { // Game must have farkled
			System.out.println("AI Farkled, switching players!");
			System.out.println("-------------------------------\n");
			game.switchPlayer();
			return -1;
		}
		int index = path.get(0); // The first index in the path
		path.remove(0);
		// If the AI can win right now, it will just bank instead of risking the roll
		if (!game.isFirstTime()
				&& game.getPlayerPoints()[game.getPlayer()] + game.getPlayerRoundPoints()[game.getPlayer()] >= 10000) {
			index = game.findIndex("Bank");
		}

		String temp = game.getPossibleSolutions().get(index).getName();
		if (!temp.equals("Bank") && !temp.equals("Roll") && !temp.equals("Hot Dice (Roll all of the dice again!)")) {
			EV += game.getPossibleSolutions().get(index).getPoints();
		}
		if (temp.equals("Hot Dice (Roll all of the dice again!)")) {
			expectedValueRoll[5] = expectedValueRoll[5] + EV;
		}
		game.addDiceToHand(index);
		int playerNum = game.checkIfWon();
		if (playerNum != -1) {
			return 1; // Player must have won
		} else {
			if (temp.equals("Bank")) {
				System.out.println("-------------------------------");
				System.out.println();
			}
			return 0; // Normal output
		}
	}

	/**
	 * This is the method that the program can call to construct the tree This
	 * method calls the recursiveTreeMaker to actually make the tree
	 */
	public void constructTree() {
		ArrayList<Integer> path = new ArrayList<Integer>();
		root = new Node(EV, null, game.getRoll(), game.getPossibleSolutions(), game.getNumDiceOnBoard(), "root", path);
		recursiveTreeMaker(root);
	}

	/**
	 * This is the recursive methnod that makes a tree
	 * 
	 * @param node the node that you are adding children to
	 */
	public void recursiveTreeMaker(Node node) {
		List<Node> children = new ArrayList<Node>();
		double ev = node.getEV();
		if (!node.getName().equals("Bank") && !node.getName().equals("Roll")
				&& !node.getName().equals("Hot Dice (Roll all of the dice again!)")) { // We don't need to make any
																						// child nodes if this is true
			for (int i = 0; i < node.getPossibleSolutions().size(); i++) { // Loop through all of its solutions
				Solution currentSol = node.getPossibleSolutions().get(i);
				int solSize = node.getNumDice() - currentSol.getSize();
				if (currentSol.getName().equals("Roll")
						|| currentSol.getName().equals("Hot Dice (Roll all of the dice again!)")) { // Assigns
																									// appropriate EV's
					if (solSize == 0) {
						ev = node.getEV() + expectedValueRoll[6];
					} else {
						ev = expectedValueRoll[solSize]; // Finds the precalculated ev for rolling i
						// number of dice
					}
					ArrayList<Integer> path = (ArrayList<Integer>) node.getPath().clone();
					path.add(i);
					children.add(new Node(ev, null, node.getDice(), null, solSize, currentSol.getName(), path));
				} else if (currentSol.getName().equals("Bank")) {
					ev = node.getEV(); // your current round points
					ArrayList<Integer> path = (ArrayList<Integer>) node.getPath().clone();
					path.add(i);
					children.add(new Node(ev, null, node.getDice(), null, solSize, currentSol.getName(), path));
				} else {
					ev = currentSol.getPoints() + node.getEV(); // The points for that solution your current round
																// points
					Dice[] temp = node.copyDice();
					Farkle tempGame = findNewRoll(temp, i);
					ArrayList<Integer> path = (ArrayList<Integer>) node.getPath().clone();
					path.add(i);
					children.add(new Node(ev, null, tempGame.getRoll(), tempGame.getPossibleSolutions(), solSize,
							currentSol.getName(), path));
				}
			}
		}
		node.updateChildren(children); // Sets the children attribute of the node
		for (int i = 0; i < children.size(); i++) { // The for loop to through the children to call the recursive method
			recursiveTreeMaker(children.get(i));
		}
	}

	/**
	 * This finds out what the new dice roll is by taking the starting dice of the
	 * node and removing the dice in the respective solution
	 * 
	 * @param roll The starting roll
	 * @param i    the index of the solution to remove from the dice
	 * @return the new game
	 */
	public Farkle findNewRoll(Dice[] roll, int i) {
		Farkle tempGame = new Farkle(2, 0);
		tempGame.setFirstTime(false);
		tempGame.setRoll(roll);
		tempGame.getPossibleSolutions().clear();
		tempGame.checkSolutions();
		tempGame.addDiceToHand(i);
		tempGame.setFirstTime(true);
		return tempGame;
	}

	/**
	 * Used to call the recursive find leaf method
	 * 
	 * @return a list of nodes containing the leaf nodes
	 */
	public List<Node> findLeaf() {
		List<Node> list = new ArrayList<Node>();
		return recursiveFindLeaf(root, list);
	}

	/**
	 * Looks through the nodes until it finds a node without any children Then it
	 * adds that node to a list
	 * 
	 * @param node the node to look at
	 * @param list full of leaf nodes
	 * @return a list with all of the children in it
	 */
	public List<Node> recursiveFindLeaf(Node node, List<Node> list) {
		if (node.getChildren().size() == 0) {
			list.add(node);
		} else {
			for (int i = 0; i < node.getChildren().size(); i++) {
				recursiveFindLeaf(node.getChildren().get(i), list);
			}
		}
		return list;
	}

	/**
	 * Find the highest EV out of the leaf nodes
	 * 
	 * @param list the list of nodes to look through
	 * @return the node with the highest EV
	 */
	public Node findHighestLeafEV(List<Node> list) {
		double highestEV = Integer.MIN_VALUE;
		Node holder = new Node();
		for (int i = 0; i < list.size(); i++) { // Finds the highest EV out of the array
			if (list.get(i).getEV() > highestEV) {
				highestEV = list.get(i).getEV();
				holder = list.get(i);
			}
		}
		System.out.println("Picking node: Name: " + holder.getName() + " , EV: " + holder.getEV());
		System.out.println("Path to node: " + holder.getPath() + "\n");
		return holder;
	}

	/**
	 * This method is used to calculate the different combinations of rolling n
	 * number of dice when all of those n dice are either a 1 or 5.
	 */
//	public void evAlgorithm() {
//		int[] result = new int[10];
//		int index = 0;
//		int tempi = 0;
//		int tempj = 0;
//		int tempn = 0;
//		for (int i = 0; i < 2; i++) {
//			if (i == 0) {
//				tempi = 100;
//			} else {
//				tempi = 50;
//			}
//			for (int j = 0; j < 2; j++) {
//				if (j == 0) {
//					tempj = 100;
//				} else {
//					tempj = 50;
//				}
//				for (int n = 0; n < 2; n++) {
//					if (n == 0) {
//						tempn = 100;
//					} else {
//						tempn = 50;
//					}
//					result[index] = tempi + tempj + tempn;
//					index++;
//				}
//
//			}
//		}
//		for (int i = 0; i < result.length; i++) {
//			System.out.println(result[i]);
//		}
//	}

}
