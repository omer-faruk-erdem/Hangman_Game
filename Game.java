import java.awt.desktop.SystemSleepEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

enum Status{
    NOT_STARTED,
    CONTINUE,
    ENDED
}

public class Game {
    public static void main(String[] args) {
        // TODO : Implement main code logic here.
    }
    private String currentWord ;
    private String displayedWord ;
    private String inputFileName ;
    ArrayList<String> words ;
    Status gameStatus ;

    Player player ;

    public Game(){
        gameStatus = Status.NOT_STARTED ;
        getWords();
    }

    /* Setters */
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    private void setCurrentWord( String s){
        currentWord = s ;
    }

    private void setDisplayedWord( String s){
        displayedWord = s ;
    }

    public void setGameStatus( Status st){
        gameStatus = st ;
    }


    /* Getters */
    public Status getGameStatus() {
        return gameStatus;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public String getDisplayedWord() {
        return displayedWord;
    }


    private void getWords(){
        String filePath = inputFileName;

        try {
            // Create a File object representing the file
            File file = new File(filePath);

            // Create a Scanner object to read from the file
            Scanner scanner = new Scanner(file);

            // Read and process each line of the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                words.add(line) ;
                System.out.println(line);
            }

            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            // Handle file not found exception
            System.out.println( e.getMessage() );
        }

    }

    public Boolean pickRandomWordFromWordList(){
        int numberOfWords = words.size();
        if( numberOfWords == 0) return false ;

        int randomIndex = (int) (Math.random()*numberOfWords);
        setCurrentWord( words.get(randomIndex) );
        // erase word from word list
        words.remove(randomIndex) ;
        return true;
    }

    public void addPlayer(String name){
        player = new Player(name) ;
    }

    public void takeGuessFromPlayer(){
        System.out.println("Enter your guess ... ");

        Scanner scanner = new Scanner(System.in) ;
        String input = scanner.nextLine();

        // Extract the first character from the input string
        char gs = '-' ;

        if (!input.isEmpty()) {
            gs = input.charAt(0);
            System.out.println("You entered: " + gs);
        } else {
            System.out.println("You didn't enter anything.");
        }

        player.setGuess(gs);
        scanner.close();
    }

    public void printPlayerStatus(){
        // Clear the console
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("Your word " + currentWord);
        System.out.println("Your health " + player.getHealth() );
        System.out.println("Your current score " + player.getScore() );
    }

    public boolean isEnded(){
        return gameStatus == Status.ENDED ;
    }



    public void draw(){
        int health = player.getHealth() ;

        if( health <= 4 ){
            drawRope() ;
        }

        if( health <= 3 ){
            drawHead();
        }

        if( health <= 2 ){
            drawArms() ;
        }

        if( health <= 1 ){
            drawBody() ;
        }

        if( health <= 0 ){
            drawFoot() ;
        }

    }

    private void drawRope(){
        System.out.println("**************************************");
        System.out.println("      |   ");
        System.out.println("      |   ");
        System.out.println("    ----- ");
    }

    private void drawHead (){
        System.out.println("      O   ");
    }
    private void drawArms(){
        // put one extra backslash to escape special character property of it in java
        System.out.println("     /|\\  ");
    }
    private void drawBody(){
        System.out.println("      |   ");
    }
    private void drawFoot (){
        System.out.println("     / \\  ");
    }

    public void playGame(){
        pickRandomWordFromWordList() ;

        int wordSize = currentWord.length() ;

        String buffer = ""  ;
        // Make displayed word same length with currentWord but all chars replaced by '_'

        for(int i = 0 ; i<wordSize ; i++){
            buffer =  buffer + "_" ;
        }


        while( !isEnded() ){
            printPlayerStatus();
            draw();
            takeGuessFromPlayer();

            boolean charFound = false ;

            String bufferDisplayed = "" ;

            // Update displayed word according to player's guess.
            for(int i = 0 ; i< currentWord.length() ; i++ ){
                if( currentWord.charAt(i)  == player.getGuess() ){
                    charFound = true ;
                    wordSize -- ;
                    bufferDisplayed += player.getGuess() ;
                }
                else{
                    bufferDisplayed += displayedWord.charAt(i) ;
                }
            }

            // change displayed word
            setDisplayedWord(bufferDisplayed);

            if(!charFound){
                player.setHealth( player.getHealth()-1 );
            }

            // Determine WIN LOSE or CONTINUE

            // CASE: LOSE

            if( player.isDead() ){
                setGameStatus(Status.ENDED);

                System.out.println("You have lost the game :/") ;
                System.out.println("As total you have guessed "+ player.getNumberOfKnownWords() +" words correctly");
                System.out.println("The word to be guessed was : " + currentWord) ;


                // TODO: implement wait logic here
                continue;
            }

            if( wordSize == 0 ){
                printPlayerStatus() ;
                draw() ;
                player.setNumberOfKnownWords( player.getNumberOfKnownWords()+1 );

                System.out.println("You guessed the word correctly ");
                // Case : No words left to be guessed
                if(!pickRandomWordFromWordList()){
                    System.out.println("You have guessed all the words correctly");
                    System.out.println("As total you have guessed " + player.getNumberOfKnownWords() + " words correctly");
                    System.out.println("---------------Game Ended---------------");
                    System.out.println("****************************************");

                    // TODO: implement wait logic here
                    // std::this_thread::sleep_for(std::chrono::seconds(3));

                    setGameStatus(Status.ENDED) ;
                    continue ;
                }
                // Case : There are words to be guessed
                else{
                    System.out.println("Next word is loading...");
                    setDisplayedWord("");
                    // Maybe we should implement other game logic here other than calling same function recursively.
                    playGame() ;
                    return ;
                }
            }
            setGameStatus(Status.CONTINUE);
        }
    }
}