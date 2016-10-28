/**
 * Created by jay on 2016/10/24.
 */
public class Task {
    /**
     *
     */
    public int taskid=0;
    public int r=0;
    public int childnumber;
    public int[] child;
    public int c;
    public int m;
    public int[] tnet;
    public int fnum=0;
    public int[] f;
    public int starttime=0;
    public int finishtime=0;
    public boolean isCalc=false;


    public static void deletechildEdge(Task task,int childid){
        for (int i=childid;i<task.childnumber-1;i++){
            task.child[i]=task.child[i+1];
        }
        task.childnumber--;
    }

    public static void deletefatherEdge(Task task,int father){
        for (int i=father;i<task.fnum-1;i++){
            task.f[i]=task.f[i+1];
        }
        task.fnum--;
    }
}


