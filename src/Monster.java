public class Monster extends Stats {
    String Name;
    Monster(String Name) {
        this.Name = Name;
    }

    int[] getStats(int Endurance, int Skill) {
        int[] stats = new int[2];
        stats[0] = Endurance;
        stats[1] = Skill;
        return stats;
    }

    public void Death() {
        System.out.println("You bested the " + Name);
    }
}
