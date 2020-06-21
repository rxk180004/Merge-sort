/** Sample starter code for SP9.
 * Team:
 * Phanindra Pydisetty (pxp180031)
 * Sudeep Maity
 */

package pxp180031;

import java.util.Random;

public class MergeSort {
  public static Random random = new Random();
  public static int numTrials = 100;
  public static int threshold = 20;

  public static void main(String[] args) {
    int n = 8000000;
    int choice = 2;
    if (args.length > 0) {
      n = Integer.parseInt(args[0]);
    }
    if (args.length > 1) {
      choice = Integer.parseInt(args[1]);
    }
    int[] arr = new int[n];
    for (int i = 0; i < n; i++) {
      arr[i] = i;
    }
    Timer timer = new Timer();
    switch (choice) {
    case 1:
      Shuffle.shuffle(arr);
      numTrials = 1;
      insertionSort(arr);

      break;
    case 2:
      for (int i = 0; i < numTrials; i++) {
        Shuffle.shuffle(arr);
        // Shuffle.printArray(arr, "unsorted array");
        mergeSort1(arr);
        // Shuffle.printArray(arr, "sorted array");
      }
      break;
    case 3:
      for (int i = 0; i < numTrials; i++) {
        Shuffle.shuffle(arr);
        mergeSort2(arr);
      }
      break;
    case 4:
      for (int i = 0; i < numTrials; i++) {
        Shuffle.shuffle(arr);
        mergeSort3(arr);
      }
      break;
    }
    timer.end();
    timer.scale(numTrials);

    System.out.println("Choice: " + choice + "\n" + timer);
  }

  public static void insertionSort(int[] arr) {
    insertionSort(arr, 0, arr.length - 1);
  }

  public static void insertionSort(int[] arr, int start, int end) {
    for (int i = start; i <= end; i++) {
      int temp = arr[i];
      int j = i - 1;
      while (j >= start && temp < arr[j]) {
        arr[j + 1] = arr[j];
        j = j - 1;
      }
      arr[j + 1] = temp;
    }
  }
  /**
   * Take 1
   * Normal merge sort (text book method!)
   */
  public static void merge1(int[] arr, int start, int mid, int end) {
    int[] left = new int[mid - start + 1];
    int[] right = new int[end - mid];
    System.arraycopy(arr, start, left, 0, mid - start + 1);
    System.arraycopy(arr, mid + 1, right, 0, end - mid);
    for (int i = 0, j = 0, k = start; k <= end; k++) {
      if (j >= right.length || (i < left.length && left[i] <= right[j])) arr[k] = left[i++];
      else arr[k] = right[j++];
    }
  }

  private static void mergeSort1(int[] arr, int start, int end) {
    if (start < end) {
      int mid = (start + end) / 2;
      mergeSort1(arr, start, mid);
      mergeSort1(arr, mid + 1, end);
      merge1(arr, start, mid, end);
    }
  }

  public static void mergeSort1(int[] arr) {
    mergeSort1(arr, 0, arr.length - 1);
  }

  /**
   * Take 2
   * 1. Pass temporary array once instead of creating again and again in merge
   * 2. When size less than threshold, use insertion sort
   */
  public static void merge2(int[] arr, int[] buffer, int start, int mid, int end) {
    System.arraycopy(arr, start, buffer, start, end - start + 1);
    for (int i = start, j = mid + 1, k = start; k <= end; k++) {
      if (j > end || (i <= mid && buffer[i] <= buffer[j])) arr[k] = buffer[i++];
      else arr[k] = buffer[j++];
    }
  }

  private static void mergeSort2(int[] arr, int[] buffer, int start, int n) {
    if (n < threshold) {
      insertionSort(arr, start, start + n - 1);
    } else {
      int len = n / 2;
      mergeSort2(arr, buffer, start, len);
      mergeSort2(arr, buffer, start + len, n - len);
      merge2(arr, buffer, start, start + len - 1, start + n - 1);
    }
  }

  public static void mergeSort2(int[] arr) {
    int[] buffer = new int[arr.length];
    mergeSort2(arr, buffer, 0, arr.length);
  }

  /**
   * Take 3
   * 1. Avoid unnecessary copying of values between the arrays.
   * Copy the full array into buffer once and use it for sorting
   *
   * 2. Use a while instead of for loop!
   */
  public static void merge3(int[] arr, int[] buffer, int start, int mid, int end) {
    int i = start, j = mid + 1, k = start;
    while (i <= mid && j <= end) {
      if (buffer[i] <= buffer[j]) arr[k++] = buffer[i++];
      else arr[k++] = buffer[j++];
    }
    while (i <= mid) {
      arr[k++] = buffer[i++];
    }
    while (j <= end) {
      arr[k++] = buffer[j++];
    }
  }

  private static void mergeSort3(int[] arr, int[] buffer, int start, int n) {
    if (n < threshold) {
      insertionSort(arr, start, start + n - 1);
    } else {
      int len = n / 2;
      mergeSort3(buffer, arr, start, len);
      mergeSort3(buffer, arr, start + len, n - len);
      merge3(arr, buffer, start, start + len - 1, start + n - 1);
    }
  }

  public static void mergeSort3(int[] arr) {
    int[] buffer = new int[arr.length];
    System.arraycopy(arr, 0, buffer, 0, arr.length);
    mergeSort3(arr, buffer, 0, arr.length);
  }


  /**
   * Timer class for roughly calculating running time of programs
   *
   * @author rbk Usage: Timer timer = new Timer(); timer.start(); timer.end();
   *         System.out.println(timer); // output statistics
   */

  public static class Timer {
    long startTime, endTime, elapsedTime, memAvailable, memUsed;
    boolean ready;

    public Timer() {
      startTime = System.currentTimeMillis();
      ready = false;
    }

    public void start() {
      startTime = System.currentTimeMillis();
      ready = false;
    }

    public Timer end() {
      endTime = System.currentTimeMillis();
      elapsedTime = endTime - startTime;
      memAvailable = Runtime.getRuntime().totalMemory();
      memUsed = memAvailable - Runtime.getRuntime().freeMemory();
      ready = true;
      return this;
    }

    public long duration() {
      if (!ready) {
        end();
      }
      return elapsedTime;
    }

    public long memory() {
      if (!ready) {
        end();
      }
      return memUsed;
    }

    public void scale(int num) {
      elapsedTime /= num;
    }

    public String toString() {
      if (!ready) {
        end();
      }
      return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed / 1048576) + " MB / "
          + (memAvailable / 1048576) + " MB.";
    }
  }

  /**
   * @author rbk : based on algorithm described in a book
   */

  /* Shuffle the elements of an array arr[from..to] randomly */
  public static class Shuffle {

    public static void shuffle(int[] arr) {
      shuffle(arr, 0, arr.length - 1);
    }

    public static <T> void shuffle(T[] arr) {
      shuffle(arr, 0, arr.length - 1);
    }

    public static void shuffle(int[] arr, int from, int to) {
      int n = to - from + 1;
      for (int i = 1; i < n; i++) {
        int j = random.nextInt(i);
        swap(arr, i + from, j + from);
      }
    }

    public static <T> void shuffle(T[] arr, int from, int to) {
      int n = to - from + 1;
      // Random random = new Random();
      for (int i = 1; i < n; i++) {
        int j = random.nextInt(i);
        swap(arr, i + from, j + from);
      }
    }

    static void swap(int[] arr, int x, int y) {
      int tmp = arr[x];
      arr[x] = arr[y];
      arr[y] = tmp;
    }

    static <T> void swap(T[] arr, int x, int y) {
      T tmp = arr[x];
      arr[x] = arr[y];
      arr[y] = tmp;
    }

    public static void printArray(int[] arr, String message) {
      printArray(arr, 0, arr.length - 1, message);
    }

    public static void printArray(int[] arr, int from, int to, String message) {
      System.out.print(message);
      for (int i = from; i <= to; i++) {
        System.out.print(" " + arr[i]);
      }
      System.out.println();
    }
  }
}
