import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

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
    public Button passBtn;
    @FXML
    public Button clearBtn;
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
    public Button pb1;
    @FXML
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

    private final ArrayList<int[]> cellList = new ArrayList<>();
    private final ArrayList<Button> clickedBtn = new ArrayList<>();
    private final ArrayList<String> validWords = new ArrayList<>();
    private final ArrayList<Button> playedButtons = new ArrayList<>();
    private final ArrayList<Tile> currentlyPlayingTiles = new ArrayList<>();
    private final ArrayList<Tile> allTiles = new ArrayList<>();
    private final ArrayList<StackPane> stackPanes = new ArrayList<>();
    private final Player player = new Player(true);
    private final Computer computer = new Computer(false);
    private final ScoredWords scoredWords = new ScoredWords();
    private final int sizeOfGrid = 15;
    private boolean isVertical = true;

    public Game(){}
    private void gridCellIndexes() {
        cellList.clear();
        for (int i = 0; i < sizeOfGrid; i++) {
            for (int j = 0; j < sizeOfGrid; j++) {
                int[] cell = {i,j};
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

    //adding buttons in each cell of grid pane
    private void addLabelOnGrid() throws NullPointerException{
        int[][] DL = {{0,3},{0,11},{2,6},{2,8},{3,0},{3,7},{3,14},{6,2},{6,6},
                {6,8},{6,12},{7,3},{7,11},{8,2},{8,6},{8,8},{8,12},{11,0},{11,7},
                {11,14},{12,6},{12,8},{14,3},{14,11}};
        int[][] TL = {{1,5},{1,9},{5,1},{5,5},{5,9},{5,13},{9,1},{9,5},{9,9},{9,13},{14,5},{14,9}};
        int[][] DW = {{1,1},{2,2},{3,3},{4,4},{4,10},{3,11},{2,12},{1,13},{10,4},{11,3},{12,2},{13,1},{10,10},
                {11,11},{12,12},{13,13}};
        int[][] TW = {{0,0},{0,7},{0,14},{7,0},{7,14},{14,0},{14,7},{14,14}};
        for (int i = 0; i < cellList.size(); i++) {
            int valueOfCell = 1;
            int[] point = cellList.get(i);
            StackPane stackPane = new StackPane();
            Label label = new Label();
            label.setPrefSize(33,33);
            label.setStyle("-fx-background-color: #e0e0b0");
            label.setAlignment(Pos.CENTER);
            label.setFont(new Font(25));
            if (checkPoint(point[0],point[1],DL)){
                label.setStyle("-fx-background-image: url(DL.png)");
                valueOfCell = 2;
            }
            else if (checkPoint(point[0],point[1],TL)){
                label.setStyle("-fx-background-image: url(TL.png)");
                valueOfCell=3;
            }
            else if (checkPoint(point[0],point[1],DW)){
                label.setStyle("-fx-background-image: url(DW.png)");
            }
            else if (checkPoint(point[0],point[1],TW)){
                label.setStyle("-fx-background-image: url(TW.png)");
            }
            else if(point[0]==7 && point[1]==7){
                label.setStyle("-fx-background-image: url(ST.png)");
            }
            stackPane.setOnMouseClicked(event -> {
                if (!clickedBtn.isEmpty()) {
                    int row = GridPane.getRowIndex((Node) event.getSource());
                    int col = GridPane.getColumnIndex((Node) event.getSource());
                    Tile tile = new Tile(clickedBtn.get(0).getText(),row,col);
                    currentlyPlayingTiles.add(tile);
                    Label newLabel = new Label(clickedBtn.get(0).getText());
                    newLabel.setPrefSize(stackPane.getWidth(),stackPane.getHeight());
                    newLabel.setStyle("-fx-background-color: #e0e0b0");
                    newLabel.setFont(new Font(20));
                    newLabel.setAlignment(Pos.CENTER);
                    clickedBtn.get(0).setVisible(false);
                    playedButtons.add(clickedBtn.get(0));
                    clickedBtn.clear();

                    StackPane stack = (StackPane) getNodeByRowColumnIndex(row,col,gridPane);
                    stack.getChildren().add(newLabel);
                    stackPanes.add(stack);
                }
            });
            stackPane.getChildren().addAll(label);
            this.gridPane.add(stackPane,point[0],point[1]);
        }
    }

    private boolean checkPoint(int x,int y,int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][0]==x && arr[i][1]==y){
                return true;
            }
        }
        return false;
    }
    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
    public void initialize() throws FileNotFoundException {
        BackgroundImage bgImage= new BackgroundImage(new Image("bg2.jpg",900, 900, false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        gamePane.setBackground(new Background(bgImage));
        gridPane.setStyle("-fx-background-color: WHITE");

        player.setName("Laxman Adhikari");

        showGrid();
        startGame();

        Scanner scanner = new Scanner(new File("/Users/laxmanadhikari/CS_351/humanbenchmark/src/dictionary.txt"));
        while (scanner.hasNextLine()){
            validWords.add(scanner.next());
        }
        scanner.close();
    }

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
        }
        else if(computer.getTurn()){
            computerPlay();
        }

        playBtn.setOnAction(event -> {
            player.setTurn(false);
            checkWord();
        });
    }

    private void checkWord() {
        String string = getPlayedString();
        int result = searchString(validWords,string);
        if (result==-1){
            rePlay();
            System.out.println("WORD NOT FOUND IN DICTIONARY");
            //Alert Message
        }else{
            if (validPosition()) {
                allTiles.addAll(currentlyPlayingTiles);
                player.setTurn(false);
                computer.setTurn(true);
                currentlyPlayingTiles.clear();
                stackPanes.clear();
                playedButtons.clear();
                updateScore();
            }else{
                rePlay();
            }
        }
        startGame();
    }

    private boolean validPosition(){
         // check if any tile is touching
        // checkTilesTouch() return strings that the currently playing tiles has formed
        //                      return empty string if there are none so we know that the position is invalid
        // when we get the strings from the method, we look them again into dictionary if we find it we add score
        // to the player, if not we run replay() with alert message!!
        if (allTiles.isEmpty()) {
            scoredWords.addWord(getPlayedString());
            return true;
        }
        if (isVertical){
            int minRow = currentlyPlayingTiles.get(0).getRow();
            int maxRow = currentlyPlayingTiles.get(currentlyPlayingTiles.size()-1).getRow();
            int col = currentlyPlayingTiles.get(0).getCol();
            if (checkTouch(minRow, maxRow, col)) return true;
        }else{
            int minCol = currentlyPlayingTiles.get(0).getCol();
            int maxCol = currentlyPlayingTiles.get(currentlyPlayingTiles.size()-1).getCol();
            int row = currentlyPlayingTiles.get(0).getRow();
            if (checkTouch(minCol, maxCol, row)) return true;
        }
        return false;
    }

    private boolean checkTouch(int min, int max, int rowOrCol) {
        for (int i = 0; i < allTiles.size(); i++) {
            for (int j = min-1; j <=max+1; j++) {
                if (j==min-1 && allTiles.get(i).getRow()==j){
                    String s = getPlayedString();
                    
                    return true;
                }

                if (j==max+1 && allTiles.get(i).getRow()==j){

                    return true;
                }
                if (allTiles.get(i).getCol()==(rowOrCol+1) || allTiles.get(i).getCol()==(rowOrCol-1)){

                    return true;
                }
            }
        }
        return false;
    }

    private String getPlayedString() {
        String string = "";
        int minCol = 14;
        int minRow = 14;
        int maxRow = 0;
        for (Tile tile: currentlyPlayingTiles) {
            if (tile.getRow()<minRow){
                minRow = tile.getRow();
            }
            if (tile.getRow()>maxRow){
                maxRow = tile.getRow();
            }
            if (tile.getCol()<minCol){
                minCol = tile.getCol();
            }
        }
        char[] ch = new char[currentlyPlayingTiles.size()];
        if (minRow==maxRow){
            isVertical = false;
            for (int i = 0; i < currentlyPlayingTiles.size(); i++) {
                ch[currentlyPlayingTiles.get(i).getCol()-minCol] = currentlyPlayingTiles.get(i).getString().charAt(0);
            }
        }else{
            isVertical = true;
            for (int i = 0; i < currentlyPlayingTiles.size(); i++) {
                ch[currentlyPlayingTiles.get(i).getRow()-minRow] = currentlyPlayingTiles.get(i).getString().charAt(0);
            }
        }
        for (int i = 0; i < ch.length; i++) {
            string += ch[i];
        }
        System.out.println(string);
        return string.toLowerCase();
    }

    /*
        Player needs to replay if the words they played is not found in the dictionary
     */
    private void rePlay() {
        for (int i = playedButtons.size()-1; i > -1; i--) {
            playedButtons.get(i).setVisible(true);
            stackPanes.get(i).getChildren().remove(1);
        }
        currentlyPlayingTiles.clear();
        stackPanes.clear();
        playedButtons.clear();
    }

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

    private void updateScore() {

    }

    private void computerPlay() {
        // Functionality for computer to play the game
        System.out.println("I played, Now your turn");

        updateScore();
        player.setTurn(true);
        computer.setTurn(false);
        startGame();
    }

    private void showWordsPlayed() {
        playedWordsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        playedWordsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Needs to show the played words in words list
    }

    private void showGrid(){
        gridCellIndexes();
        gridBuild();
        addLabelOnGrid();
    }
    // ShowGrid
        // Add functionality to multiply the words or letters score in special boxes
    // ShowWords
        // Show the letters from the 100 letters
        // Add exchange letters functionality
        // Add words drag functionality
    // CheckDrag
        // check if the cell we are dragging our letter is valid when the user hits enter/play btn
    // Add Computer Logic to play the best word possible
    // ShowScore - update the score everytime user hits the enter/play btn
}
