package com.nordeus.Backend.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.LinkedList;
import java.util.Queue;


@RestController
@RequestMapping("/grid")
public class GridController {

  private final String EXTERNAL_API_URL = "https://jobfair.nordeus.com/jf24-fullstack-challenge/test";

  @GetMapping("")
  public TwoMatricesResponse  getGridData() {
    RestTemplate restTemplate = new RestTemplate();
    String response = restTemplate.getForObject(EXTERNAL_API_URL, String.class);
    int[][] matrixGrid1 = parseGridData(response);
    double[][] matrixGrid2 = makeAverageGridMatrix(matrixGrid1);
    TwoMatricesResponse tmr = new TwoMatricesResponse(matrixGrid1,matrixGrid2);
    return tmr;
  }


  private int[][] parseGridData(String gridData) {
    int[][] grid = new int[30][30];
    String[] rows = gridData.trim().split("\n");

    for (int i = 0; i < rows.length; i++) {
      String[] values = rows[i].trim().split(" ");
      for (int j = 0; j < values.length; j++) {
        grid[i][j] = Integer.parseInt(values[j]);
      }
    }
    return grid;
  }

  private double[][] makeAverageGridMatrix(int[][] matrixGrid)
  {
    double[][] averageGridMatrix = new double[30][30];
    int[][] visitedMatrix = new int[30][30];

    for (int i = 0; i < visitedMatrix.length; i++) {
      for (int j = 0; j < visitedMatrix[i].length; j++) {
        visitedMatrix[i][j] = 0;
      }
    }

    for(int i=0;i<30;i++)
    {
      for(int j=0;j<30;j++)
      {
        if(visitedMatrix[i][j]==0 && matrixGrid[i][j]!=0)
        {
            setAverage(i,j,matrixGrid,visitedMatrix,averageGridMatrix);
        }
      }
    }
    return averageGridMatrix;
  }


  void setAverage(int x, int y, int[][] matrix,int [][]visitedMatrix,double[][] averageGridMatrix)
  {
    Queue<Pair<Integer,Integer>> queue = new LinkedList<>();
    queue.add(new Pair<>(x,y));
    double sum = 0;
    int cnt = 0;
    while(!queue.isEmpty())
    {
      Queue<Pair<Integer,Integer>> pom = new LinkedList<>();
      while(!queue.isEmpty())
      {
        int i = queue.peek().first;
        int j = queue.peek().second;
        sum+=matrix[i][j];
        cnt++;
        visitedMatrix[i][j] = 1;
        queue.poll();
        if(i+1<30 && matrix[i+1][j]!=0 && visitedMatrix[i+1][j]==0)
        {
          pom.add(new Pair<>(i+1,j));
        }
        if(i-1>=0 && matrix[i-1][j]!=0 && visitedMatrix[i-1][j]==0)
        {
          pom.add(new Pair<>(i-1,j));
        }
        if(j+1<30 && matrix[i][j+1]!=0 && visitedMatrix[i][j+1]==0)
        {
          pom.add(new Pair<>(i,j+1));
        }
        if(j-1>=0 && matrix[i][j-1]!=0 && visitedMatrix[i][j-1]==0)
        {
          pom.add(new Pair<>(i,j-1));
        }
      }
      queue=pom;
    }

    double average = sum/cnt;

    queue.add(new Pair<>(x,y));

    while(!queue.isEmpty())
    {
      Queue<Pair<Integer,Integer>> pom = new LinkedList<>();
      while(!queue.isEmpty())
      {
        int i = queue.peek().first;
        int j = queue.peek().second;
        averageGridMatrix[i][j] = average;
        visitedMatrix[i][j]=2;
        queue.poll();

        if(i+1<30 && matrix[i+1][j]!=0 && visitedMatrix[i+1][j]==1)
        {
          pom.add(new Pair<>(i+1,j));
        }
        if(i-1>=0 && matrix[i-1][j]!=0 && visitedMatrix[i-1][j]==1)
        {
          pom.add(new Pair<>(i-1,j));
        }
        if(j+1<30 && matrix[i][j+1]!=0 && visitedMatrix[i][j+1]==1)
        {
          pom.add(new Pair<>(i,j+1));
        }
        if(j-1>=0 && matrix[i][j-1]!=0 && visitedMatrix[i][j-1]==1)
        {
          pom.add(new Pair<>(i,j-1));
        }
      }
      queue=pom;
    }

  }

  public static class TwoMatricesResponse {
    private int[][] matrix1;
    private double[][] matrix2;

    public TwoMatricesResponse(int[][] matrix1, double[][] matrix2) {
      this.matrix1 = matrix1;
      this.matrix2 = matrix2;
    }

    public int[][] getMatrix1() {
      return matrix1;
    }

    public double[][] getMatrix2() {
      return matrix2;
    }
  }


  public class Pair<T, U> {
    public final T first;
    public final U second;

    public Pair(T first, U second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public String toString() {
      return "(" + first + ", " + second + ")";
    }
  }

}
