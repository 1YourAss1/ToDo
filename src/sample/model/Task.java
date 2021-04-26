package sample.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    private boolean done;
    private String priority, toDate;
    private String task;
    private final ArrayList<String> projects;
    private final ArrayList<String> tags;

    public Task(String strTask) {
        Matcher matcher;
        projects = new ArrayList<>();
        tags = new ArrayList<>();
        ArrayList<String> splitStrTask = new ArrayList<>(Arrays.asList(strTask.trim().split(" ")));

        // Parsing
        // Parse isDone
        if (splitStrTask.get(0).equals("x")) {
            done = true;
            splitStrTask.remove(0);
        } else {
            done = false;
        }

        StringBuilder taskBuilder = new StringBuilder();
        for (String word: splitStrTask) {
            // Parse priority
            matcher = Pattern.compile("\\([A-Z]\\)").matcher(word);
            if (matcher.find()) { priority = matcher.group(); continue; }

            // Parse projects
            matcher = Pattern.compile("^\\+").matcher(word);
            if (matcher.find()) { projects.add(word.substring(1)); continue; }

            // Parse labels
            matcher = Pattern.compile("^@").matcher(word);
            if (matcher.find()) { tags.add(word.substring(1)); continue; }

            // Parse toDate
            matcher = Pattern.compile("^due:").matcher(word);
            if (matcher.find()) { toDate = word.substring(4); continue; }

            taskBuilder.append(word).append(" ");
        }
        task = taskBuilder.toString().trim();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean val) {
        done = val;
    }

    public String getStringPriority() {
        return priority;
    }

    public int getIntPriority() {
        if (priority != null) {
            Matcher matcher = Pattern.compile("[A-Z]").matcher(priority);
            matcher.find();
            char c = matcher.group().toCharArray()[0];
            return c;
        } else {
            return -1;
        }
    }

    public String getTask() {
        return task;
    }

    public ArrayList<String> getProjects() {
        return projects;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getToDate() {
        return toDate;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        // isDone
        if (done) stringBuilder.append("x ");
        // Priority
        if (priority != null) stringBuilder.append(priority).append(" ");
        // Task
        stringBuilder.append(task);
        // Projects
        if (!projects.isEmpty()) {
            for (String project : projects) {
                stringBuilder.append(" ").append("+").append(project);
            }
        }
        // Tags
        if (!tags.isEmpty()) {
            for (String tag : tags) {
                stringBuilder.append(" ").append("@").append(tag);
            }
        }
        // toDate
        if (toDate != null) stringBuilder.append(" ").append("due:").append(toDate);

        return stringBuilder.toString();
    }
}
