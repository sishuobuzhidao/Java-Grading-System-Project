package codes;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
// import java.util.Arrays;

import javax.swing.*;

public class AddStudentFrame extends JFrame implements ActionListener {
    JTextField nameField;
    JTextField ageField;
    JTextField classField;
    JTextArea classesArea;

    JButton yesButton;
    JButton noButton;
    
    MainFrame main;

    public AddStudentFrame(MainFrame main) {
        this.main = main;

        this.setSize(792, 700);
        this.setAlwaysOnTop(true);
        this.setTitle("Add Student");
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        initFrame();
        
        this.setVisible(true);
    }

    public void initFrame() {
        JLabel nameLabel = new JLabel("Enter the student's name: ");
        JLabel ageLabel = new JLabel("Enter the student's age: ");
        JLabel classLabel = new JLabel("Enter the student's class from: ");
        JLabel classesLabel1 = new JLabel("Enter all the student's classes below. ");
        JLabel classesLabel2 = new JLabel("Format must be \"class=score\". Example: English=20");
        JLabel classesLabel3 = new JLabel("Do not include float numbers or non-number score input or age input.");
        
        nameLabel.setBounds(20, 20, 400, 20);
        ageLabel.setBounds(20, 60, 400, 20);
        classLabel.setBounds(20, 100, 450, 20);
        classesLabel1.setBounds(20, 140, 600, 20);
        classesLabel2.setBounds(20, 160, 600, 20);
        classesLabel3.setBounds(20, 180, 600, 20);

        nameLabel.setFont(new Font("Arial", 0, 15));
        ageLabel.setFont(new Font("Arial", 0, 15));
        classLabel.setFont(new Font("Arial", 0, 15));
        classesLabel1.setFont(new Font("Arial", 0, 15));
        classesLabel2.setFont(new Font("Arial", 0, 15));
        classesLabel3.setFont(new Font("Arial", 1, 15)); //Bolded

        this.add(nameLabel);
        this.add(ageLabel);
        this.add(classLabel);
        this.add(classesLabel1);
        this.add(classesLabel2);
        this.add(classesLabel3);

        nameField = new JTextField();
        ageField = new JTextField();
        classField = new JTextField();

        nameField.setBounds(250, 18, 510, 24);
        ageField.setBounds(250, 58, 510, 24);
        classField.setBounds(250, 98, 510, 24);

        this.add(nameField);
        this.add(ageField);
        this.add(classField);

        classesArea = new JTextArea();
        JScrollPane jsp = new JScrollPane(classesArea);
        jsp.setBounds(20, 215, 740, 360); //放入容器中
        this.add(jsp);

        yesButton = new JButton("Confirm");
        yesButton.setBounds(100, 600, 200, 40);
        yesButton.addActionListener(this);
        this.add(yesButton);

        noButton = new JButton("Cancel");
        noButton.setBounds(470, 600, 200, 40);
        noButton.addActionListener(this);
        this.add(noButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == yesButton) {
            System.out.println("Confirm adding student process, uploading input");
            try {
                if (writeInfo()) {
                    clear();
                    this.setVisible(false);
                    main.refresh(); //学生对象会自动创建
                }
            } catch (IOException e1) {
                System.out.println("Captured IOException");
            }
        } else if (obj == noButton) {
            System.out.println("Cancel adding student process");
            clear();
            this.setVisible(false);
        }
    }
    
    public boolean writeInfo() throws IOException{
        String name = nameField.getText();
        int age;
        String classIn = classField.getText();
        String classes = classesArea.getText();
        
        //防止空输入框出现
        if (name.equals("") || classIn.equals("") || ageField.getText().equals("") || classes.equals("")) {
            System.out.println("Empty input for some fields");
            MainFrame.throwModal("Empty input for some fields!", "Warning");
            return false;
        }

        try {
            //确保年龄输入一定是整数
            age = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid age input (not an integer)");
            MainFrame.throwModal("Invalid age input. Please change it into an integer.", "Warning");
            return false;
        }

        try {
            String[] arr = classes.split("\\r?\\n|\\r"); //换行符
            for (String str : arr) {
                String[] arr2 = str.split("=");
                if (arr2.length != 2) {
                    System.out.println("Invalid score input (not in the correct form)");
                    MainFrame.throwModal("Invalid score input. Please make sure all classes are in the form class=score       " , "Warning");
                    return false;
                }
                Integer.parseInt(arr2[1]);
            }
            // System.out.println(Arrays.toString(arr));
        } catch (NumberFormatException e) {
            System.out.println("Invalid score input (score not an integer)");
            MainFrame.throwModal("Invalid score input. Please change it into an integer.", "Warning");
            return false;
        } 

        for (Student s : main.students) {
            //查重名
            if (s.getName().equals(name)) {
                System.out.println("Duplicate student name");
                MainFrame.throwModal("This student has already been added!", "Warning");
                return false;
            }
        }
        System.out.println("Input checked. Writing inputs into file");

        BufferedWriter bw = new BufferedWriter(new FileWriter("files\\" + main.students.size() + ".txt"));
        bw.write(name);
        bw.newLine();
        bw.write("" + age);
        bw.newLine();
        bw.write(classIn);
        bw.newLine();
        bw.write(classes); //有换行符所以不用过多writeLine
        bw.close();
        System.out.println("Successfully written the student profile");
        MainFrame.throwModal("Successfully added the student " + name + "!", null);
        return true;
    }

    public void clear() {
        nameField.setText("");
        ageField.setText("");
        classField.setText("");
        classesArea.setText("");
    }
}
