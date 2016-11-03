/**
 * Created by jay on 2016/10/24.
 */
public class Task implements Cloneable{
    /**
     *
     */
    public int taskid=0;
    public int r=1;
    public int childnumber=0;
    public int[] child=new int[100];
    public int c;
    public int m;
    public int[] tnet=new int[100];
    public int[] ftnet=new int[100];
    public int fnum=0;
    public int[] f=new int[100];
    public int starttime=0;
    public int finishtime=0;
    public boolean isCalc=false;


    public void deletechildEdge(int child){
        int t=0;
        for (int i=0;i<this.childnumber;i++)
            if (this.child[i]==child){
                t=i;
            }
        for (int i=t;i<this.childnumber-1;i++) {
            this.child[i] = this.child[i + 1];
            this.tnet[i]=this.tnet[i+1];
        }
        this.child[this.childnumber-1]=0;
        this.tnet[this.childnumber-1]=0;
        this.childnumber--;
    }

    public  void deletefatherEdge(int father){
        int t=0;
        for (int i=0;i<this.fnum;i++)
            if (this.f[i]==father){
                t=i;
            }
        for (int i=t;i<this.fnum-1;i++) {
            this.f[i] = this.f[i + 1];
            this.ftnet[i]=this.ftnet[i+1];
        }
        this.f[this.fnum-1]=0;
        this.ftnet[this.fnum-1]=0;
        this.fnum--;
    }

    public  void addchildEdge(int b,int w){
        this.child[this.childnumber]=b;
        this.tnet[this.childnumber]=w;
        this.childnumber++;
    }
    public  void addfatherEdge(int b,int w){
        this.f[this.fnum]=b;
        this.ftnet[this.fnum]=w;
        this.fnum++;
    }

    public static Task[] taskcopy(Task[] task) {
        Task[] newtask=new Task[task.length];
        for (int i=0;i<task.length;i++){
            try {
                newtask[i]= (Task) task[i].clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newtask;
    }

    public Object clone() {
        Task task = null;
        try {
            task = (Task) super.clone();
            task.child=child.clone();//深度clone
            task.f=f.clone();
            task.tnet=tnet.clone();
            task.ftnet=ftnet.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return task;
    }



}


