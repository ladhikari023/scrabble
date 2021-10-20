import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

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

    private ArrayList<int[]> cellList = new ArrayList<>();
    private ArrayList<Label> labelList = new ArrayList<>();
    private ArrayList<Button> clickedBtn = new ArrayList<>();
    private ArrayList<Button> playedWord = new ArrayList<>();
    private Player player = new Player(true);
    private final int sizeOfGrid = 15;

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
    private void addLabelOnGrid(){
        labelList.clear();
        int[][] DL = {{0,3},{0,11},{2,6},{2,8},{3,0},{3,7},{3,14},{6,2},{6,6},
                {6,8},{6,12},{7,3},{7,11},{8,2},{8,6},{8,8},{8,12},{11,0},{11,7},
                {11,14},{12,6},{12,8},{14,3},{14,11}};
        int[][] TL = {{1,5},{1,9},{5,1},{5,5},{5,9},{5,13},{9,1},{9,5},{9,9},{9,13},{14,5},{14,9}};
        int[][] DW = {{1,1},{2,2},{3,3},{4,4},{4,10},{3,11},{2,12},{1,13},{10,4},{11,3},{12,2},{13,1},{10,10},
                {11,11},{12,12},{13,13}};
        int[][] TW = {{0,0},{0,7},{0,14},{7,0},{7,14},{14,0},{14,7},{14,14}};
        System.out.println(DL.length);
        for (int i = 0; i < cellList.size(); i++) {
            int[] point = cellList.get(i);
            Label label = new Label();
            label.setPrefSize(33,33);
            label.setStyle("-fx-background-color: #e0e0b0");
            if (checkPoint(point[0],point[1],DL)){
                label.setStyle("-fx-background-image: url(DL.png)");
            }
            else if (checkPoint(point[0],point[1],TL)){
                label.setStyle("-fx-background-image: url(TL.png)");
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
            label.setOnMouseClicked(event -> {
                if (!clickedBtn.isEmpty()) {
                    label.setText(clickedBtn.get(0).getText());
                    clickedBtn.get(0).setVisible(false);
                    playedWord.add(clickedBtn.get(0));
                    clickedBtn.clear();
                }
            });
            labelList.add(label);
            this.gridPane.add(label,point[0],point[1]);
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
    public void initialize(){
        BackgroundImage bgImage= new BackgroundImage(new Image("bg2.jpg",900, 900, false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        gamePane.setBackground(new Background(bgImage));
        gridPane.setStyle("-fx-background-color: WHITE");

        player.setName("Laxman Adhikari");

        showGrid();
        startGame();
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
            if (!clickedBtn.isEmpty()) playedWord.add(clickedBtn.get(0));
        }
        else if(!player.getTurn()){
            computerPlay();
        }

        playBtn.setOnAction(event -> {
//            player.setTurn(false);
            System.out.println(playedWord);
            checkWord();
        });
    }

    private void checkWord() {

    }

    private void computerPlay() {
        // Functionality for computer to play the game
    }

    private void showWordsPlayed() {
        playedWordsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        playedWordsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    private void showGrid(){
        gridCellIndexes();
        gridBuild();
        addLabelOnGrid();
    }
    // ShowGrid
        // Make Grid Pane
        // Populate Grid Pane with the special boxes
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
