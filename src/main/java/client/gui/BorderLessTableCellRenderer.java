package client.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Class provides possibility when selecting table row don't highlight cell.
 */
public class BorderLessTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(final JTable table,
                                                   final Object value,
                                                   final boolean isSelected,
                                                   final boolean hasFocus,
                                                   final int row,
                                                   final int col) {

        final boolean showFocusedCellBorder = false;

        final Component c = super.getTableCellRendererComponent(table,
                value,
                isSelected,
                showFocusedCellBorder && hasFocus,
                row,
                col);

        return c;
    }
}
