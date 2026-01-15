package codes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Student {
    private String name;
    private int age;
    private String classIn;
    private ArrayList<String> classes;
    private ArrayList<Integer> scores;

    public Student(String path) throws IOException{
        loadStudent(path);
    }

    public Student(String name, int age, String classIn, ArrayList<String> classes, ArrayList<Integer> scores) {
        this.name = name;
        this.age = age;
        this.classIn = classIn;
        this.classes = classes;
        this.scores = scores;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getClassIn() {
        return this.classIn;
    }

    public void setClassIn(String classIn) {
        this.classIn = classIn;
    }

    public ArrayList<String> getClasses() {
        return this.classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public ArrayList<Integer> getScores() {
        return this.scores;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(name, student.name) && age == student.age && Objects.equals(classIn, student.classIn) && Objects.equals(classes, student.classes) && Objects.equals(scores, student.scores);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", age='" + getAge() + "'" +
            ", classIn='" + getClassIn() + "'" +
            ", classes='" + getClasses() + "'" +
            ", scores='" + getScores() + "'" +
            "}";
    }

    public int getSomeScore(String theClass) {
        // return the score of a particular class
        // if the class is not chosen by the student, return Integer.MIN_VALUE in case someone use negative numbers
        int index = classes.indexOf(theClass);
        return index == -1 ? Integer.MIN_VALUE : scores.get(index);
    }

    public double getMeanScore() {
        int sum = 0;
        for (Integer score : scores) {
            sum += score;
        }
        return (double)sum / scores.size();
    }

    public void loadStudent(String path) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(path));
        String str;
        int line = 0;
        this.classes = new ArrayList<>();
        this.scores = new ArrayList<>();
        while((str = br.readLine()) != null){
            if (line == 0) this.name = str;
            else if (line == 1) this.age = Integer.parseInt(str);
            else if (line == 2) this.classIn = str;
            else {
                String[] arr = str.split("=");
                classes.add(arr[0]);
                scores.add(Integer.parseInt(arr[1]));
            }
            line++;
        }
        br.close();
    }
}
