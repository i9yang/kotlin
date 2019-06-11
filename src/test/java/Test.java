import java.util.LinkedList;
import java.util.Scanner;

public class Test {
    static class Matrix {
        int v = 0;
        boolean b = false;

        Matrix(int v, boolean b) {
            this.v = v;
            this.b = b;
        }
    }

    public static void main(String[] arags){
        Scanner scanner = new Scanner(System.in);

        String[] init = scanner.nextLine().split(" ");

        int mx = Integer.parseInt(init[0]);
        int my = Integer.parseInt(init[1]);

        Matrix[][] matrix = new Matrix[mx][my];

        for(int idx = 0;idx<mx;idx++) {
            String v = scanner.nextLine();
            for(int i=0;i<v.length();i++) {
                matrix[idx][i] = new Matrix(Integer.parseInt(v.split("")[i]), false);
            }
        }

        LinkedList<Integer> queue = new LinkedList<>();
        queue.push(0);
        int result = 1;

        while(true) {
            boolean out = false;
            int size = queue.size();
            for(int o=0;o<size;o++) {
                int v = queue.poll();
                int x = v / 100;
                int y = v % 100;

                if(x == (mx-1) && y == (my-1)) {
                    out = true;
                    break;
                }
                matrix[x][y].b = true;

                if(x-1 >= 0 && matrix[x-1][y].v != 0 && !matrix[x-1][y].b) queue.add((x-1) * 100 + y);
                if(y-1 >= 0 && matrix[x][y-1].v != 0 && !matrix[x][y-1].b) queue.add((x) * 100 + y-1);
                if(x+1 <= mx-1 && matrix[x+1][y].v != 0 && !matrix[x+1][y].b) queue.add((x+1) * 100 + y);
                if(y+1 <= my-1 && matrix[x][y+1].v != 0 && !matrix[x][y+1].b) queue.add((x) * 100 + y+1);

//                queue.stream().forEach(i -> {System.out.print(i + " ");});
//                System.out.println();
            }

            if(out) break;
//            System.out.println("===================");
            result++;
        }

        System.out.println(result);
    }
}
