import java.util.Arrays;
import java.util.Random;

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
        for (int i=1;i<tasknumber-2;i++)
            for (int j=i;j<tasknumber-1;j++) {
                int totaltime=T(task,i,j);
                if (totaltime < min) {
                entryTask = i;
                    exitTask = j;
                    min = totaltime;
                }
            }
        return Arrays.copyOfRange(task,entryTask,exitTask);
    }

    public Task[] ceateTaskGraph(){
        Task[] squentialTask=new Task[10];
        for (int i=0;i<10;i++){
            squentialTask[i]=new Task();
            Random random=new Random();
            squentialTask[i].taskid=i;
            if (i!=9) {
                squentialTask[i].childnumber=1;
                squentialTask[i].child = new int[squentialTask[i].childnumber];
                squentialTask[i].tnet = new int[squentialTask[i].childnumber];
                squentialTask[i].child[0] = i + 1;
                squentialTask[i].tnet[0]=random.nextInt(5)+1;
            }
            squentialTask[i].c=random.nextInt(10)+1;
            squentialTask[i].m=random.nextInt(100)+1;
            squentialTask[i].r=1;
        }
        return squentialTask;
    }

    public static void main(String[] args) {
        Main main=new Main();
        Task[] task=main.ceateTaskGraph();
        Task[] taskincloud=main.sequentailTask(task);
        for (int i=0;i<taskincloud.length;i++){
            System.out.print(taskincloud[i].taskid);
        }
    }
}
