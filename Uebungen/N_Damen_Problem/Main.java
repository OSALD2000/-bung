package N_Damen_Problem;


import java.util.ArrayList;

class PartitionManager
{
    private int N;
    private ArrayList<int[]> partitions;

    public PartitionManager(int N)
    {
        this.N = N;
    }

    public ArrayList<int[]> createPartition()
    {
        partitions = new ArrayList<>();

        for (int i = 0; i < this.N; i++)
        {
            int[] partition = new int[this.N];

            for (int j = 0; j < this.N; j++){
                partition[j] = -1;
            }

            partition[0] = i;


            for (int col = 0; col < this.N; col++)
            {
                    if (!(partition[0] == col || partition[0] - 1 == col || partition[0] + 1 == col))
                    {
                        var copy_partition = new int[this.N];
                        System.arraycopy(partition, 0, copy_partition, 0, this.N);
                        copy_partition[1] = col;
                        partitions.add(copy_partition);
                    }
            }
        }

        return partitions;
    }
}


class Solver extends Thread
{
    public int conter = 0;
    private int[] initBoard;

    @Override
    public void run()
    {
        _solveProblem(initBoard, 2);
        System.out.println(Thread.currentThread().getName() + " Total conter :  " + conter);
    }

    public Solver(int[] board, String name)
    {
        super(name);
        initBoard = board.clone();
    }


    private void _solveProblem(int [] board, int row)
    {
        if(row == board.length)
        {
            conter += 1;
        } else
        {
            for (int col = 0; col < board.length; col++)
            {
                if (isSave(board, row, col))
                {
                    board[row] = col;
                    _solveProblem(board, row + 1);
                    board[row] = -1;
                }
            }
        }
    }

    private boolean isSave(int[]board, int row, int col)
    {
        for (int i = 0; i < row; i++)
        {
            if (board[i] == col || board[i] - i == col - row || board[i] + i == col + row)
            {
                return false;
            }
        }
        return true;
    }
}



public class Main
{
    public static void main(String[] args)
    {
        PartitionManager pm = new PartitionManager(8);
        var partitions = pm.createPartition();

        ArrayList<Solver> solvers = new ArrayList<>();

        int id = 1;
        for (var partition : partitions)
        {
            String name = ("Thread number   " + (id<10 ? "0" : "") +id);
            var solver = new Solver(partition, name);
            solvers.add(solver);

            id+=1;
        }

        solvers.forEach(Thread::start);


        solvers.forEach(solver -> {
            try {
                solver.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        int conter = 0;

        for (Solver solver : solvers)
        {
            conter += solver.conter;
        }

        System.out.println("conter: " + conter);
    }
}
