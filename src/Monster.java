public class Monster extends Stats {
    private String name;

    Monster(String name) {
        this.name = name;
    }

    int[] getStats(int Endurance, int Skill) {
        int[] stats = new int[2];
        stats[0] = Endurance;
        stats[1] = Skill;
        return stats;
    }

    public String getName() {
        return name;
    }

    public int getEndurance() {
        return stats[0];
    }

    public int getSkill() {
        return stats[1];
    }
    public void Death() {
        System.out.println("You bested the " + name);
    }
}
