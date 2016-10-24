import java.util.Arrays;

public class Main {

    public int T(Task[] task,int left,int right){
        int sum=0;
        for (int i=0;i<left-1;i++)
            sum+=task[i].m;
        sum+=task[left-1].tnet[0];
        for (int i=left;i<=right;i++)
            sum+=task[i].c;
        sum+=task[right].tnet[0];
        for (int i=right+1;i<task.length;i++)
            sum+=task[i].m;
        return sum;
    }

    public Task[] sequentailTask(Task[] task){
        int entryTask=1;
        int exitTask=1;
        int min=T(task,1,1);
        int tasknumber=task.length;
        for (int i=1;i<tasknumber-1;i++)
            for (int j=i;i<=tasknumber-1;i++) {
                int totaltime=T(task,i,j);
                if (totaltime < min) {
                    entryTask = i;
                    exitTask = j;
                    min = totaltime;
                }
            }
        return Arrays.copyOfRange(task,entryTask,exitTask);
    }



    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
