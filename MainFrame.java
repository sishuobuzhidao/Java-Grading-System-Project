package codes;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
// import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;

public class MainFrame extends JFrame implements ActionListener{
    
    ArrayList<Student> students;
    ArrayList<Student> fixedStudents; //防止出现重新排列之后的索引问题
    JTable table;

    JMenuItem addItem;
    JMenuItem editItem;
    JMenuItem meanStudentItem;
    JMenuItem meanClassItem;
    JMenuItem rankItem;
    JMenuItem refreshItem;
    JMenuItem descriptionItem;
    JMenuItem aboutUsItem;

    AddStudentFrame asf;
    EditStudentFrame edf;

    public MainFrame(){
        asf = new AddStudentFrame(this);
        asf.setVisible(false);
        edf = new EditStudentFrame(this);
        edf.setVisible(false);

        this.setSize(888, 600);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("Student Grading System");

        loadStudents();
        initFrame();
        initTable();
        initMenu();

        this.setVisible(true);
    }

    public void initFrame(){
        JLabel titleLabel = new JLabel("Student Grading System");
        titleLabel.setBounds(264, 30, 400, 40);
        titleLabel.setFont(new Font("Times New Roman", 0, 36));
        this.getContentPane().add(titleLabel);
    }

    public void initTable(){

        table = new JTable();
        // table.setBounds(40, 50, 650, 420);
        // 由于是将table放到JScrollPane当中，故不用设置JTable的大小

        table.setSelectionMode(1);
        Object[] columns = getColumn();
        Object[][] data = new Object[students.size()][columns.length];
        for (int i = 0; i < data.length; i++) {
            Student student = students.get(i);
            data[i][0] = student.getName();
            data[i][1] = student.getAge();
            data[i][2] = student.getClassIn();
            for (int j = 3; j < data[0].length; j++) {
                int score = student.getSomeScore((String)columns[j]);
                data[i][j] = score == Integer.MIN_VALUE ? "NaN" : score;
            }
        }
        // for (Object[] arr : data) {
        //     System.out.println(Arrays.toString(arr));
        // }
        table.setModel(new MyTableModel(data, columns));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//防止自动适应导致表头分布失效

        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);//不可挪动列
        table.getTableHeader().setResizingAllowed(false);//不可调整列宽

        // set column WIDTH
        // table.getColumnModel().getColumn(0).setPreferredWidth(50);
        // table.getColumnModel().getColumn(0).setMinWidth(50);
        // table.getColumnModel().getColumn(0).setMaxWidth(50);

        // table.getColumnModel().getColumn(1).setPreferredWidth(30);
        // table.getColumnModel().getColumn(0).setMinWidth(30);
        // table.getColumnModel().getColumn(0).setMaxWidth(30);

        // table.getColumnModel().getColumn(2).setPreferredWidth(40);
        // table.getColumnModel().getColumn(0).setMinWidth(40);
        // table.getColumnModel().getColumn(0).setMaxWidth(40);

        // for (int i = 3; i < columns.length; i++) {
        //     table.getColumnModel().getColumn(i).setPreferredWidth(50);
        //     table.getColumnModel().getColumn(i).setMinWidth(50);
        //     table.getColumnModel().getColumn(i).setMaxWidth(50);
        // }

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(40, 100, 800, 420);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.getContentPane().add(jsp);
        
        //this.repaint();
    }

    public void initMenu(){
        JMenuBar jmb = new JMenuBar();
        JMenu optionMenu = new JMenu("Options");
        JMenu infoMenu = new JMenu("Info");

        jmb.add(optionMenu);
        jmb.add(infoMenu);

        addItem = new JMenuItem("Add Student");
        editItem = new JMenuItem("Edit Student");
        meanStudentItem = new JMenuItem("Get Student Mean Score");
        meanClassItem = new JMenuItem("Get Class Mean Score");
        rankItem = new JMenuItem("Rank Students");
        refreshItem = new JMenuItem("Refresh");
        descriptionItem = new JMenuItem("Description");
        aboutUsItem = new JMenuItem("About us");

        optionMenu.add(addItem);
        optionMenu.add(editItem);
        optionMenu.add(meanStudentItem);
        optionMenu.add(meanClassItem);
        optionMenu.add(rankItem);
        optionMenu.add(refreshItem);
        infoMenu.add(descriptionItem);
        infoMenu.add(aboutUsItem);

        addItem.addActionListener(this);
        editItem.addActionListener(this);
        meanStudentItem.addActionListener(this);
        meanClassItem.addActionListener(this);
        rankItem.addActionListener(this);
        refreshItem.addActionListener(this);
        descriptionItem.addActionListener(this);
        aboutUsItem.addActionListener(this);

        this.setJMenuBar(jmb);
    }

    public void loadStudents(){
        int number = 0;
        students = new ArrayList<>();
        fixedStudents = new ArrayList<>();
        while (true){
            try {
                Student s = new Student("files\\" + number + ".txt");
                students.add(s);
                fixedStudents.add(s);//确保添加的是同一个学生对象
                number++;
            } catch (FileNotFoundException e) {
                //一旦无法找到文件，就停止继续加载
                System.out.println("Loaded in all students");
                break;
            } catch (IOException e) {
                System.out.println("Captured IOException");
                continue;
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid file format");
                e.printStackTrace();
                continue;
            }
        }
        System.out.println("Successfully loaded in student data");
    }

    public Object[] getColumn(){
        LinkedHashSet<String> allClasses = new LinkedHashSet<>();
        for (Student s : students) {
            // stream流将所有Classes放入allClasses的Set之中。
            s.getClasses().stream().forEach(str -> allClasses.add(str));
        }
        Object[] classes = new Object[allClasses.size() + 3];
        Iterator<String> iterator = allClasses.iterator();
        classes[0] = "Name";
        classes[1] = "Age";
        classes[2] = "Class From";
        for (int i = 3; i < classes.length; i++) {
            classes[i] = iterator.next();
        }
        // System.out.println(Arrays.toString(classes));
        return classes;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == addItem) {
            System.out.println("Add new Student");
            asf.setVisible(true);
        }

        else if (obj == editItem) {
            System.out.println("Edit student profile");
            editing(table.getSelectedRow());
        }

        else if (obj == meanStudentItem) {
            System.out.println("Get Mean Score of student");
            int index = table.getSelectedRow();
            if (index == -1) 
                throwModal("Please select a row first!", "Warning");
            else if (table.getSelectedRows().length > 1) 
                throwModal("Please select only one row.", "Warning");
            else 
                throwModal("The mean score of " + students.get(index).getName()  + " is: " + students.get(index).getMeanScore(), "Student Mean Score");
        }

        else if (obj == meanClassItem) {
            System.out.println("Get Mean Score of class");
            getClassMeanScore(table.getSelectedColumn());
        }

        else if (obj == rankItem) {
            System.out.println("Rank students based on column");
            rankStudents(table.getSelectedColumn());
        }

        else if (obj == refreshItem) {
            System.out.println("Refresh the page");
            refresh();
        }

        else if (obj == descriptionItem) {
            System.out.println("Show description");
            try {
                showDescription();
            } catch (IOException e1) {
                System.out.println("Captured IOException");;
            }
        }

        else if (obj == aboutUsItem) {
            System.out.println("About Us: created by sishuo 2024.05.30 All Rights Reserved");
            throwModal("Created by sishuo 2024.05.30 All Rights Reserved", "About Us");
        }
    }

    public static void throwModal(String text, String title){
        //弹窗警告
        JDialog jd = new JDialog();
        jd.setLayout(null);
        jd.setSize(375 + 5 * text.length(), 290);
        jd.setModal(true);
        jd.setAlwaysOnTop(true);
        jd.setLocationRelativeTo(null);
        jd.setTitle(title == null ? "Message" : title);

        JLabel message = new JLabel(text);
        message.setBounds(50, 100, 384 + 5 * text.length(), 40);
        message.setFont(new Font("Arial", 1, 20));
        jd.getContentPane().add(message);

        jd.setVisible(true);
    }

    public void editing(int index) {
        if (index == -1) throwModal("Please select a student first.", "Warning");
        else {
            //确保在重新rank之后也能得到正确的学生对象(极其重要！！！！)
            edf.appear(fixedStudents.indexOf(students.get(index)));
        }
    }

    public void showDescription() throws IOException{
        JDialog jd = new JDialog();
        jd.setLayout(new FlowLayout(FlowLayout.LEFT));
        jd.setSize(375, 400);
        jd.setModal(true);
        jd.setAlwaysOnTop(true);
        jd.setLocationRelativeTo(null);
        jd.setTitle("Description");

        StringBuilder sb = new StringBuilder();
        String str;
        BufferedReader br = new BufferedReader(new FileReader("files\\description.txt"));
        while((str = br.readLine()) != null) sb.append(str);
        br.close();

        //用HTML文本实现超出JLabel长度自动换行
        JLabel message = new JLabel("<html><div style=\"width: 260px; text-align: center;\">" + sb.toString() + "</div></html>");
        //message.setBounds(50, 100, 100, 300);
        message.setFont(new Font("Arial", 0, 15));
        jd.getContentPane().add(message);

        jd.setVisible(true);
    }

    public void getClassMeanScore(int index) {
        if (index == -1)
            throwModal("Please select a column first!", "Warning");
        else if (index == 0 || index == 1 || index == 2)
            throwModal("Please select a column representing a class.", "Warning");
        else {
            String theClass = (String) getColumn()[index];
            //using streams to do this because I am bored
            double meanScore = students.stream().mapToInt(s -> s.getSomeScore(theClass) == Integer.MIN_VALUE ? 0 : s.getSomeScore(theClass)).sum()
            / (double) students.stream().filter(s -> s.getSomeScore(theClass) != Integer.MIN_VALUE).count();
            throwModal("The mean score of " + theClass + " is: " + meanScore, "Class Mean Score");
        }    
    }

    public void rankStudents(int index) {
        //使用stream流的sorted方法操作
        if (index == -1)
            throwModal("Please select a column first!", "Warning");
        else if (index == 0) {
            List<Student> newList = students.stream().sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName())).toList();
            for (int i = 0; i < students.size(); i++) {
                students.set(i, newList.get(i));
            }
            throwModal("Successfully ranked the students based on name.", null);
            refreshWithoutIO();
        } else if (index == 1) {
            List<Student> newList = students.stream().sorted((o1, o2) -> o1.getAge() - o2.getAge()).toList();
            for (int i = 0; i < students.size(); i++) {
                students.set(i, newList.get(i));
            }
            throwModal("Successfully ranked the students based on age.", null);
            refreshWithoutIO();
        } else if (index == 2) {
            List<Student> newList = students.stream().sorted((o1, o2) -> o1.getClassIn().compareToIgnoreCase(o2.getClassIn())).toList();
            for (int i = 0; i < students.size(); i++) {
                students.set(i, newList.get(i));
            }
            throwModal("Successfully ranked the students based on class.", null);
            refreshWithoutIO();
        } else {
            String theClass = table.getColumnName(index);

            //有分数的学生按分数排列
            Stream<Student> streamWithClass = students.stream().filter(s -> s.getSomeScore(theClass) != Integer.MIN_VALUE)
            .sorted((o1, o2) -> o2.getSomeScore(theClass) - o1.getSomeScore(theClass));
            
            //无分数的学生按姓名排列
            Stream<Student> streamWithoutClass = students.stream().filter(s -> s.getSomeScore(theClass) == Integer.MIN_VALUE)
            .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

            //合并两个流
            List<Student> newList = Stream.concat(streamWithClass, streamWithoutClass).toList();
            for (int i = 0; i < students.size(); i++) {
                students.set(i, newList.get(i));
            }
            throwModal("Successfully ranked the students based on the score of " + theClass + ".", null);
            refreshWithoutIO();
        }
    }

    public void refresh(){
        //刷新页面，removeAll的时候只移除contentPane里的东西
        //刷新会失去所有的ranks
        this.getContentPane().removeAll();
        loadStudents();
        initFrame();
        initTable();
        this.getContentPane().repaint();
    }

    public void refreshWithoutIO(){
        //不需要重新加载文件并刷新
        this.getContentPane().removeAll();
        initFrame();
        initTable();
        this.getContentPane().repaint();
    }

}
