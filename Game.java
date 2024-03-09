import java.awt.desktop.SystemSleepEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Thread ;

enum Status{
    NOT_STARTED,
    CONTINUE,
    ENDED
}

public class Game {
    private String currentWord ;
    private String displayedWord ;
    private String inputFileName ;
    ArrayList<String> words ;
    Status gameStatus ;
    Scanner scanner ;


    Player player ;

    public Game(){
        gameStatus = Status.NOT_STARTED ;
        words = new ArrayList<>() ;
        inputFileName = "words.txt" ;
        scanner = new Scanner(System.in) ;
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
            Scanner sc = new Scanner(file);

            // Read and process each line of the file
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                words.add(line) ;
            }
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

        String guess = scanner.nextLine() ;

        char gs = guess.charAt(0) ;

        player.setGuess(gs);

    }

    public void printPlayerStatus(){
        // Clear the console
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("Your word " + displayedWord);
        System.out.println("Your health " + player.getHealth() );
        System.out.println("Your current score " + player.getNumberOfKnownWords() );
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

        displayedWord = buffer ;

        while( !isEnded() ){
            printPlayerStatus();
            draw();
            while(true){
                try{
                    takeGuessFromPlayer();
                    break;
                }
                catch (Exception e){
                    System.out.println("You didn't enter any guess!!ç");
                }
            }

            boolean charFound = false ;
            boolean alreadyGuessed = false ;
            String bufferDisplayed = "" ;


            // check if current guess is in the displayed words
            for(int i = 0 ; i< currentWord.length() ; i++ ){
                if(player.getGuess() == displayedWord.charAt(i) ){
                    alreadyGuessed = true ;
                    break;
                }
            }

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

            if(!charFound || alreadyGuessed){
                player.setHealth( player.getHealth()-1 );
            }

            // Determine WIN LOSE or CONTINUE

            // CASE: LOSE

            if( player.isDead() ){
                setGameStatus(Status.ENDED);

                System.out.println("You have lost the game " + player.getName()+ " :/") ;
                System.out.println("As total you have guessed "+ player.getNumberOfKnownWords() +" words correctly");
                System.out.println("The word to be guessed was : " + currentWord) ;

                try {
                    Thread.sleep( 3* 1000);
                } catch (InterruptedException ie) {
                    System.out.println("Sleep interrupted");
                    ie.printStackTrace();
                }
                continue;
            }

            if( wordSize == 0 ){
                player.setNumberOfKnownWords( player.getNumberOfKnownWords()+1 );
                printPlayerStatus() ;
                draw() ;

                System.out.println("You guessed the word correctly ");
                // Case : No words left to be guessed
                if(!pickRandomWordFromWordList()){
                    System.out.println("You have guessed all the words correctly");
                    System.out.println("As total you have guessed " + player.getNumberOfKnownWords() + " words correctly");
                    System.out.println("---------------Game Ended---------------");
                    System.out.println("****************************************");

                    try {
                        Thread.sleep( 3* 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    setGameStatus(Status.ENDED) ;
                    continue ;
                }
                // Case : There are words to be guessed
                else{
                    printPlayerStatus();
                    draw() ;
                    player.setGuess('*');

                    System.out.println("Next word is loading...");
                    setDisplayedWord("");
                    // Maybe we should implement other game logic here other than calling same function recursively.
                    try {
                        Thread.sleep( 3* 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    playGame() ;
                    return ;
                }
            }
            setGameStatus(Status.CONTINUE);
        }
    }
}