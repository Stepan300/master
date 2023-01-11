//package main;
//
//import main.model.Task;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class Storage {
//
//    private static ConcurrentHashMap<Integer, Task> tasks = new ConcurrentHashMap<>();
//    private static int currantId = 1;
//
//    public static int addTask(Task task) {
//        task.setId(currantId);
//        int id = currantId;
//        currantId += 2;
//        if (task.getStatus() == null) {
//            task.setStatus();
//        }
//        if (task.getPriority() == null) {
//            task.setPriority();
//        }
//        if (task.getExecutor().getName() == null) {
//            task.setRandomExecutorName();
//        }
//        if (task.getExecutor().getPosition() == null) {
//            task.setRandomExecutorPosition();
//        }
//        tasks.put(id, task);
//        return id;
//    }
//
//    public static List<Task> getAllTasks() {
//        return new ArrayList<>(tasks.values());
//    }
//
//    public static Task getTask(int idTask) {
//        if (tasks.containsKey(idTask)) {
//            return tasks.get(idTask);
//        }
//        return null;
//    }
//
//    public static boolean updateTask(int id, Task task) {
//        if (tasks.containsKey(id)) {
//            if (task.getStatus() == null) {
//                task.setStatus();
//            }
//            if (task.getPriority() == null) {
//                task.setPriority();
//            }
//            task.setId(id);
//            if (task.getExecutor().getName() == null) {
//                task.setRandomExecutorName();
//
//            }
//            if (task.getExecutor().getPosition() == null) {
//                task.setRandomExecutorPosition();
//            }
//            tasks.put(id, task);
//            return true;
//        } else {
//            System.out.println("Attention!!! The element with index №" + id + " is not found");
//        }
//        return false;
//    }
//
//    public static boolean deleteTask(int id) {
//        boolean confirmation = false;
//        if (tasks.get(id) != null) {
//            tasks.remove(id);
//            confirmation = true;
//        } else {
//            System.out.println("Attention!!! The element with index №" + id + " is absent");
//        }
//        return confirmation;
//    }
//
//    public static void deleteTasks() {
//        tasks.clear();
//    }
//}
