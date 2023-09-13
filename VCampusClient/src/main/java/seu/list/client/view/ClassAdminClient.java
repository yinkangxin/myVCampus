package seu.list.client.view;

import seu.list.client.driver.Client;
import seu.list.client.driver.ClientMainFrame;
import seu.list.common.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ClassAdminClient extends JFrame {


    private JTextField search;
    private JTable table;
    private DefaultTableModel model;
    private int num;
    private int targetrow;
    private int targetcol;
    private int target;
    private Vector<Student> Stu = null;// save data from db ,actually for time saving
    private Vector<ClassManage> Clss = null;
    private JScrollPane scrollPane;
    private Vector<Student> StuTemp = null;
    private JLabel lblNewLabel_1;
    private MODEL nowmodel;

    ;

    @SuppressWarnings("unchecked")
    public ClassAdminClient() {
        nowmodel = MODEL.WATCHING;
        num = 0;
        setTitle("学生管理界面");
        setLayout(null);
        model = new DefaultTableModel(new Object[][]{}, new String[]{"班级", "教师", "学号", "姓名", "专业", "联系电话"}) {

            private static final long serialVersionUID = 1L;

            /*
             * overload the method to change the table's factor
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                if (target == 0) {// col
                    if (column == gettargetcol()) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (target == 1) {
                    if (row == gettargetrow()) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (target == 2) {
                    if (column == gettargetcol() && row == gettargetrow()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {// set id
                    if ((column == gettargetcol() || column == gettargetcol() + 1 || column == gettargetcol() + 4)
                            && row == gettargetrow()) {
                        return false;
                    } else if (row == gettargetrow()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        };

        JLabel backgroundImageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("imgs/ClassAdminClient.png")));
        Toolkit k = Toolkit.getDefaultToolkit();
        Dimension d = k.getScreenSize();
        setBounds(d.width / 2 - 640, d.height / 2 - 720 / 2, 1280, 745);
        backgroundImageLabel.setBounds(0, 0, 1280, 720);
        setResizable(false);
        setLayout(null);
        //下拉框
        final JComboBox<String> select = new JComboBox<String>();
        select.addItem("全部");
        select.addItem("班级");
        select.addItem("学号");
        select.addItem("姓名");
        select.setBounds(545, 154, 150, 50);
        add(select);
        select.setFont(new Font("华文行楷", Font.BOLD, 40));

        //搜索框
        search = new JTextField();
        search.setFont(new Font("华文行楷", Font.BOLD, 40));
        search.setBounds(745, 154, 978 - 745, 50);
        add(search);
        search.setOpaque(false);
        search.setBorder(new EmptyBorder(0, 0, 0, 0));

        //表格
        scrollPane = new JScrollPane();
        table = new JTable();
        table.setModel(model);
        table.getColumnModel().getColumn(5).setPreferredWidth(144);
        scrollPane.setViewportView(table);
        table.setRowHeight(25);
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 35));
        addRows();
        getClasses();
        table.setBounds(0, 0, 1132 - 292, 601 - 216);
        scrollPane.setBounds(292, 216, 1132 - 292, 601 - 216);
        add(scrollPane);
        add(backgroundImageLabel);


        //透明化处理
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 28));
        table.setRowHeight(40);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {"班级", "教师", "学号", "姓名", "专业", "联系电话"};
        for (int i = 0; i < 6; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getColumnHeader().setOpaque(false);
        add(backgroundImageLabel);

        JButton motify = new JButton("修改");
        motify.setBounds(126, 439, 243 - 126, 282 - 229);
        add(motify);
        JButton add = new JButton("增加");
        add.setBounds(126, 229, 243 - 126, 282 - 229);
        add(add);
        JButton delete = new JButton("删除");
        delete.setBounds(128, 334, 243 - 126, 282 - 229);
        add(delete);


        motify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (nowmodel == MODEL.ADD) {
                    JOptionPane.showMessageDialog(null, "请先进行保存", "提示", JOptionPane.WARNING_MESSAGE);
                } else if (nowmodel == MODEL.DELETE) {
                    JOptionPane.showMessageDialog(null, "请先完成删除操作", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    nowmodel = MODEL.MODIFY;
                    setModifyFrame();
                    nowmodel = MODEL.WATCHING;
                }
            }
        });


        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (nowmodel == MODEL.ADD) {
                    JOptionPane.showMessageDialog(null, "请先进行保存", "提示", JOptionPane.WARNING_MESSAGE);
                } else if (nowmodel == MODEL.DELETE) {
                    JOptionPane.showMessageDialog(null, "请先完成删除操作", "提示", JOptionPane.WARNING_MESSAGE);
                } else if (nowmodel == MODEL.MODIFY) {
                    JOptionPane.showMessageDialog(null, "请先完成修改操作", "提示", JOptionPane.WARNING_MESSAGE);
                } else {
                    nowmodel = MODEL.ADD;
                    setAddFrame();
                    nowmodel = MODEL.WATCHING;
                }
            }
        });


        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nowmodel = MODEL.DELETE;
                setDeleteFrame();
                nowmodel = MODEL.WATCHING;
            }
        });

        motify.setOpaque(false);
        add.setOpaque(false);
        delete.setOpaque(false);

        //退出按钮
        JButton exitbutton = new JButton("退出");
        exitbutton.setFont(new Font("宋体", Font.PLAIN, 18));
        exitbutton.setBounds(125, 545, 243 - 126, 282 - 229);
        add(exitbutton);
        exitbutton.setOpaque(false);


        //确认按钮
        JButton serachbutton = new JButton("搜索");
        serachbutton.setFont(new Font("宋体", Font.PLAIN, 18));
        serachbutton.setBounds(1008, 152, 1121 - 1008, 201 - 152);
        add(serachbutton);
        serachbutton.setOpaque(false);

        serachbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (select.getSelectedIndex()) {
                    case 0: {
                        updateStudent(Stu);
                    }
                    break;
                    case 1: {
                        // class
                        StuTemp = null;
                        StuTemp = new Vector<Student>();
                        int iforsearch = 0;
                        String sch = search.getText();
                        while (iforsearch < Stu.size()) {
                            // System.out.println(iforsearch);
                            String test = Stu.get(iforsearch).getClassid();
                            test.replaceAll("\\p{C}", "");
                            sch.replaceAll("\\p{C}", "");
                            // System.out.println(test.equals(sch));
                            // System.out.println(test);
                            // System.out.println(sch);
                            if (test.equals(sch)) {
                                StuTemp.add(Stu.get(iforsearch));
                                // System.out.println(1);
                            }
                            iforsearch++;
                            // System.out.println("okay");
                        }
                        System.out.println("update Student vector success!");
                        updateStudent(StuTemp);
                    }
                    break;
                    case 2: {
                        // student
                        StuTemp = null;
                        StuTemp = new Vector<Student>();
                        int iforsearch = 0;
                        String sch = search.getText();
                        while (iforsearch < Stu.size()) {
                            if (Stu.get(iforsearch).getStudentid().equals(sch)) {
                                StuTemp.add(Stu.get(iforsearch));
                            }
                            iforsearch++;
                        }
                        System.out.println("update Student vector success!");
                        //System.out.println(StuTemp.size());
                        updateStudent(StuTemp);
                    }
                    break;
                    case 3: {
                        // name
                        StuTemp = null;
                        StuTemp = new Vector<Student>();
                        int iforsearch = 0;
                        String sch = search.getText();
                        while (iforsearch < Stu.size()) {
                            if (Stu.get(iforsearch).getStudentName().equals(sch)) {
                                StuTemp.add(Stu.get(iforsearch));
                            }
                            iforsearch++;
                        }
                        System.out.println("update Student vector success!");
                        updateStudent(StuTemp);
                    }
                    default:
                        break;
                }
            }
        });

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        exitbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

    }

    @SuppressWarnings("unchecked")
    void addRows() {
        Message mes = new Message();
        mes.setMessageType(MessageType.ClassAdminGetAll);// operation type
        mes.setModuleType(ModuleType.Student);
        Message serverresponse = new Message();
        Vector<Student> stu = new Vector<Student>();// your data
        Client client = new Client(ClientMainFrame.socket);
        serverresponse = client.sendRequestToServer(mes);
        stu = (Vector<Student>) serverresponse.getData();
        Stu = stu;
        String[] arr = new String[6];
        for (int i = 0; i < stu.size(); i++) {
            //System.out.println(stu.get(i).getMajor());
            arr[0] = stu.get(i).getClassid();
            arr[1] = stu.get(i).getTeacherid();
            arr[2] = stu.get(i).getStudentid();
            arr[3] = stu.get(i).getStudentName();
            arr[4] = stu.get(i).getMajor();
            arr[5] = stu.get(i).getStudentphone();

            model.addRow(arr);
            table.setModel(model);
        }
        //透明化处理
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 28));
        table.setRowHeight(40);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {"班级", "教师", "学号", "姓名", "专业", "联系电话"};
        for (int i = 0; i < 6; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getColumnHeader().setOpaque(false);
    }

    @SuppressWarnings("unchecked")
    void getClasses() {
        // TODO Auto-generated method stub
        Message mes = new Message();
        mes.setMessageType(MessageType.ClassGetAll);// operation type
        mes.setModuleType(ModuleType.Student);
        Message serverresponse = new Message();
        Client client = new Client(ClientMainFrame.socket);
        serverresponse = client.sendRequestToServer(mes);
        Clss = new Vector<ClassManage>();
        Clss = (Vector<ClassManage>) serverresponse.getData();
        //System.out.println("get Classes");
        //System.out.println(Clss.isEmpty());
    }

    void settargetrow(int tar) {
        targetrow = tar;
        target = 1;// row
    }

    int gettargetrow() {
        return targetrow;
    }

    void settargetcol(int tar) {
        targetcol = tar;
        targetcol = 0;// col
    }

    int gettargetcol() {
        return targetcol;
    }

    void settarget(int tar) {
        target = tar;
    }

    void setAddFrame() {
        this.setEnabled(false);
        this.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
        ClassAdminForAdd frame = new ClassAdminForAdd(this, Stu, Clss);
        frame.setVisible(true);
    }

    void setModifyFrame() {
        this.setEnabled(false);
        this.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
        ClassAdminForModify frame = new ClassAdminForModify(this, Stu, Clss);
        frame.setVisible(true);
    }

    void setDeleteFrame() {
        this.setEnabled(false);
        this.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
        ClassAdminForDelete frame = new ClassAdminForDelete(this, Stu, Clss);
        frame.setVisible(true);
    }

    void close() {
        //menu.repaint();
        this.dispose();
        //MainTest frame = new MainTest();
        //frame.setVisible(true);
    }

    private void updateStudent(Vector<Student> tempforstu) {
        while (table.getRowCount() > 0) {
            model.removeRow(table.getRowCount() - 1);
            table.setModel(model);
        }

        String[] arr = new String[6];
        for (int i = 0; i < tempforstu.size(); i++) {
            arr[0] = tempforstu.get(i).getClassid();
            arr[1] = tempforstu.get(i).getTeacherid();
            arr[2] = tempforstu.get(i).getStudentid();
            arr[3] = tempforstu.get(i).getStudentName();
            arr[4] = tempforstu.get(i).getMajor();
            arr[5] = tempforstu.get(i).getStudentphone();
            //System.out.println("111111");

            model.addRow(arr);
            table.setModel(model);
        }
        //透明化处理
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 28));
        table.setRowHeight(40);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {"班级", "教师", "学号", "姓名", "专业", "联系电话"};
        for (int i = 0; i < 6; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getColumnHeader().setOpaque(false);
    }

    public void updateFrame(Vector<Student> Stu_update, Vector<ClassManage> Clss_update) {
        // use it in the child frame
        Stu = Stu_update;
        Clss = Clss_update;
        while (table.getRowCount() > 0) {
            model.removeRow(table.getRowCount() - 1);
            table.setModel(model);
        }
        addRows();
        //透明化处理
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Serif", Font.BOLD, 28));
        table.setRowHeight(40);                //表格行高
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);    //设置透明
        String[] Names = {"班级", "教师", "学号", "姓名", "专业", "联系电话"};
        for (int i = 0; i < 6; i++) {
            table.getColumn(Names[i]).setCellRenderer(renderer);//单格渲染
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderRenderer(renderer);//表头渲染
        }
        table.setOpaque(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createBevelBorder(0));
        scrollPane.getVerticalScrollBar().setOpaque(false);//滚动条设置透明
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getColumnHeader().setOpaque(false);
    }

    private enum MODEL {
        WATCHING, MODIFY, ADD, DELETE
    }
}
