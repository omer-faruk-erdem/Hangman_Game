import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Game game = new Game();
        System.out.println("Enter your name");

        String username ="";
        Scanner scanner = new Scanner(System.in);

        username = scanner.nextLine();

        System.out.println("Your name is : "+username);

        game.addPlayer(username);
        game.playGame();
    }
}
