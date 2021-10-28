import java.util.ArrayList;

public class ScoredWords {
    private ArrayList<String> strings = new ArrayList<>();

    public ScoredWords() {}

    public void addWord(String string){
        this.strings.add(string);
    }
    public ArrayList<String> getWords(){
        return this.strings;
    }
}
