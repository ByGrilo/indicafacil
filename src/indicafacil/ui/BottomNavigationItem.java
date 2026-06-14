package indicafacil.ui;

import java.awt.Dimension;
import javax.swing.JButton;

/*
 * Item individual da barra de baixo.
 */
public class BottomNavigationItem extends JButton {
    private final AppScreenKey target;

    public BottomNavigationItem(String text, AppScreenKey target) {
        super(text);
        this.target = target;
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFont(AppTheme.titleFont(22));
        setForeground(AppTheme.SECONDARY_TEXT);
        setPreferredSize(new Dimension(54, 34));
    }

    public AppScreenKey getTarget() {
        return target;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setForeground(selected ? AppTheme.ACCENT_DARK : AppTheme.SECONDARY_TEXT);
    }
}
