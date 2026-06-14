package indicafacil.ui;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * Esse componente mostra estrelas e, quando precisa, tambem deixa escolher a nota.
 */
public class StarRatingView extends JPanel {
    private final boolean interactive;
    private final List<JButton> stars;
    private int rating;

    public StarRatingView() {
        this(0, true);
    }

    public StarRatingView(int rating) {
        this(rating, false);
    }

    public StarRatingView(int rating, boolean interactive) {
        this.interactive = interactive;
        this.rating = rating;
        this.stars = new ArrayList<>();

        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
        buildStars();
        refreshStars();
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = Math.max(0, Math.min(5, rating));
        refreshStars();
    }

    private void buildStars() {
        for (int i = 1; i <= 5; i++) {
            final int starValue = i;
            JButton star = new JButton("\u2606");
            star.setBorderPainted(false);
            star.setContentAreaFilled(false);
            star.setFocusPainted(false);
            star.setFont(AppTheme.titleFont(18));
            star.setForeground(AppTheme.WARNING);
            star.setEnabled(interactive);
            if (interactive) {
                star.addActionListener(event -> setRating(starValue));
            }
            stars.add(star);
            add(star);
        }
    }

    private void refreshStars() {
        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).setText(i < rating ? "\u2605" : "\u2606");
        }
    }
}
