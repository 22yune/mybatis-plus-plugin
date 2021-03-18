package com.baomidou.plugin.idea.mybatisx.codegenerator.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.Enumeration;

/**
 * <p><b>     </b></p><br>
 * <p>     <br>
 * <br><p>
 *
 * @author baogen.zhang          2021-03-18 17:28
 */
public class ViewUtil {

    public static void fitTableColumns(JTable myTable) {               //設置table的列寬隨內容調整
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(
                column.getIdentifier());
            int width = (int) myTable.getTableHeader().getDefaultRenderer()
                .getTableCellRendererComponent(myTable,
                    column.getIdentifier(), false, false, -1, col)
                .getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++){
                int preferedWidth = (int) myTable.getCellRenderer(row, col)
                    .getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false,
                        row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column);
            column.setWidth(width + myTable.getIntercellSpacing().width);
        }
    }
}
