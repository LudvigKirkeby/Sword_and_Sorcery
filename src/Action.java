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

    // This method is horrible.
    public void printResult() {
        StringBuilder segmented = new StringBuilder();
        String[] words = result.split(" ");
        short count = 0;
        for (String word : words) {
            segmented.append(word);
            segmented.append(" ");
            count++;
            if (count == 15) {
                System.out.println(segmented);
                System.out.println();
                segmented = new StringBuilder();
                count = 0;
            }
        }
        System.out.println(segmented);
    }

    public String getDescription(){
        return " - "+description+" (use '"+keyword+"')";
    }
}
