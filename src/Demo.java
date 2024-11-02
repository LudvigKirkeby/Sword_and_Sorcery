import java.io.IOException;
import java.util.Scanner;
public class Demo {
    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.println("1. Continue,  2. New Game");
        int input = s.nextInt();
        Adventure adventure = new Adventure();
        switch(input) {
            case 1:
                adventure.NewGame_or_Load();
                adventure.play();
                break;
                case 2:
                    adventure.new_Game = true;
                    adventure.NewGame_or_Load();
                    adventure.play();
        }
    }
}
