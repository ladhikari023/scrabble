/*
    This is the FXML loader class of game.fxml
    Main logics of the game are written here in this class
    First I have read the dictionary.txt required for the program
    I have all the labels and text fields on the respective fields as shown in the website
    There are three methods which are responsible for the layout of the game
    I used GridPane to make the crossword like box of size 15/15
    I created random tiles from the tile bag of 100 tiles
    Game runs perfectly for player but doesn't run quite well for the computer
    I have used functions to check the validity of the tile placement by user
    If the tile placement is good and the word formed in the process are in the dictionary, player gets point.
    updateScore() method is responsible for updating the user's score
    The game is continued till there is a possibility of play
 */

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Game {

    // Accessing nodes from fxml to controller class
    @FXML
    private AnchorPane gamePane;
    @FXML
    private GridPane gridPane;
    @FXML
    public ScrollPane playedWordsScroll;
    @FXML
    public Button compRackBtn;
    @FXML
    public Button playBtn;
    @FXML
    public Button swapBtn;
    @FXML
    public Label compLastScoreLabel;
    @FXML
    public Label compTotalScoreLabel;
    @FXML
    public Label yourLastScoreLabel;
    @FXML
    public Label tilesLeftLabel;
    @FXML
    public Label yourTotalScoreLabel;
    @FXML
    private Label blankTileLabel;
    @FXML
    private TextField blankTileTextField;
    @FXML
    private Button blankTileEnterBtn;
    public Label playerScore;
    public Label compTotalScore;
    public Label playerTotalScore;
    public Label tilesCount;
    public Label compLastScore;

    @FXML
    public Button pb1;
    public Button pb2;
    public Button pb3;
    public Button pb4;
    public Button pb5;
    public Button pb6;
    public Button pb7;

    public Button cb1;
    public Button cb2;
    public Button cb3;
    public Button cb4;
    public Button cb5;
    public Button cb6;
    public Button cb7;

    // created variables to use in different functionality
    private final ArrayList<int[]> cellList = new ArrayList<>(); // stores row and column of each cell
    private final ArrayList<Button> clickedBtn = new ArrayList<>(); // The Tile we click to add in game is stored here
    private final ArrayList<String> validWords = new ArrayList<>(); // all words from dictionary
    private final ArrayList<Button> playedButtons = new ArrayList<>(); // all clicked buttons in one turn
    private final ArrayList<Tile> currentlyPlayingTiles = new ArrayList<>(); // all tiles put into box in one turn
    private final ArrayList<StackPane> stackPanes = new ArrayList<>(); // list of stackpanes holding tiles on them
    private final ArrayList<Button> playerButtons = new ArrayList<>(); // rack of players tiles
    private final ArrayList<Button> computerButtons = new ArrayList<>(); // rack of computer tiles
    private final Player player = new Player(true); // instance of class player initialized with player's turn true
    private final Computer computer = new Computer(false); // instance of class computer initialized with computer's turn false
    private final ScoredWords scoredWords = new ScoredWords(); // class holding all the scored words
    private final int sizeOfGrid = 15; // grid size
    private int tileBag = 100 - 7 - 7; // initial tiles
    private int blankCount = 0; // counting the blank tiles shown to user
    private boolean firstTurn = true; // checking if its the first play of the game
    private int playerTotalSum = 0; // total score of player

    String[] alphabets = {"A", "B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R",
            "S","T","U","V","W","X","Y","Z",""};  // All alphabets used in the game

    // Constructor
    public Game() {
    }

    /*
        background image is added to the AnchorPane
        read dictionary and added all words to validWords
        added all buttons to players and computers rack
     */
    public void initialize() throws FileNotFoundException {
        BackgroundImage bgImage = new BackgroundImage(new Image("bg2.jpg", 900, 900, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        gamePane.setBackground(new Background(bgImage));
        gridPane.setStyle("-fx-background-color: WHITE");

        player.setName("Laxman Adhikari");

        showGrid();
        startGame();

        Scanner scanner = new Scanner(new File("src/dictionary.txt"));
        while (scanner.hasNextLine()) {
            validWords.add(scanner.next());
        }
        scanner.close();

        blankTileLabel.setVisible(false);
        blankTileTextField.setVisible(false);
        blankTileEnterBtn.setVisible(false);

        playerButtons.add(pb1);
        playerButtons.add(pb2);
        playerButtons.add(pb3);
        playerButtons.add(pb4);
        playerButtons.add(pb5);
        playerButtons.add(pb6);
        playerButtons.add(pb7);

        computerButtons.add(cb1);
        computerButtons.add(cb2);
        computerButtons.add(cb3);
        computerButtons.add(cb4);
        computerButtons.add(cb5);
        computerButtons.add(cb6);
        computerButtons.add(cb7);

        for (int i = 0; i < playerButtons.size(); i++) {
            playerButtons.get(i).setText(alphabets[getRandomNumber(0,alphabets.length-1)]);
            computerButtons.get(i).setText(alphabets[getRandomNumber(0,alphabets.length-1)]);
        }
    }

    // generate cells of the grid using 2D array
    private void gridCellIndexes() {
        cellList.clear();
        for (int i = 0; i < sizeOfGrid; i++) {
            for (int j = 0; j < sizeOfGrid; j++) {
                int[] cell = {i, j};
                cellList.add(cell);
            }
        }
    }

    // add row and column constraints in the grid pane
    private void gridBuild() {
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        for (int i = 0; i < sizeOfGrid; i++) {
            RowConstraints row = new RowConstraints(33);
            gridPane.getRowConstraints().add(row);
            ColumnConstraints col = new ColumnConstraints(33);
            gridPane.getColumnConstraints().add(col);
        }
    }

    /*
        added stackpanes in each cell of grid pane and added label with background
        added background images in the appropriate cells of GridPane by constructing relevant 2d arrays
        added on click listener property to each stackpanes of the cell
     */
    private void addLabelOnGrid() throws NullPointerException {
        int[][] DL = {{0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7}, {3, 14}, {6, 2}, {6, 6},
                {6, 8}, {6, 12}, {7, 3}, {7, 11}, {8, 2}, {8, 6}, {8, 8}, {8, 12}, {11, 0}, {11, 7},
                {11, 14}, {12, 6}, {12, 8}, {14, 3}, {14, 11}};
        int[][] TL = {{1, 5}, {1, 9}, {5, 1}, {5, 5}, {5, 9}, {5, 13}, {9, 1}, {9, 5}, {9, 9}, {9, 13}, {14, 5}, {14, 9}};
        int[][] DW = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {4, 10}, {3, 11}, {2, 12}, {1, 13}, {10, 4}, {11, 3}, {12, 2}, {13, 1}, {10, 10},
                {11, 11}, {12, 12}, {13, 13}};
        int[][] TW = {{0, 0}, {0, 7}, {0, 14}, {7, 0}, {7, 14}, {14, 0}, {14, 7}, {14, 14}};
        for (int i = 0; i < cellList.size(); i++) {
            int[] point = cellList.get(i);
            StackPane stackPane = new StackPane();
            Label label = new Label();
            label.setPrefSize(33, 33);
            label.setStyle("-fx-background-color: #e0e0b0");
            label.setAlignment(Pos.CENTER);
            label.setFont(new Font(25));
            if (checkPoint(point[0], point[1], DL)) {
                label.setStyle("-fx-background-image: url(DL.png)");
            } else if (checkPoint(point[0], point[1], TL)) {
                label.setStyle("-fx-background-image: url(TL.png)");
            } else if (checkPoint(point[0], point[1], DW)) {
                label.setStyle("-fx-background-image: url(DW.png)");
            } else if (checkPoint(point[0], point[1], TW)) {
                label.setStyle("-fx-background-image: url(TW.png)");
            } else if (point[0] == 7 && point[1] == 7) {
                label.setStyle("-fx-background-image: url(ST.png)");
            }
            stackPane.setOnMouseClicked(event -> {
                if (!clickedBtn.isEmpty() && stackPane.getChildren().size() == 1) {
                    int row = GridPane.getRowIndex((Node) event.getSource());
                    int col = GridPane.getColumnIndex((Node) event.getSource());

                    Tile tile = new Tile(clickedBtn.get(0).getText(), row, col);
                    currentlyPlayingTiles.add(tile);

                    // Adding the clicked button as label
                    Label newLabel = new Label(clickedBtn.get(0).getText());
                    newLabel.setPrefSize(stackPane.getWidth(), stackPane.getHeight());
                    newLabel.setStyle("-fx-background-color: #e0e0b0");
                    newLabel.setFont(new Font(20));
                    newLabel.setAlignment(Pos.CENTER);

                    clickedBtn.get(0).setVisible(false);
                    playedButtons.add(clickedBtn.get(0));
                    clickedBtn.clear();

                    StackPane stack = (StackPane) getNodeByRowColumnIndex(row, col, gridPane);
                    stack.getChildren().add(newLabel);
                    stackPanes.add(stack);
                }
            });
            stackPane.getChildren().addAll(label);
            this.gridPane.add(stackPane, point[0], point[1]);
        }
    }

    // checks if the cell row and column matches the special cells where background image needs to be placed
    private boolean checkPoint(int x, int y, int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][0] == x && arr[i][1] == y) {
                return true;
            }
        }
        return false;
    }

    // returns node at specific row and column index of GridPane
    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    /*
        added on action listener for all player buttons/tiles
        added on action listener for play button and swap button
        calls validityCheck() method which returns an arraylist of strings
        checks if all the strings/words lies in the dictionary
        If exists, sets turn for computer.
        If not, continue playing again
        when swap button is clicked, all the player buttons are swaped to new sets of tiles
     */
    private void startGame() {
        showWordsPlayed();
        if (player.getTurn()) {
            pb1.setOnAction(event -> {
                clickedBtn.add(pb1);
            });
            pb2.setOnAction(event -> {
                clickedBtn.add(pb2);
            });
            pb3.setOnAction(event -> {
                clickedBtn.add(pb3);
            });
            pb4.setOnAction(event -> {
                clickedBtn.add(pb4);
            });
            pb5.setOnAction(event -> {
                clickedBtn.add(pb5);
            });
            pb6.setOnAction(event -> {
                clickedBtn.add(pb6);
            });
            pb7.setOnAction(event -> {
                clickedBtn.add(pb7);
            });
        } else if (computer.getTurn()) {
            computerPlay();
        }

        playBtn.setOnAction(event -> {
            ArrayList<String> currentStringsList = getAllPlayedWords();
            System.out.println(currentStringsList);
            if (!firstTurn && currentStringsList.size()==1 && currentStringsList.get(0).length()==currentlyPlayingTiles.size()) rePlay();
            boolean midPlayed = true;
            if (firstTurn){
                midPlayed = false;
                for (Tile tile:currentlyPlayingTiles
                     ) {
                    if (tile.getRow()==7 && tile.getCol()==7){
                        midPlayed = true;
                    }
                }
                if (!midPlayed) rePlay();
                else firstTurn = false;
            }

            boolean allValid = true;
            for (String word:currentStringsList
                 ) {
                int result = searchString(validWords,word.toLowerCase());
                if (result==-1){
                    allValid = false;
                }
            }
            if (allValid){
                updateScore(currentStringsList);
                showWordsPlayed();
                player.setTurn(false);
                computer.setTurn(true);
                extractTiles();
                playedButtons.clear();
                stackPanes.clear();
                currentlyPlayingTiles.clear();
            }else{
                rePlay();
            }
            startGame();
        });

        swapBtn.setOnAction(event -> {
            for (int i = 0; i < 7; i++) {
                playerButtons.get(i).setText(alphabets[getRandomNumber(0,alphabets.length-1)]);
                playerButtons.get(i).setVisible(true);
                tileBag--;
            }
            tilesCount.setText(String.valueOf(tileBag));
        });
    }

    /*
        returns arraylist of string of all the words formed as a result of placed tiles in the game board
     */
    private ArrayList<String> getAllPlayedWords() {
        ArrayList<String> currentStrings = new ArrayList<>();
        int maxRow = 0, minRow = 14, maxCol = 0, minCol = 14;
        for (Tile tile : currentlyPlayingTiles
        ) {
            if (tile.getRow() > maxRow) maxRow = tile.getRow();
            if (tile.getRow() < minRow) minRow = tile.getRow();
            if (tile.getCol() > maxCol) maxCol = tile.getCol();
            if (tile.getCol() < minCol) minCol = tile.getCol();
        }
        // horizontal case
        if (maxCol-minCol>=1){
            int row = minRow;
            int col = minCol;
            String string = "";

            while (true){
                StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row,col-1,gridPane);
                if (stackPane.getChildren().size()>1){
                    col--;
                }else{
                    break;
                }
            }
            while (true){
                StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row,col,gridPane);
                if (stackPane.getChildren().size()>1){
                    Label label = (Label) stackPane.getChildren().get(1);
                    string += label.getText();
                    col++;
                }else{
                    break;
                }
            }
            if (string.length()>1) currentStrings.add(string.toLowerCase());
            currentStrings.addAll(checkVertical(minCol,maxCol,row));
        }
        // vertical case
        if (maxRow-minRow>=1){
            int startRow = 0;
            int row = minRow;
            int col = minCol;
            while (true){
                StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row-1,col,gridPane);
                if (stackPane.getChildren().size()>1){
                    row--;
                }else{
                    break;
                }
            }
            String string = "";
            while (true){
                StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row,col,gridPane);
                if (stackPane.getChildren().size()>1){
                    Label label = (Label) stackPane.getChildren().get(1);
                    string += label.getText();
                    row++;
                }else{
                    break;
                }
            }
            if (string.length()>1) currentStrings.add(string.toLowerCase());
            currentStrings.addAll(checkHorizontal(minRow,maxRow,col));
        }
        // Adding single letter case
        if (maxCol==minCol && maxRow==minRow){
            String string = "";
            int row = minRow;
            int col = minCol;


            // Search left
            while (true){
                StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row,col-1,gridPane);
                if (stackPane.getChildren().size()>1){
                    col--;
                }else{
                    break;
                }
            }
            // Go right
            while (true) {
                StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row, col, gridPane);
                if (stackPane.getChildren().size() > 1) {
                    Label label = (Label) stackPane.getChildren().get(1);
                    string += label.getText();
                    col++;
                } else {
                    break;
                }
            }
            if (string.length()>1) currentStrings.add(string.toLowerCase());

            string = "";

            // Search Up
            col = minCol;
            row = minRow;
            while (true){
                StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row-1,col,gridPane);
                if (stackPane.getChildren().size()>1){
                    row--;
                }else{
                    break;
                }
            }
            while (true) {
                StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row, col, gridPane);
                if (stackPane.getChildren().size() > 1) {
                    Label label = (Label) stackPane.getChildren().get(1);
                    string += label.getText();
                    row++;
                } else {
                    break;
                }
            }
            if (string.length()>1) currentStrings.add(string.toLowerCase());
        }
        return currentStrings;
    }

    // returns arraylist of string of all the words formed as a result of each vertical play
    private ArrayList<String> checkVertical(int minCol, int maxCol,int dRow) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = minCol; i <= maxCol; i++) {
            int col = i;
            int row = dRow;
            boolean escape = true;
            for (int j = 0; j < currentlyPlayingTiles.size(); j++) {
                Tile tile = currentlyPlayingTiles.get(j);
                if (tile.getCol() == col && tile.getRow() == row) {
                    escape = false;
                    break;
                }
            }
            if (!escape) {
                while (true) {
                    StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row - 1, col, gridPane);
                    if (stackPane.getChildren().size() > 1) {
                        row--;
                    } else {
                        break;
                    }
                }
                String string = "";
                while (true) {
                    StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row, col, gridPane);
                    if (stackPane.getChildren().size() > 1) {
                        Label label = (Label) stackPane.getChildren().get(1);
                        string += label.getText();
                        row++;
                    } else {
                        break;
                    }
                }
                if (string.length() > 1) {
                    strings.add(string.toLowerCase());
                }
            }
        }
        return strings;
    }

    // returns arrayList of string of all the words formed as a result of each horizontal play
    private ArrayList<String> checkHorizontal(int minRow, int maxRow,int dCol) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = minRow; i <= maxRow; i++) {
            int row = i;
            int col = dCol;
            boolean escape = true;
            for (int j = 0; j < currentlyPlayingTiles.size(); j++) {
                Tile tile = currentlyPlayingTiles.get(j);
                if (tile.getCol() == col && tile.getRow() == row) {
                    escape = false;
                    break;
                }
            }
            if (!escape) {
                while (true) {
                    StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row, col - 1, gridPane);
                    if (stackPane.getChildren().size() > 1) {
                        col--;
                    } else {
                        break;
                    }
                }
                String string = "";
                while (true) {
                    StackPane stackPane = (StackPane) getNodeByRowColumnIndex(row, col, gridPane);
                    if (stackPane.getChildren().size() > 1) {
                        Label label = (Label) stackPane.getChildren().get(1);
                        string += label.getText();
                        col++;
                    } else {
                        break;
                    }
                }
                if (string.length() > 1) {
                    strings.add(string.toLowerCase());
                }
            }else System.out.println("Yes sir");
        }
        System.out.println(strings + " check horizontal");
        return strings;
    }

    // replaces played tiles with new set of tiles
    private void extractTiles() {
        if (tileBag > 0) {
            for (int i = 0; i < playedButtons.size(); i++) {
                int rand = getRandomNumber(0, alphabets.length-1);
                if (blankCount > 2 && alphabets.length==27) {
                    String[] newAlphabets = new String[alphabets.length - 1];
                    for (int j = 0; j < alphabets.length-1; j++) {
                        newAlphabets[j] = alphabets[j];
                    }
                    alphabets = newAlphabets;
                }
                playedButtons.get(i).setText(alphabets[rand]);
                if (alphabets[rand].equals("")) {
                    playedButtons.get(i).setText("-");
                    askValueForBlank(alphabets, i);
                    blankCount++;
                }
                playedButtons.get(i).setVisible(true);
                tileBag--;
                tilesCount.setText(String.valueOf(tileBag));
            }
        }
    }

    // asks user to enter the value if blank tiles is shown
    private void askValueForBlank(String[] alphabets, int x) {
        blankTileLabel.setVisible(true);
        blankTileTextField.setVisible(true);
        blankTileEnterBtn.setVisible(true);

        blankTileEnterBtn.setOnAction(event -> {
            boolean isValid = false;
            for (int i = 0; i < alphabets.length - 2; i++) {
                System.out.println(blankTileTextField.getText() + " and "+alphabets[i]);
                if (blankTileTextField.getText().equals(alphabets[i])) {
                    isValid = true;
                    break;
                }
            }
            if (isValid) playedButtons.get(x).setText(blankTileTextField.getText());
        });
    }

    // reutrns a random number
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /*
        Player needs to replay if the words they played is not found in the dictionary
     */
    private void rePlay() {
        for (int i = playedButtons.size()-1; i > -1; i--) {
            playedButtons.get(i).setVisible(true);
            stackPanes.get(i).getChildren().remove(1);
        }
        player.setTurn(true);
        currentlyPlayingTiles.clear();
        stackPanes.clear();
        playedButtons.clear();
    }

    // binary search string on all the words from dictionary
    private int searchString(ArrayList<String> arr, String x)
    {
        int l = 0, r = arr.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            int res = x.compareTo(arr.get(m));
            if (res == 0)
                return m;
            if (res > 0)
                l = m + 1;
            else
                r = m - 1;
        }
        return -1;
    }

    // updates score based on the words formed during the game play
    private void updateScore(ArrayList<String> stringList) {
        int sum = 0;
        for (String string:stringList
             ) {
            for (int i = 0; i < string.length(); i++) {
                sum += getValueOfString(string.charAt(i));
            }
        }
        playerTotalSum += sum;
        playerScore.setText(String.valueOf(sum));
        playerTotalScore.setText(String.valueOf(playerTotalSum));
    }

    // returns value of string from A to Z
    private int getValueOfString(char ch) {
        int value = 0;
        String string ="";
        string+=ch;

        switch (string){
            case "a":
            case "e":
                value = 4;
            break;
            case "b":
            case "d":
            case "f":
                value=6;
                break;
            case "c":
            case "g":
                value=7;
                break;
            case "h":
            case "l":
            case "p":
                value+=6;
                break;
            case "i":
            case "o":
            case "r":
            case "s":
                value+=4;
                break;
            case "j":
            case "m":
            case "n":
            case "t":
                value+=5;
                break;
            case "k":
            case "u":
            case "y":
                value+=7;
                break;
            case "q":
            case "z":
                value+=10;
                break;
            case "v":
            case "w":
                value+=8;
                break;
            case "x": value+=9;
                break;
            default: value = 0;
        }
        return value;
    }

    // logic to be added for computer to play. Not complete
    private void computerPlay() {
        // Functionality for computer to play the game
        for (int i = 0; i < 15; i++) {
            StackPane stackPane = (StackPane) getNodeByRowColumnIndex(i,i,gridPane);
            if (stackPane.getChildren().size()>1){
                int row = i,col = i;
                System.out.println(i+" row"+ i+ " col");
                while (true){
                    stackPane = (StackPane) getNodeByRowColumnIndex(row-1,col,gridPane);
                    if (stackPane.getChildren().size()>1){
                        row--;
                    }else{
                        break;
                    }
                }
                generateWord(row,col);
            }
        }

        //updateScore(computerWordList);
        player.setTurn(true);
        computer.setTurn(false);
        startGame();
    }

    // generates word for computer to put on the game board
    private void generateWord(int row,int col) {
        for (int j = row; j < row+5; j++) {
            StackPane stackPane = (StackPane) getNodeByRowColumnIndex(j,col,gridPane);

            Label newLabel = new Label(computerButtons.get(getRandomNumber(0,7)).getText());
            newLabel.setPrefSize(stackPane.getWidth(), stackPane.getHeight());
            newLabel.setStyle("-fx-background-color: #e0e0b0");
            newLabel.setFont(new Font(20));
            newLabel.setAlignment(Pos.CENTER);

            stackPane.getChildren().add(1,newLabel);
        }
    }

    // shows all the played words in the scroll pane view
    private void showWordsPlayed() {
        playedWordsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        playedWordsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


        // Needs to show the played words in words list
    }

    // shows grid
    private void showGrid(){
        gridCellIndexes();
        gridBuild();
        addLabelOnGrid();
    }
}
