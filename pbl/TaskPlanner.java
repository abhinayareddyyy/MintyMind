package pbl;

public class TaskPlanner {

    public static class Task {
        public int id;
        public String name;
        public double timeRequired;
        public String deadline;
        public int priority;

        public Task(int id, String name, double timeRequired, String deadline, int priority) {
            this.id = id;
            this.name = name;
            this.timeRequired = timeRequired;
            this.deadline = deadline;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return String.format("%s (Priority: %d, Time: %.1fh)", name, priority, timeRequired);
        }
    }

    /**
     * Merge Sort Algorithm - Sorts tasks by priority in descending order
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public static Task[] mergeSort(Task[] tasks) {
        if (tasks == null || tasks.length <= 1) {
            return tasks;
        }

        Task[] result = new Task[tasks.length];
        mergeSortHelper(tasks, result, 0, tasks.length - 1);
        return result;
    }

    /**
     * Helper method for Merge Sort
     */
    private static void mergeSortHelper(Task[] tasks, Task[] result, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            // Sort left and right halves
            mergeSortHelper(tasks, result, left, mid);
            mergeSortHelper(tasks, result, mid + 1, right);

            // Merge the sorted halves
            merge(tasks, result, left, mid, right);
        }
    }

    /**
     * Merge two sorted subarrays
     */
    private static void merge(Task[] tasks, Task[] result, int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;

        while (i <= mid && j <= right) {
            if (tasks[i].priority >= tasks[j].priority) {
                result[k++] = tasks[i++];
            } else {
                result[k++] = tasks[j++];
            }
        }

        while (i <= mid) {
            result[k++] = tasks[i++];
        }

        while (j <= right) {
            result[k++] = tasks[j++];
        }

        // Copy result back to tasks array
        for (i = left; i <= right; i++) {
            tasks[i] = result[i];
        }
    }

    /**
     * Quick Sort Algorithm - Sorts tasks by priority
     * Time Complexity: O(n log n) average, O(n²) worst case
     * Space Complexity: O(log n) due to recursion
     */
    public static Task[] quickSort(Task[] tasks) {
        if (tasks == null || tasks.length <= 1) {
            return tasks;
        }

        Task[] result = new Task[tasks.length];
        // Copy elements to result array
        for(int i = 0; i < tasks.length; i++) {
            result[i] = tasks[i];
        }

        quickSortHelper(result, 0, result.length - 1);
        return result;
    }

    /**
     * Helper method for Quick Sort implementation
     */
    private static void quickSortHelper(Task[] tasks, int low, int high) {
        if (low < high) {
            int pi = partition(tasks, low, high);
            quickSortHelper(tasks, low, pi - 1);
            quickSortHelper(tasks, pi + 1, high);
        }
    }

    /**
     * Partition helper for Quick Sort - uses Lomuto partition scheme
     */
    private static int partition(Task[] tasks, int low, int high) {
        Task pivot = tasks[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (tasks[j].priority > pivot.priority) {
                i++;
                // Swap tasks[i] and tasks[j]
                Task temp = tasks[i];
                tasks[i] = tasks[j];
                tasks[j] = temp;
            }
        }

        // Swap tasks[i + 1] and tasks[high]
        Task temp = tasks[i + 1];
        tasks[i + 1] = tasks[high];
        tasks[high] = temp;

        return i + 1;
    }

    /**
     * Greedy Scheduling Algorithm
     * Selects maximum priority tasks that fit within 8-hour workday constraint
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(n)
     */
    public static Task[] greedySchedule(Task[] tasks, double maxHours) {
        if (tasks == null || tasks.length == 0) {
            return new Task[0];
        }

        // Sort tasks by priority in descending order
        Task[] sorted = mergeSort(tasks);

        // Count how many tasks can fit
        int count = 0;
        double totalTime = 0;
        for (Task task : sorted) {
            if (totalTime + task.timeRequired <= maxHours) {
                totalTime += task.timeRequired;
                count++;
            }
        }

        // Create result array
        Task[] scheduled = new Task[count];
        totalTime = 0;
        int idx = 0;

        for (Task task : sorted) {
            if (totalTime + task.timeRequired <= maxHours) {
                scheduled[idx++] = task;
                totalTime += task.timeRequired;
            }
        }

        return scheduled;
    }

    /**
     * Depth-First Search (DFS) for task dependency graph
     * Detects cycles in task dependencies
     * Time Complexity: O(V + E) where V is tasks and E is dependencies
     * graph is represented as adjacency list using 2D array
     */
    public static boolean hasCycle(int n, int[][] graph) {
        boolean[] visited = new boolean[n];
        boolean[] recStack = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                if (dfsCycle(i, graph, visited, recStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method for DFS cycle detection
     */
    private static boolean dfsCycle(int v, int[][] graph, boolean[] visited, boolean[] recStack) {
        visited[v] = true;
        recStack[v] = true;

        if (graph[v] != null) {
            for (int neighbor : graph[v]) {
                if (!visited[neighbor]) {
                    if (dfsCycle(neighbor, graph, visited, recStack)) {
                        return true;
                    }
                } else if (recStack[neighbor]) {
                    return true;
                }
            }
        }

        recStack[v] = false;
        return false;
    }

    /**
     * Breadth-First Search (BFS) for task execution order
     * Generates topological order using BFS (Kahn's algorithm)
     * Time Complexity: O(V + E)
     */
    public static int[] topologicalSort(int n, int[][] graph) {
        int[] inDegree = new int[n];

        // Calculate in-degrees
        for (int i = 0; i < n; i++) {
            if (graph[i] != null) {
                for (int j : graph[i]) {
                    inDegree[j]++;
                }
            }
        }

        // Queue for vertices with in-degree 0 (using array-based queue)
        int[] queue = new int[n];
        int queueSize = 0;

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue[queueSize++] = i;
            }
        }

        int[] result = new int[n];
        int resultSize = 0;
        int index = 0;

        while (index < queueSize) {
            int u = queue[index++];
            result[resultSize++] = u;

            if (graph[u] != null) {
                for (int v : graph[u]) {
                    inDegree[v]--;
                    if (inDegree[v] == 0) {
                        queue[queueSize++] = v;
                    }
                }
            }
        }

        // If result doesn't have all vertices, cycle exists
        if (resultSize != n) {
            return null; // Cycle detected
        }

        // Return only the valid part of the result array
        int[] finalResult = new int[resultSize];
        for (int i = 0; i < resultSize; i++) {
            finalResult[i] = result[i];
        }

        return finalResult;
    }

    /**
     * Analyze task priority distribution using selection algorithm concept
     * Finds median priority (kth smallest element)
     * Time Complexity: O(n) average case
     */
    public static int findMedianPriority(Task[] tasks) {
        if (tasks == null || tasks.length == 0) return 0;

        int[] priorities = new int[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            priorities[i] = tasks[i].priority;
        }

        // Simple insertion sort for small arrays
        for (int i = 1; i < priorities.length; i++) {
            int key = priorities[i];
            int j = i - 1;
            while (j >= 0 && priorities[j] > key) {
                priorities[j + 1] = priorities[j];
                j--;
            }
            priorities[j + 1] = key;
        }

        return priorities[priorities.length / 2];
    }

    /**
     * Calculate optimal schedule statistics
     */
    public static class ScheduleStats {
        public int totalTasks;
        public int scheduledTasks;
        public double totalTime;
        public double maxAvailableTime;
        public double efficiency;

        public ScheduleStats(int total, int scheduled, double time, double max) {
            this.totalTasks = total;
            this.scheduledTasks = scheduled;
            this.totalTime = time;
            this.maxAvailableTime = max;
            this.efficiency = (time / max) * 100;
        }

        @Override
        public String toString() {
            return String.format(
                "Schedule Statistics:\n" +
                "Total Tasks: %d\n" +
                "Scheduled: %d\n" +
                "Total Time: %.1fh / %.1fh\n" +
                "Efficiency: %.1f%%",
                totalTasks, scheduledTasks, totalTime, maxAvailableTime, efficiency
            );
        }
    }

    /**
     * Get schedule statistics for given tasks and max hours
     */
    public static ScheduleStats getScheduleStats(Task[] tasks, double maxHours) {
        Task[] scheduled = greedySchedule(tasks, maxHours);
        double totalTime = 0;
        for (Task task : scheduled) {
            totalTime += task.timeRequired;
        }
        return new ScheduleStats(tasks.length, scheduled.length, totalTime, maxHours);
    }

    // Main method for testing
    public static void main(String[] args) {
        // Create sample tasks array
        Task[] tasks = new Task[5];
        tasks[0] = new Task(1, "Code Review", 2.0, "2026-04-17", 5);
        tasks[1] = new Task(2, "Database Design", 3.5, "2026-04-17", 4);
        tasks[2] = new Task(3, "Testing", 1.5, "2026-04-17", 3);
        tasks[3] = new Task(4, "Documentation", 2.0, "2026-04-17", 2);
        tasks[4] = new Task(5, "Bug Fixes", 1.0, "2026-04-17", 5);

        System.out.println("=== MintyMind DAA Algorithms ===\n");

        // Test Merge Sort
        System.out.println("Merge Sort Results (by priority):");
        Task[] mergeSorted = mergeSort(tasks);
        for (Task t : mergeSorted) {
            System.out.println("  " + t);
        }

        // Test Quick Sort
        System.out.println("\nQuick Sort Results (by priority):");
        Task[] quickSorted = quickSort(tasks);
        for (Task t : quickSorted) {
            System.out.println("  " + t);
        }

        // Test Greedy Scheduling
        System.out.println("\nGreedy Schedule (8-hour workday):");
        Task[] scheduled = greedySchedule(tasks, 8.0);
        double totalTime = 0;
        for (Task t : scheduled) {
            System.out.println("  " + t);
            totalTime += t.timeRequired;
        }
        System.out.printf("Total scheduled time: %.1fh\n", totalTime);

        // Test Statistics
        System.out.println("\n" + getScheduleStats(tasks, 8.0));

        // Test Median Priority
        System.out.printf("\nMedian Priority: %d\n", findMedianPriority(tasks));

        // Test Graph algorithms
        System.out.println("\n=== Graph Algorithms ===");
        int n = 4;
        int[][] graph = new int[4][];
        graph[0] = new int[]{1, 2};
        graph[1] = new int[]{3};
        graph[2] = new int[]{3};
        graph[3] = new int[]{};

        System.out.println("Has Cycle (should be false): " + hasCycle(n, graph));

        int[] topo = topologicalSort(n, graph);
        System.out.print("Topological Order: ");
        for (int vertex : topo) {
            System.out.print(vertex + " ");
        }
        System.out.println();
    }
}
