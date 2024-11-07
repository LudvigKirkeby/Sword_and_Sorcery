public class Action {

    //Fields
    private String description;
    private String keyword;
    private String result;
    private int[] stats;

    public Action(String description, String keyword, String result, int[] stats){
        this.description=description;
        this.keyword=keyword;
        this.result=result;
        this.stats =stats;
    }

    public boolean canExecute(String command){
        if(command.equals(keyword)){return true;}
        else{return false;}
    }

    public int[] change_stats(int[] currentStats) {
        for (int i = 0; i < currentStats.length; i++) {
            if (i < 3) {
                currentStats[i] = currentStats[i] + stats[i];
            } else {
                currentStats[i] += stats[i];
            }
        }
        return currentStats;
    }

    public String execute(){
        return result;
    }

    public String getDescription(){
        return " - "+description+" (use '"+keyword+"')";
    }
}
