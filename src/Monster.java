public class Monster extends Stats {
    private String name;
    private int[] stats = new int[2];

    Monster(String name, int Endurance, int Skill) {
        this.name = name;
        this.stats[0] = Endurance;
        this.stats[1] = Skill;

    }

    public String getName() {
        return name;
    }

    public boolean takeDamage(int damage, boolean inCombat) {
        this.stats[0] -= damage;
        if (this.stats[0] <= 0) {
            Death();
            return false;
        }
        return true;
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
