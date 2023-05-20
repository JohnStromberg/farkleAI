package farkleGUI;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import java.util.Arrays;

/**
 * The GUI class for Farkle
 * 
 * @author John Stromberg
 *
 */
public class viewGUI extends Application {

	/**
	 * The current game
	 */
	private Farkle farkle;

	/**
	 * A text box that displays a lot of little info like rolling dice, hot dice,
	 * and AI playing
	 */
	private Text gameStatText;

	/**
	 * The text for the current scores
	 */
	private Text currScores;

	/**
	 * The text to display whose turn it is
	 */
	private Text playerNum;

	/**
	 * Array of the displays 1 based indexing for the roll
	 */
	private ToggleButton[] diceToDisplayRoll;

	/**
	 * Array of the displays 1 based indexing for the hand
	 */
	private ImageView[] diceToDisplayHand;

	/**
	 * Array of dices images. 1 based indexing.
	 */
	private Image[] dice;

	/**
	 * The textfield for the solutions
	 */
	private Text solField;

	/**
	 * The text for the roundScore
	 */
	private Text roundScore;

	/**
	 * Button to add dice to your hand
	 */
	private Button addHandButton;

	/**
	 * Button to roll the dice
	 */
	private Button rollButton;

	/**
	 * A button to bank your score
	 */
	private Button bankButton;

	/**
	 * A button to switch players after a farkle
	 */
	private Button switchButton;

	/**
	 * The AI agent that will play
	 */
	private aiAgent AI;

	/**
	 * A gridpane for the top part of the gui
	 */
	private GridPane topGrid;

	/**
	 * This is used to score current scores and the game stat text field
	 */
	private GridPane middleGrid;

	/**
	 * The is used to display the roll dice images
	 */
	private GridPane rollGrid;

	/**
	 * This is used to display the dice images in your hand
	 */
	private GridPane handGrid;

	/**
	 * This is used to display the buttons and there respective actions
	 */
	private GridPane buttonGrid;

	/**
	 * This is used to display the current possible solutions
	 */
	private GridPane solGrid;

	@Override
	public void start(Stage primaryStage) {
		try {
			// master grid
			GridPane grid = new GridPane();
			Scene scene = new Scene(grid, 1100, 650);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Farkle");

			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();
			primaryStage.setX(bounds.getMinX());
			primaryStage.setY(bounds.getMinY());
			primaryStage.setWidth(bounds.getWidth());
			primaryStage.setHeight(bounds.getHeight());

			grid.setHgap(10);
			grid.setVgap(10);

			// Grid for the elements on the top
			topGrid = new GridPane();
			topGrid.setHgap(10);
			topGrid.setVgap(10);

			createTopPane();
			grid.add(topGrid, 0, 0, 1, 1);

			// Grid for the curr scores and the rolling dice text
			middleGrid = new GridPane();
			middleGrid.setHgap(10);
			middleGrid.setVgap(10);
			createMiddleGrid();
			grid.add(middleGrid, 0, 1, 1, 1);

			// Grid for the dice in your roll
			rollGrid = new GridPane();
			rollGrid.setHgap(10);
			rollGrid.setVgap(10);
			createRollGrid();
			grid.add(rollGrid, 0, 2, 1, 1);

			// Grid for the dice in your hand
			handGrid = new GridPane();
			handGrid.setHgap(10);
			handGrid.setVgap(10);
			createHandGrid();
			grid.add(handGrid, 0, 3, 1, 1);

			// grid for the buttons
			buttonGrid = new GridPane();
			buttonGrid.setHgap(10);
			buttonGrid.setVgap(10);
			createButtonGrid();
			grid.add(buttonGrid, 0, 4, 1, 1);

			solGrid = new GridPane();
			solGrid.setHgap(10);
			solGrid.setVgap(10);
			createSolGrid();
			grid.add(solGrid, 0, 5, 1, 1);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Creates the toppane and fills it will buttons and text
	 */
	public void createTopPane() {
		// Grid for the elements on the top
		topGrid.setHgap(10);
		topGrid.setVgap(10);

		// Just says farkle
		Text welcomeText = new Text("Farkle");
		welcomeText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 35));
		topGrid.add(welcomeText, 0, 0, 1, 1);

		Separator separatorNewGame = new Separator();
		separatorNewGame.setOrientation(Orientation.VERTICAL);
		topGrid.add(separatorNewGame, 1, 0, 1, 1);

		Text humantext = new Text("Enter the number of human players: ");
		topGrid.add(humantext, 2, 0, 1, 1);

		// Textfield for the user to enter the number of human players
		TextField numHuman = new TextField();
		numHuman.setMaxWidth(50);
		topGrid.add(numHuman, 3, 0, 1, 1);

		Text aitext = new Text("Enter the number of AI players: ");
		topGrid.add(aitext, 4, 0, 1, 1);

		// Textfield for the user to enter the number of AI players
		TextField numAI = new TextField();
		numAI.setMaxWidth(50);
		topGrid.add(numAI, 5, 0, 1, 1);

		Button humanButton = new Button("Start Game");
		topGrid.add(humanButton, 6, 0, 1, 1);
		humanButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int i = 1; i <= 6; i++) {
					diceToDisplayRoll[i].setSelected(false); // Clears the toggleboxs
				}
				try {
					int humanNum = 0;
					int aiNum = 0;
					if(!numHuman.getText().equals("")) {
						humanNum = Integer.parseInt(numHuman.getText());
					}
					if(!numAI.getText().equals("")) {
						aiNum = Integer.parseInt(numAI.getText());
					}
					if (humanNum < 0 || aiNum < 0) {
						throw new Exception();
					}
					if (humanNum + aiNum < 2) {
						throw new Exception("Too Few");
					}
					farkle = new Farkle(humanNum, aiNum);
					bankButton.setDisable(false);
					rollButton.setDisable(false);
					addHandButton.setDisable(false);
					for (int i = 1; i <= 6; i++) {
						diceToDisplayRoll[i].setDisable(false);
					}
					updateDisplay();
					Alert a = new Alert(Alert.AlertType.NONE);
					a.setAlertType(Alert.AlertType.INFORMATION);
					a.setTitle("Starting Game");
					a.setContentText("Starting game! The first " + humanNum + " player(s) will be humans. The next "
							+ aiNum + " will be AI");
					a.show();
				} catch (Exception e) {
					if (e.getMessage().equals("Too Few")) {
						Alert a = new Alert(Alert.AlertType.NONE);
						a.setAlertType(Alert.AlertType.ERROR);
						a.setTitle("Invalid Input");
						a.setContentText("The number of humans and AI must be greater than 1");
						a.show();
					} else {
						Alert a = new Alert(Alert.AlertType.NONE);
						a.setAlertType(Alert.AlertType.ERROR);
						a.setTitle("Invalid Input");
						a.setContentText(
								"You must enter a number that is greater than or equal to 0, not a letter in both boxes");
						a.show();
					}
				}

			}
		});

		Separator supRules = new Separator();
		supRules.setOrientation(Orientation.VERTICAL);
		topGrid.add(supRules, 7, 0, 1, 1);

		// Static text field
		Text rules = new Text("First player to 10,000 points wins!");
		rules.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		topGrid.add(rules, 8, 0, 1, 1);
	}

	/**
	 * Creates the middlegrid object
	 */
	public void createMiddleGrid() {
		Separator separatorGame = new Separator();
		separatorGame.setOrientation(Orientation.HORIZONTAL);
		separatorGame.setValignment(VPos.CENTER);
		GridPane.setColumnSpan(separatorGame, 100);
		middleGrid.add(separatorGame, 0, 1);

		currScores = new Text("Current Scores: ");
		currScores.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
		currScores.setWrappingWidth(1500);
		middleGrid.add(currScores, 0, 2, 1, 1);

		playerNum = new Text("");
		playerNum.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
		middleGrid.add(playerNum, 0, 3, 1, 1);

		gameStatText = new Text();
		gameStatText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
		middleGrid.add(gameStatText, 0, 4, 1, 1);

	}

	/**
	 * Creates a grid of images for the roll
	 */
	public void createRollGrid() {
		Text rollText = new Text("Roll: ");
		rollText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		rollGrid.add(rollText, 0, 0, 1, 1);

		dice = new Image[7]; // 1 based indexing
		dice[0] = new Image("file:src/farkleGUI/diceImg/blank.png"); // the blank dice
		diceToDisplayRoll = new ToggleButton[7];
		for (int i = 1; i <= 6; i++) {
			dice[i] = new Image("file:src/farkleGUI/diceImg/dice" + i + ".png"); // puts 1-6 in the dice array.
			diceToDisplayRoll[i] = new ToggleButton("", new ImageView(dice[0])); // All of the starting dice are blank
			rollGrid.add(diceToDisplayRoll[i], i, 0, 1, 1);
		}

		Separator diceSep = new Separator();
		diceSep.setOrientation(Orientation.HORIZONTAL);
		diceSep.setValignment(VPos.CENTER);
		GridPane.setColumnSpan(diceSep, 100);
		rollGrid.add(diceSep, 0, 1);
	}

	/**
	 * Creates a grid with the dice in your hand
	 */
	public void createHandGrid() {
		Text handText = new Text("Hand: ");
		handText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		handGrid.add(handText, 0, 0, 1, 1);

		diceToDisplayHand = new ImageView[7];
		for (int i = 1; i <= 6; i++) {
			diceToDisplayHand[i] = new ImageView(dice[0]); // All zero dice to start with
			handGrid.add(diceToDisplayHand[i], i, 0, 1, 1);
		}

		Separator diceSep2 = new Separator();
		diceSep2.setOrientation(Orientation.HORIZONTAL);
		diceSep2.setValignment(VPos.CENTER);
		GridPane.setColumnSpan(diceSep2, 100);
		handGrid.add(diceSep2, 0, 1);
	}

	/**
	 * Creates the button grid to store all of the buttons
	 */
	public void createButtonGrid() {
		addHandButton = new Button("Add dice to hand");
		addHandButton.setDisable(true);
		addHandButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Dice[] temp = getSelectedDice(); // THIS WORKS
				boolean noneSelected = true;
				for (int i = 0; i < temp.length; i++) {
					if (temp[i].getValue() != 0) {
						noneSelected = false;
						continue;
					}
				}
				if (noneSelected == true) {
					Alert a = new Alert(Alert.AlertType.NONE);
					a.setAlertType(Alert.AlertType.ERROR);
					a.setTitle("Invalid Choice");
					a.setContentText("You must select at least one dice");
					a.show();
				} else {
					int index = farkle.checkSelectedDice(temp);
					if (index == -1) { // The user picked invalid dice
						Alert a = new Alert(Alert.AlertType.NONE);
						a.setAlertType(Alert.AlertType.ERROR);
						a.setTitle("Invalid Choice");
						a.setContentText("You must select dice that are in a solution");
						a.show();
					} else {
						farkle.addDiceToHand(index);
					}
					for (int i = 1; i <= 6; i++) {
						diceToDisplayRoll[i].setSelected(false); // Clears the toggleboxs
					}
					updateDisplay();
					if (farkle.getNumDiceOnBoard() == 0) {
						gameStatText.setText("Hot Dice!!!! Click Roll Dice to get a free roll!");
					}
				}
			}
		});
		buttonGrid.add(addHandButton, 0, 0, 1, 1);

		rollButton = new Button("Roll Dice");
		rollButton.setDisable(true);
		buttonGrid.add(rollButton, 1, 0, 1, 1);
		rollButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (farkle.getNumDiceOnBoard() == 0) {
					int index2 = farkle.findIndex("Hot Dice (Roll all of the dice again!)");
					farkle.addDiceToHand(index2);
					updateDisplay();
				} else {
					int index = farkle.findIndex("Roll");
					farkle.addDiceToHand(index);
					updateDisplay();
				}
			}
		});

		bankButton = new Button("Bank Dice");
		bankButton.setDisable(true);
		buttonGrid.add(bankButton, 2, 0, 1, 1);
		bankButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int index = farkle.findIndex("Bank");
				farkle.addDiceToHand(index);
				int playerNum = checkIfWon(); // A player can only win if they bank their score
				if (playerNum != -1) {
					setWinText(playerNum);
				} else {
					updateDisplay();
				}
			}
		});

		// Allows the user to switch players so it doesn't happen instantly
		switchButton = new Button("Switch players (for a farkle)");
		switchButton.setDisable(true);
		buttonGrid.add(switchButton, 3, 0, 1, 1);
		switchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				farkle.switchPlayer();
				updateDisplay();
			}
		});
	}

	/**
	 * Creates the grid to store the solution field
	 */
	public void createSolGrid() {
		roundScore = new Text("Current score for the round: ");
		roundScore.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		solGrid.add(roundScore, 0, 0, 1, 1);

		solGrid.setHgap(10);
		solGrid.setVgap(10);
		solField = new Text("Possible solutions: ");
		solField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		solGrid.add(solField, 0, 1, 1, 1);

	}

	/**
	 * One method to update all of the text fields and images
	 */
	public void updateDisplay() {
		updateCurrScores();
		updateRollDice();
		updateHandDice();
		updateGameStatText();
		updateSolField();
		updateRoundScore();
		updateInputButtons();
		updatePlayerNum();
		setTextAI();
	}

	/**
	 * Updates the text if you roll the dice or not
	 */
	public void updateGameStatText() {
		if (farkle != null && farkle.isFirstTime()) {
			gameStatText.setText("Rolling Dice!!!");
		} else {
			gameStatText.setText("");
		}
	}

	/**
	 * Updates which players turn it is
	 */
	public void updatePlayerNum() {
		if (farkle.getIsAI()[farkle.getPlayer()] == false) { // Checks if they are an AI
			playerNum.setText("Player " + farkle.getPlayer() + "'s turn");
		} else {
			playerNum.setText("Player " + farkle.getPlayer() + "'s (AI) turn");
		}
	}

	/**
	 * Updates the current scores
	 */
	public void updateCurrScores() {
		String returnString = "Current Scores: ";
		if (farkle != null) {
			for (int i = 1; i <= farkle.getPlayerNum(); i++) { // gets all of the scores for each player
				returnString += "Player " + i + ": " + farkle.getPlayerPoints()[i];
				if (i != farkle.getPlayerNum()) {
					returnString += ", ";
				}
			}
			currScores.setText(returnString);
		}
	}

	/**
	 * Updates the dice roll
	 */
	public void updateRollDice() {
		int diceIndex = 1;
		for (int i = 0; i < farkle.getRoll().length; i++) { // gets the current roll
			if (farkle.getRoll()[i].getValue() != 0) {
				diceToDisplayRoll[diceIndex].setGraphic(new ImageView(dice[farkle.getRoll()[i].getValue()]));
				diceIndex++;
			} else {
				diceToDisplayRoll[diceIndex].setGraphic(new ImageView(dice[0]));
				diceIndex++;
			}
		}
	}

	/**
	 * Updates the dice in the hand
	 */
	public void updateHandDice() {
		int diceIndex = 1;
		for (int i = 0; i < farkle.getHand().length; i++) { // gets the current roll
			if (farkle.getHand()[i].getValue() != 0) {
				diceToDisplayHand[diceIndex].setImage(dice[farkle.getHand()[i].getValue()]);
				diceIndex++;
			} else {
				diceToDisplayHand[diceIndex].setImage(dice[0]);
				diceIndex++;
			}
		}

	}

	/**
	 * Gets the solutions and prints them out
	 */
	public void updateSolField() {
		String returnString = "";
		if (!farkle.isDidFarkle()) {
			returnString += "Here are your possible options: \n";

		} else {
			returnString += "Oh no, you Farkled! Click the switch players button to switch players.";
		}
		if (farkle.getPossibleSolutions().size() != 0) { // displays all of the possible scoring options
			Dice[] currDice = new Dice[6];
			for (int i = 0; i < farkle.getPossibleSolutions().size(); i++) {
				if (!farkle.getPossibleSolutions().get(i).getName().equals("Bank")
						&& !farkle.getPossibleSolutions().get(i).getName().equals("Roll")
						&& !farkle.getPossibleSolutions().get(i).getName()
								.equals("Hot Dice (Roll all of the dice again!)")) {
					returnString += "(" + (i + 1) + "): " + "Name: " + farkle.getPossibleSolutions().get(i).getName()
							+ ", points: " + farkle.getPossibleSolutions().get(i).getPoints()
							+ ", dice required for this solution: ";
				} else {
					returnString += "(" + (i + 1) + "): " + "Name: " + farkle.getPossibleSolutions().get(i).getName();
				}
				if (farkle.getPossibleSolutions().get(i).getName().equals("Hot Dice (Roll all of the dice again!)")) {
					returnString += " (Hit roll again to use your hot dice)";
				}
				currDice = farkle.getPossibleSolutions().get(i).getDiceInSol();
				for (int j = 0; j < currDice.length; j++) { // gets the dice needed for the solution
					if (currDice[j].getValue() != 0) {
						returnString += currDice[j].getValue() + " ";
					}
				}
				returnString += "\n";
			}

		}
		solField.setText(returnString);
	}

	/**
	 * Updates the round score
	 */
	public void updateRoundScore() {
		roundScore.setText("Current score for the round: " + farkle.getPlayerRoundPoints()[farkle.getPlayer()]);
	}

	/**
	 * Turns the bank and roll buttons off when it is a your first roll Also turns
	 * off the buttons if you farkle
	 */
	public void updateInputButtons() {
		if (farkle.isFirstTime()) {
			bankButton.setDisable(true);
			rollButton.setDisable(true);
		} else {
			bankButton.setDisable(false);
			rollButton.setDisable(false);
		}
		if (!farkle.isDidFarkle()) {
			switchButton.setDisable(true);
			addHandButton.setDisable(false);
		} else if (farkle.isDidFarkle()) {
			switchButton.setDisable(false);
			addHandButton.setDisable(true);
		}
		if (farkle.getNumDiceOnBoard() == 0) {
			addHandButton.setDisable(true);
		}
	}

	/**
	 * Takes the dice that the user selects and puts then in an array
	 * 
	 * @return A dice array with the dice that the user selected
	 */
	public Dice[] getSelectedDice() {
		boolean[] tempArray = new boolean[6];
		Dice[] returnArray = new Dice[6];
		for (int i = 0; i < 6; i++) {
			returnArray[i] = new Dice(0); // Initializes the dice array
			if (diceToDisplayRoll[i + 1].isSelected()) {
				tempArray[i] = true;
			}
		}
		int index = 0;
		for (int i = 0; i < 6; i++) {
			if (tempArray[i]) {
				returnArray[index] = new Dice(farkle.getRoll()[i].getValue());
				index++;
			}
		}
		Arrays.sort(returnArray);
		return returnArray;
	}

	/**
	 * Checks if the a player has won
	 * 
	 * @return returns the index of the player, -1 if no player has won
	 */
	public int checkIfWon() {
		return farkle.checkIfWon();
	}

	/**
	 * Turns everything off if someone has won
	 */
	public void setWinText(int player) {
		solField.setText("");
		currScores.setText("");
		roundScore.setText("");
		playerNum.setText("");
		bankButton.setDisable(true);
		rollButton.setDisable(true);
		addHandButton.setDisable(true);
		gameStatText
				.setText("Player " + player + " has won!!!!! Click the buttons in the top right to start a new game!");
		for (int i = 1; i <= 6; i++) {
			diceToDisplayRoll[i].setDisable(true);
		}
		for(int i = 1; i < farkle.getPlayerPoints().length; i++) {
			System.out.println("Player " + i + " score: " + farkle.getPlayerPoints()[i]);
		}
	}

	/**
	 * Turns very much all of the buttons off if an AI is playing This way, a human
	 * can't interfere
	 */
	public void setTextAI() {
		if (farkle.getIsAI()[farkle.getPlayer()]) {
			bankButton.setDisable(true);
			rollButton.setDisable(true);
			addHandButton.setDisable(true);
			switchButton.setDisable(true);
			gameStatText.setText("AI is currently plaing");
			for (int i = 1; i <= 6; i++) {
				diceToDisplayRoll[i].setDisable(true);
			}
			try {
				callAIPlays();
			} catch (Exception e) {

			}
		} else {
			for (int i = 1; i <= 6; i++) {
				if (i >= farkle.getNumDiceOnBoard() + 1) {
					diceToDisplayRoll[i].setDisable(true); // Turns off the toggle boxes that are empty
				} else {
					diceToDisplayRoll[i].setDisable(false); // Turn them on if there is a dice there
				}
			}
		}
	}

	/**
	 * The method that calls the AI
	 */
	public void callAIPlays() {
		if (farkle.isFirstTime() && farkle.getNumDiceOnBoard() == 6
				&& farkle.getPlayerRoundPoints()[farkle.getPlayer()] == 0) {
			AI = new aiAgent(farkle);
		}
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.75), event -> {
			// This timeline allows the game to pause between AI players
			int status = AI.AIPlays();
			if (status == 0) { // The AI farkled or banked
				updateDisplay();
			} else if (status == -1) {
				gameStatText.setText("AI Farkled, switching players!");
				updateDisplay();
			} else {
				int playerNum = farkle.checkIfWon();
				setWinText(playerNum);
			}
		}));
		timeline.playFromStart();
	}

}
