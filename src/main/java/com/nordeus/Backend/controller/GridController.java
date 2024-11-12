package com.nordeus.Backend.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.LinkedList;
import java.util.Queue;


@RestController
@RequestMapping("/grid")
@CrossOrigin(origins = "https://your-netlify-app-url.com")
public class GridController {

  private final String EXTERNAL_API_URL = "https://jobfair.nordeus.com/jf24-fullstack-challenge/test";

  @GetMapping("")
  public TwoMatricesResponse  getGridData() {
    RestTemplate restTemplate = new RestTemplate();
    String response = restTemplate.getForObject(EXTERNAL_API_URL, String.class);
    int[][] matrixGrid1 = parseGridData(response);



    return getAverage(matrixGrid1);
  }


  private TwoMatricesResponse getAverage(int [][] matrix1)
  {
    double[][] matrix2 = new double[30][30];
    int[][] visited = new int[30][30];
    for (int i = 0; i < 30; i++) {
      for (int j = 0; j < 30; j++) {
        visited[i][j] = 0;
      }
    }
    double max=0;
    for (int i = 0; i < 30; i++) {
      for (int j = 0; j < 30; j++) {
        if (matrix1[i][j] > 0 && visited[i][j]==0) {
          double average = calculateIslandAverageAndSetValue(i, j, matrix1,matrix2, visited);
          if(average > max)
            max = average;
        }
      }
    }

    return new TwoMatricesResponse(matrix1,matrix2,max);
  }

  double calculateIslandAverageAndSetValue(int x, int y, int[][] matrix1, double[][] matrix2, int[][] visited) {
    Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
    queue.add(new Pair<>(x, y));
    visited[x][y] = 1;

    double sum = 0;
    int count = 0;
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    while (!queue.isEmpty()) {
      Pair<Integer, Integer> p = queue.poll();
      sum += matrix1[p.first][p.second];
      count++;

      for (int[] dir : directions) {
        int nx = p.first + dir[0];
        int ny = p.second + dir[1];

        if (nx >= 0 && nx < 30 && ny >= 0 && ny < 30 && matrix1[nx][ny] > 0 && visited[nx][ny] == 0) {
          queue.add(new Pair<>(nx, ny));
          visited[nx][ny] = 1;
        }
      }
    }

    double average = sum / count;

    queue.add(new Pair<>(x, y));
    visited[x][y] = 2;

    while (!queue.isEmpty()) {
      Pair<Integer, Integer> p = queue.poll();
      matrix2[p.first][p.second] = average;

      for (int[] dir : directions) {
        int nx = p.first + dir[0];
        int ny = p.second + dir[1];

        if (nx >= 0 && nx < 30 && ny >= 0 && ny < 30 && matrix1[nx][ny] > 0 && visited[nx][ny] == 1) {
          queue.add(new Pair<>(nx, ny));
          visited[nx][ny] = 2;
        }
      }
    }

    return average;
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

  public static class TwoMatricesResponse {
    private int[][] matrix1;
    private double[][] matrix2;

    private double max ;

    public TwoMatricesResponse(int[][] matrix1, double[][] matrix2,double max) {
      this.matrix1 = matrix1;
      this.matrix2 = matrix2;
      this.max = max;
    }

    public int[][] getMatrix1() {
      return matrix1;
    }

    public double[][] getMatrix2() {
      return matrix2;
    }

    public double getMax() {
      return max;
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
