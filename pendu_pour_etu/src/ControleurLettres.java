import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * Controleur du clavier
 */
public class ControleurLettres implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     */
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    ControleurLettres(MotMystere modelePendu, Pendu vuePendu){
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * Actions à effectuer lors du clic sur une touche du clavier
     * Il faut donc: Essayer la lettre, mettre à jour l'affichage et vérifier si la partie est finie
     * @param actionEvent l'événement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        String lettre = source.getText();
        // Essayer la lettre
        modelePendu.essayerLettre(lettre);
        // Mettre à jour l'affichage
        vuePendu.majAffichage();
        // Vérifier si la partie est finie
        if (modelePendu.estGagne()) {
            vuePendu.popUpMessageGagne().showAndWait();
            vuePendu.lancePartie();
        } else if (modelePendu.estPerdu()) {
            vuePendu.popUpMessagePerdu().showAndWait();
            vuePendu.lancePartie();
        }
    }
}
