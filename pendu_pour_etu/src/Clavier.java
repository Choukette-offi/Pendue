import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Génère la vue d'un clavier et associe le contrôleur aux touches
 * le choix ici est d'un faire un héritié d'un TilePane
 */
public class Clavier extends TilePane {

    private Pendu vuePendu; 
    private MotMystere modelePendu;

    /**
     * il est conseillé de stocker les touches dans un ArrayList
     */
    private List<Button> clavier;

    /**
     * constructeur du clavier
     * @param touches une chaine de caractères qui contient les lettres à mettre sur les touches
     * @param actionTouches le contrôleur des touches
     */
    public Clavier(String touches,Pendu vuePende, MotMystere modelePendu, EventHandler<ActionEvent> actionTouches) {
        this.clavier = new ArrayList<>();
        this.vuePendu = vuePende;
        this.modelePendu = modelePendu;
        // Configuration du TilePane
        this.setPrefColumns(6); // 6 colonnes par ligne
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
        for (int i = 0; i < touches.length(); i++) {
            char lettre = touches.charAt(i);
            Button bouton = new Button(String.valueOf(lettre));
            bouton.setPrefSize(40, 40);
            bouton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            bouton.setOnAction(new ControleurLettres(modelePendu, vuePendu));
            this.clavier.add(bouton);
            this.getChildren().add(bouton);
        }
    }

    /**
     * permet de désactiver certaines touches du clavier (et active les autres)
     * @param touchesDesactivees une chaine de caractères contenant la liste des touches désactivées
     */
    public void desactiveTouches(Set<String> touchesDesactivees) {
        for (Button bouton : this.clavier) {
            String texteBouton = bouton.getText();

            if (touchesDesactivees.contains(texteBouton)) {
                // Désactiver le bouton
                bouton.setDisable(true);
                bouton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #cccccc;");
            } else {
                // Activer le bouton
                bouton.setDisable(false);
                bouton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #ffffff;");
            }
        }
    }
}
