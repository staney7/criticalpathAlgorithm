import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Created by jay on 2016/11/14.
 */
public class TextCreate {
    private Random random = new Random(System.currentTimeMillis());

    public TextCreate(int tasknumber ,int p) {
        _createmeshtext(tasknumber);
        _createtreegraph(tasknumber);
        _creategeneralgraph(tasknumber,p);
    }

    private void _creategeneralgraph(int tasknumber,int p) {
        boolean[][] f = new boolean[30][30];
        File file = new File("src/generalgraph1.txt");
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(tasknumber + "\n");
            fw.write("0\n");
            for (int i = 1; i < tasknumber - 1; i++) {
                fw.write(random.nextInt(50)+50 + "\n");
            }
            fw.write("0\n");
            fw.flush();
            Task[] task = new Task[tasknumber];
            for (int i = 0; i < tasknumber; i++) task[i] = new Task();
            int h = random.nextInt(3) + 2;
            int len = (tasknumber - 4) / h;
//            System.out.println(len + " " + h);
            int temp=(p-1)*tasknumber;
            fw.write((tasknumber + h-2+temp) + "\n");
            _createEdge(fw,task,0,1,f);
            for (int k=0;k<h-1;k++) {
//                fw.write(1 + " " + (k*len+2) + " " + random.nextInt(100) + "\n");
                _createEdge(fw,task,1,k*len+2,f);
                for (int i = k*len+2; i < (k+1)*len+1; i++) {
//                    fw.write(i + " " + (i + 1) + " " + random.nextInt(100) + "\n");
                    _createEdge(fw,task,i,i+1,f);
                }
//                fw.write(((k+1)*len+2) + " " + (tasknumber - 2) + " " + random.nextInt(100) + "\n");
                _createEdge(fw,task,(k+1)*len+1,tasknumber-2,f);
            }
//            fw.write((tasknumber-2)+" "+(tasknumber-1)+" "+random.nextInt(100)+"\n");
            _createEdge(fw,task,1,(h-1)*len+2,f);
            for (int i=(h-1)*len+2;i<tasknumber-1;i++)
                _createEdge(fw,task,i,i+1,f);
            fw.flush();
            int i=0;
            while (i<temp) {
                int x = random.nextInt(tasknumber - 3) + 2;
                int y = random.nextInt(tasknumber - 3) + 2;
                int hx=(x-1)%len; if (hx==0) hx=len;
                int lenx=(x-2)/len;
                int leny=(y-2)/len;
                int hy=(y-1)%len; if (hy==0) hy=len;
                if (x>h*len+1) hx=len+1;
                if (y>h*len+1) hy=len+1;
                if(hx>hy){int t=x;x=y;y=t;}
                else if(hx==hy&&lenx>leny) {int t=x;x=y;y=t;}

//                System.out.println(x+" "+y);
                if (!f[x][y]&&x!=y){
                    _createEdge(fw,task,x,y,f);
                    i++;
                }
            }
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private boolean topo(Task[] task) {
//        boolean[] isvisited=new boolean[task.length];
//        int[] ru=new int[task.length];
//        for (int i=0;i<task.length;i++){
////            for (int j=0;j<task[i].childnumber;j++){
////                System.out.print(task[i].child[j]+" ");
////                ru[task[i].child[j]]++;
////            }
//            ru[i]=task[i].fnum;
//        }
//
//        int sum=0;
//        while (true){
//            int currenttaskid=-1;
//            for (int i=0;i<task.length;i++){
//                if (ru[i]==0&&!isvisited[i]){
//                    currenttaskid=i;
//                    isvisited[i]=true;
//                    sum++;
//                    break;
//                }
//            }
//            System.out.print(currenttaskid+" ");
//            if (currenttaskid==-1) break;
//            for (int i=0;i<task[currenttaskid].childnumber;i++){
//                ru[task[currenttaskid].child[i]]--;
//            }
//        }
//        System.out.println();
//        if (sum==task.length) return false;
//        else return true;
//    }

    private void _createEdge(FileWriter fw,  Task[] task, int x, int y,boolean[][] f) throws IOException {
        task[x].addchildEdge(y,0);
        task[y].addfatherEdge(x,0);
        if (x==0&&y==1) {
            fw.write(x + " " + y + " " + 100 + "\n");
        } else if(x==task.length-2&&y==task.length-1){
            fw.write(x + " " + y + " " + 100 + "\n");
        } else {
            fw.write(x + " " + y + " " + (random.nextInt(80) + 20) + "\n");
        }
        f[x][y]=true;
        f[y][x]=true;
//        System.out.println(x+" "+y);
    }

    private void _createtreegraph(int tasknumber) {
        File file = new File("src/treegraph1.txt");
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(tasknumber + "\n");
            fw.write("0\n");
            for (int i = 1; i < tasknumber - 1; i++) {
                fw.write((random.nextInt(50)+50) + "\n");
            }
            fw.write("0\n");
            fw.flush();
            fw.write((tasknumber / 2 + tasknumber - 2) + "\n");
            fw.write("0 1 " + 100 + "\n");
            for (int i = 1; i <= (tasknumber - 1) / 2; i++) {
                fw.write(i + " " + (2 * i) + " " + (random.nextInt(80)+20) + "\n");
                if (2 * i + 1 < tasknumber)
                    fw.write(i + " " + (2 * i + 1) + " " + (random.nextInt(80)+20) + "\n");
            }
            for (int i = (tasknumber - 1) / 2 + 1; i < tasknumber-1; i++) {
                fw.write(i + " " + (tasknumber - 1) + " " + (random.nextInt(80)+20) + "\n");
            }
            fw.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _createmeshtext(int tasknumber) {
        File file = new File("src/meshgraph1.txt");
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(tasknumber + "\n");
            fw.write("0\n");
            for (int i = 1; i < tasknumber - 1; i++) {
                fw.write((random.nextInt(50)+50) + "\n");
            }
            fw.write("0\n");
            fw.flush();

            int h = random.nextInt(3) + 2;
            int len = (tasknumber - 4) / h;
//            System.out.println(len + " " + h);
            fw.write((tasknumber + h-2) + "\n");
//            System.out.println(tasknumber+h-2);
            fw.write(0 + " " + (1) + " " + 100 + "\n");
            for (int k=0;k<h-1;k++) {
                fw.write(1 + " " + (k*len+2) + " " + (random.nextInt(80)+20) + "\n");
                for (int i = k*len+2; i < (k+1)*len+1; i++) {
                    fw.write(i + " " + (i + 1) + " " + (random.nextInt(80)+20) + "\n");
                }
                fw.write(((k+1)*len+1) + " " + (tasknumber - 2) + " " + (random.nextInt(80)+20) + "\n");
            }
            fw.write(1+" "+((h-1)*len+2)+" "+(random.nextInt(80)+20)+"\n");
            for (int i=(h-1)*len+2;i<tasknumber-2;i++)
            fw.write(i+" "+(i+1)+" "+(random.nextInt(80)+20)+"\n");
            fw.write(tasknumber-2+" "+(tasknumber-1)+" "+100+"\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        new TextCreate(15,2);
    }
}
