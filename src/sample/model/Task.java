package sample.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    private String priority, toDate;
    private final String task;
    private final ArrayList<String> projects;
    private final ArrayList<String> labels;


    public Task(String strTask) {
        Matcher matcher;
        projects = new ArrayList<>();
        labels = new ArrayList<>();
        String[] splitStrTask = strTask.trim().split(" ");

        StringBuilder taskBuilder = new StringBuilder();
        for (String word: splitStrTask) {
            // Parsing
            // Parse priority
            matcher = Pattern.compile("\\([A-Z]\\)").matcher(word);
            if (matcher.find()) { priority = matcher.group(); continue; }

            // Parse projects
            matcher = Pattern.compile("^\\+").matcher(word);
            if (matcher.find()) { projects.add(matcher.group()); continue; }

            // Parse labels
            matcher = Pattern.compile("^@").matcher(word);
            if (matcher.find()) { labels.add(matcher.group()); continue; }

            // Parse labels
            matcher = Pattern.compile("^@").matcher(word);
            if (matcher.find()) { labels.add(matcher.group()); continue; }

            // Parse toDate
            matcher = Pattern.compile("^due:").matcher(word);
            if (matcher.find()) { toDate = matcher.group(); continue; }

            taskBuilder.append(word).append(" ");
        }


        this.task = taskBuilder.toString().trim();
    }

    public String getPriority() {
        return priority;
    }

    public String getTask() {
        return task;
    }

    public ArrayList<String> getProjects() {
        return projects;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public String getToDate() {
        return toDate;
    }

    @Override
    public String toString() {
        return task;
    }
}
