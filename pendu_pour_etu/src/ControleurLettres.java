import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

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
        Button btn = (Button) actionEvent.getSource();
        String nomDubouton = btn.getText();
        char lettre = nomDubouton.charAt(0);
        System.out.println(lettre);
        modelePendu.essaiLettre(lettre);
        vuePendu.majAffichage();
        if(modelePendu.gagne()){
            Optional<ButtonType> reponse = this.vuePendu.popUpMessageGagne().showAndWait(); // on lance la fenêtre popup et on attends la réponse
            System.out.println("Gagné");
        }
        else{
            if(modelePendu.perdu()){
            Optional<ButtonType> reponse = this.vuePendu.popUpMessagePerdu().showAndWait(); // on lance la fenêtre popup et on attends la réponse
            System.out.println("Perdu");

        }
        }
    }
}
