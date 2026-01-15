package codes;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel{

    public MyTableModel(Object[][] data, Object[] column){
        //重写构造方法
        super(data, column);
    }
    
    @Override
    public boolean isCellEditable(int row, int column){
        //单元格不可编辑
        return false;
    }

    
}
