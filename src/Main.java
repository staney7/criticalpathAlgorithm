import java.io.File;
import java.util.*;

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
        return Arrays.copyOfRange(task, entryTask, exitTask + 1);
    }

    public void ToTree(Task[] task) {
        for (int i = 0; i < task.length; i++) {
            Task currentTask = task[i];
            if (currentTask.fnum >1) {
                double maxfnet = 0;
                int maxfid = 0;
                for (int j = 0; j < currentTask.fnum; j++) {
                    Task ftask = task[currentTask.f[j]];
                    if (maxfnet < currentTask.ftnet[j]) {
                        maxfnet = currentTask.ftnet[j];
                        maxfid = j;
                    }
                }
                /**
                 * delete edge which is not between fathertask and currenttask
                 */
                Task taska=(Task)currentTask.clone();
                for (int j = 0; j < taska.fnum; j++) {
                    if (j != maxfid) {
//                        System.out.println(i+" "+j);
                        Task ftask = task[taska.f[j]];
                        currentTask.deletefatherEdge(ftask.taskid);
                        ftask.deletechildEdge(taska.taskid);
                    }
                }
            }
        }
    }

    /**
     * sortTaskGraph by total runtime in Mobile;
     *
     * @param task   taskgraph
     * @param taskid currenttaskid
     * @return total runtime in Mobile
     */
    public double SortTaskGraph(Task[] task, int taskid) {
        Task currentTask = task[taskid];
        double[] m = new double[100];
        double totalm = 0;
        for (int i = 0; i < currentTask.childnumber; i++)
            if (i < task.length - 1) {
                m[i] = SortTaskGraph(task, currentTask.child[i]);
                totalm += m[i];
            }
        for (int i = 0; i < currentTask.childnumber - 1; i++)
            for (int j = i + 1; j < currentTask.childnumber; j++)
                if (m[i] < m[j]) {
                    double temp = m[i];
                    m[i] = m[j];
                    m[j] = temp;
                    int temp1 = currentTask.child[i];
                    currentTask.child[i] = currentTask.child[j];
                    currentTask.child[j] = temp1;
                }
        totalm += currentTask.m;
        return totalm;
    }

    public void setPositon(Task[] task, int taskid, int r) {
        Task currentTask = task[taskid];
        currentTask.r = r;
        for (int i = 0; i < currentTask.childnumber; i++) {
            setPositon(task, currentTask.child[i], r);
        }
    }

    public double TM(Task[] task) {
        double tm = 0;
        for (int i = 0; i < task.length; i++) {
            if (task[i].r == 1) tm += task[i].m;
        }
        return tm;
    }

    public double TC(Task[] task, List<Integer> Ventry) {
        double tc = 0;
        for (int i = 0; i < task.length; i++) {
            if (task[i].r == 0) tc += task[i].c;
        }
        double tq = 0;
        return tc + tq;
    }

    public double TNET(Task[] task, List<Integer> Ventry) {
        double tnet = 0;
        for (int i = 0; i < Ventry.size(); i++) {
            Task currenttask = task[Ventry.get(i)];
            int ftask = currenttask.f[0];
            tnet += task[ftask].tnet[0];
        }
        return tnet;
    }

    /**
     * calculate the task in cloud
     *
     * @param task taskgraph;
     */
    public double ConcurrentTask(Task[] task, int r) {
        /**
         * reduce the graph into tree and
         * sort by total runtime if task in mobile
         */
        Task[] taskcopy = Task.taskcopy(task);
        task[0].r = -1;
        task[task.length - 1].r = -1;
        Task taska = (Task) task[0].clone();
        for (int i = 0; i < taska.childnumber; i++) {
            task[taska.child[i]].deletefatherEdge(0);
            task[0].deletechildEdge(taska.child[i]);
        }
        taska = (Task) task[task.length - 1].clone();
        for (int i = 0; i < taska.fnum; i++) {
            task[taska.f[i]].deletechildEdge(taska.taskid);
            task[task.length - 1].deletefatherEdge(taska.f[i]);
        }
        ToTree(task);
        SortTaskGraph(task, 1);
        /**
         * algorithm 2: offloading concurrent Subgraph
         */
        int currentr=task[1].r;
        for (int i = 1; i < task.length - 1; i++) task[i].r = r;
        List<Integer> Ventry = new ArrayList<>();
        Task currentTask = task[1];
        while (currentTask.childnumber != 0) {
            int i = 0;
            while ((2 * currentTask.r - 1) * (TM(task) - TC(task, Ventry)) > TNET(task, Ventry)) {
                Task childtask = task[currentTask.child[i]];
                if (currentTask.r == 1) {
                    setPositon(task, childtask.taskid, 0);
                } else {
                    setPositon(task, childtask.taskid, 1);
                }
                Ventry.add(currentTask.child[i]);
                if (i==currentTask.childnumber-1) break;
                i++;
            }
            if (currentTask.r == 1) {
                Ventry.add(currentTask.child[i]);
            } else {
                setPositon(task, currentTask.child[i], 1);
            }
            for (int j = 0; j < Ventry.size(); j++) {
                if (Ventry.get(j) == currentTask.child[i]) ;
                Ventry.remove(j);
            }
            currentTask = task[currentTask.child[i]];
        }

//        for (int i = 0; i < task.length; i++) {
//            task[i].r = task[i].r;
//        }
        task[0].r = 1;
        task[task.length - 1].r = 1;
        for (int i = 0; i < taskcopy.length; i++) {
            taskcopy[i].r = task[i].r;
            System.out.print(task[i].r + " ");
        }
        double calc = CalcTimeByTaskGraph(taskcopy);
        return calc;
    }


    public double simplefuntion(Task[] task) {
        double minTime = Integer.MAX_VALUE;
        int temp = 1;
        for (int i = 0; i < task.length - 2; i++) {
            if (temp > 1000000000) break;
            temp *= 2;
        }
        int start = (int) System.currentTimeMillis();
        for (int i = 0; i < temp; i++) {
            int t = i;
            if (temp % 1000 == 0) {
                int end = (int) System.currentTimeMillis();
                if (end - start >= 2000) {
                    System.out.println(end - start);
                    break;
                }
            }
            for (int j = 1; j < task.length - 1; j++) {
                task[j].r = t % 2;
                t = t / 2;
            }
            double calctime = CalcTimeByTaskGraph(Task.taskcopy(task));
//                for (int j=0;j<task.length;j++) System.out.print(task[j].r+" ");
//                System.out.println(calctime);
            if (minTime > calctime) {
                minTime = calctime;
            }
        }
        return minTime;
    }

    public double CalcTimeByTaskGraph(Task[] task) {
        double totaltime = 0;
        int[] rd = new int[task.length];
        Task currentTask = null;
        for (int i = 0; i < task.length; i++) {
            rd[i] = task[i].fnum;
            task[i].starttime = 0;
            task[i].finishtime = 0;
            task[i].isCalc = false;
        }
        while (true) {
            double minfinishtime = Integer.MAX_VALUE;
            for (int i = 0; i < task.length; i++) {
                if (task[i].finishtime < minfinishtime && !task[i].isCalc && rd[task[i].taskid] == 0) {
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
                    rd[aTask.taskid]++;
                    currentTask.addchildEdge(aTask.taskid, 0);
                    aTask.addfatherEdge(currentTask.taskid, 0);
                }
            }
            for (int i = 0; i < currentTask.childnumber; i++) {
                Task childtask = task[currentTask.child[i]];
                childtask.starttime = Math.max(childtask.starttime,
                        currentTask.finishtime + Math.abs(childtask.r - currentTask.r) * currentTask.tnet[i]);
                if (childtask.r == 0) childtask.finishtime = childtask.c + childtask.starttime;
                else childtask.finishtime = childtask.m + childtask.starttime;
            }
            for (int j = 0; j < currentTask.childnumber; j++) {
                rd[currentTask.child[j]]--;
            }
        }
        totaltime = task[task.length - 1].finishtime;
//        System.out.println(totaltime);
        return totaltime;
    }

    public Task[] readFile(String filename) {
        File file = new File(filename);
        Task[] task = null;
        try {
            Scanner scan = new Scanner(file);
            int n = scan.nextInt();
            task = new Task[n];

            for (int i = 0; i < n; i++) {
                task[i] = new Task();
                int w = scan.nextInt();
                int m = 1000;
                int c = 3000;
                task[i].m = (double) w / m;
                task[i].c = (double) w / c;
                task[i].taskid = i;
            }
            int m = scan.nextInt();
            for (int i = 0; i < m; i++) {
                int a = scan.nextInt();
                int b = scan.nextInt();
                int w = scan.nextInt();
                int r = 10000000;
                task[a].addchildEdge(b, (double) w / r);
                task[b].addfatherEdge(a, (double) w / r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    public void Calc(Task[] task) {
        double a = ConcurrentTask(Task.taskcopy(task), 0);
        System.out.println(a);
        double b = ConcurrentTask(Task.taskcopy(task), 1);
        System.out.println(b);
        System.out.printf("%.9f\n", (Math.min(a, b)));
    }

    public static void main(String[] args) {
        Main main = new Main();
//        Task[] task = main.readFile("src/graph.txt");
        Task[] task = main.readFile("src/meshgraph.txt");
        main.Calc(Task.taskcopy(task));
        System.out.println(main.simplefuntion(task));
        task = main.readFile("src/treegraph.txt");
        main.Calc(Task.taskcopy(task));
        System.out.println(main.simplefuntion(task));
        task = main.readFile("src/generalgraph.txt");
        main.Calc(Task.taskcopy(task));
        System.out.println(main.simplefuntion(task));

    }

}
