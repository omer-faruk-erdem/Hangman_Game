public class Player {
    private static final int playerHealthInit = 5 ;
    private int health ;
    public String name ;

    private int numberOfKnownWords;
    char guess ;

    public Player( String userName){
        name = userName ;
        numberOfKnownWords = 0 ;
        health = playerHealthInit ;
    }
    public Boolean isDead(){
        return health== -1 ;
    }

    public int getHealth() {
        return health;
    }

    public String getName(){
        return name ;
    }

    public Character getGuess(){ return guess ; }

    public int getNumberOfKnownWords() {
        return numberOfKnownWords;
    }

    public void setNumberOfKnownWords(int numberOfKnownWords) {
        this.numberOfKnownWords = numberOfKnownWords;
    }

    public void setGuess(char g){ guess = g ; }

    public void setHealth(int health){
        this.health = health ;
    }
}