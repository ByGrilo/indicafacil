package indicafacil.ui;

import java.awt.FlowLayout;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/*
 * Barra de navegacao do rodape.
 */
public class BottomNavigationBar extends JPanel {
    private final Map<AppScreenKey, BottomNavigationItem> items;

    public BottomNavigationBar() {
        this.items = new EnumMap<>(AppScreenKey.class);
        setLayout(new FlowLayout(FlowLayout.CENTER, 34, 10));
        setBackground(AppTheme.SURFACE);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));
    }

    public void addItem(BottomNavigationItem item) {
        items.put(item.getTarget(), item);
        add(item);
    }

    public void setSelected(AppScreenKey target) {
        for (BottomNavigationItem item : items.values()) {
            item.setSelected(item.getTarget() == target);
        }
    }
}
