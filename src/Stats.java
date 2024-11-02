import java.util.Random;
abstract public class Stats {
    public int stats[];
    public int Damage = 2;

    public int RollStrength() {
       Random rand = new Random();
      return rand.nextInt(12) + 1 + stats[1];
    }

    abstract public void Death();

}
