import java.util.Arrays;
import java.util.Random;

public class Main {

    public int T(Task[] task, int left, int right) {
        int sum = 0;
        for (int i = 0; i < left; i++)
            sum += task[i].m;
        sum += task[left - 1].tnet[0];
        for (int i = left; i <= right; i++)
            sum += task[i].c;
        sum += task[right].tnet[0];
        for (int i = right + 1; i < task.length; i++)
            sum += task[i].m;
        return sum;
    }

    public Task[] sequentailTask(Task[] task) {
        int entryTask = 1;
        int exitTask = 1;
        int min = T(task, 1, 1);
        int tasknumber = task.length;
        for (int i = 1; i < tasknumber - 2; i++)
            for (int j = i; j < tasknumber - 1; j++) {
                int totaltime = T(task, i, j);
                if (totaltime < min) {
                    entryTask = i;
                    exitTask = j;
                    min = totaltime;
                }
            }
        for (Task atask : task) {
            if (atask.taskid >= entryTask && atask.taskid <= exitTask) atask.r = 0;
            else atask.r = 1;
        }
        return Arrays.copyOfRange(task, entryTask, exitTask+1);
    }

    public void ToTree(Task[] task,int taskid){
        Task currentTask=task[taskid];
        if (currentTask.fnum!=0){
            /**
             *  maxtnet :  max tnet ;
             *  maxfid :   id of father task with max tnet ;
             *  fchildid  : serial number of fathertask.child;
             */
            int maxfnet=0;
            int maxfid=0;
            for (int i=0;i<=currentTask.fnum;i++){
                Task ftask=task[currentTask.f[i]];
                int fchildid =0;
                for (int j=0;j<=ftask.childnumber;j++)
                    if (ftask.child[j]==taskid) {
                        fchildid=j;
                        break;
                    }
                if (maxfnet<ftask.tnet[fchildid]) {
                    maxfnet=ftask.tnet[fchildid];
                    maxfid=i;
                }
            }
            /**
             * delete edge whichi is not between maxfid and currenttask
             */
            for (int i=0;i<=currentTask.fnum;i++){
                if (i!=maxfid) {
                    Task ftask = task[currentTask.f[i]];
                    int fchildid=0;
                    for (int j = 0; j <= ftask.childnumber; j++)
                        if (ftask.child[j] == taskid) {
                            fchildid=j;
                        }
                    Task.deletechildEdge(ftask,fchildid);
                    Task.deletefatherEdge(currentTask,i);
                }
            }
        }

        /**
         * recall
         */
        for (int i=0;i<currentTask.childnumber;i++) ToTree(task,currentTask.child[i]);

    }

    public Task[] CalcTaskPosition(Task[] task){
        Task[] taskcopy=new Task[task.length];
        System.arraycopy(task,0,taskcopy,0,task.length);

        return task;
    }

    public Task[] ceateTaskGraph() {
        Task[] squentialTask = new Task[10];
        for (int i = 0; i < 10; i++) squentialTask[i] = new Task();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            squentialTask[i].taskid = i;
            if (i != 9) {
                squentialTask[i].childnumber = 1;
                squentialTask[i].child = new int[squentialTask[i].childnumber];
                squentialTask[i].tnet = new int[squentialTask[i].childnumber];
                squentialTask[i + 1].fnum = 1;
                squentialTask[i + 1].f = new int[squentialTask[i + 1].fnum];
                squentialTask[i].child[0] = i + 1;
                squentialTask[i + 1].f[0] = i;
                squentialTask[i].tnet[0] = 5;
            }
            squentialTask[i].c = 10 ;
            squentialTask[i].m = 100 ;
            squentialTask[i].r = 1;
        }
        return squentialTask;
    }


    public int CalcTimeByTaskGraph(Task[] task) {
        int totaltime = 0;
        int[] rd = new int[task.length];
        Task currentTask = null;
        for (int i = 0; i < task.length; i++) {
            rd[i] = task[i].fnum;
        }
         while (true) {
            int minfinishtime = Integer.MAX_VALUE;
            for (int i = 0; i < task.length; i++) {
                if (task[i].finishtime < minfinishtime && !task[i].isCalc&& rd[task[i].taskid]==0) {
                    minfinishtime = task[i].finishtime;
                    currentTask = task[i];
                }
            }
            if (minfinishtime == Integer.MAX_VALUE) break;
            currentTask.isCalc = true;
            if (currentTask.taskid == 0) {
                currentTask.starttime = 0;
                currentTask.finishtime = currentTask.m;
            }
            for (Task aTask : task) {
                if (aTask.r == currentTask.r && !aTask.isCalc && rd[aTask.taskid] == 0) {
                    currentTask.childnumber++;
                    int[] a = new int[aTask.childnumber];
                    System.arraycopy(aTask.child, 0, a, 0, aTask.childnumber-1);
                    a[a.length - 1] = currentTask.taskid;
                    aTask.child = a;
                    rd[aTask.taskid]++;
                }
            }
            for (int i = 0; i < currentTask.childnumber; i++) {
                Task childtask = task[currentTask.child[i]];
                childtask.starttime = Integer.max(childtask.starttime,
                        currentTask.finishtime + Math.abs(childtask.r - currentTask.r) * currentTask.tnet[i]);
                if (childtask.r==0) childtask.finishtime=childtask.c+childtask.starttime;
                else childtask.finishtime=childtask.m+childtask.starttime;
            }
            for (int j = 0; j < currentTask.childnumber; j++) {
                rd[currentTask.child[j]]--;
            }
        }

        totaltime = task[task.length-1].finishtime;
        return totaltime;
    }


    public static void main(String[] args) {
        Main main = new Main();
        Task[] task = main.ceateTaskGraph();
        Task[] taskincloud = main.sequentailTask(task);
        for (int i = 0; i < taskincloud.length; i++) {
            System.out.println(taskincloud[i].taskid);
        }
        System.out.println(main.CalcTimeByTaskGraph(task));

    }
}
