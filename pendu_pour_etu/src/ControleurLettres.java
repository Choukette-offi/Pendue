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
        // Vérifier si le temps n'est pas déjà écoulé
        if (vuePendu.getChrono().tempsEcoule()) {
            return; // Ne rien faire si le temps est écoulé
        }
        
        Button btn = (Button) actionEvent.getSource();
        String nomDubouton = btn.getText();
        char lettre = nomDubouton.charAt(0);
        
        // Remettre le chronomètre à 2 minutes à chaque clic
        vuePendu.getChrono().resetTime();
        
        modelePendu.essaiLettre(lettre);
        vuePendu.majAffichage();
        
        if(modelePendu.gagne()){
            vuePendu.getChrono().stop(); // Arrêter le chronomètre si gagné
            Optional<ButtonType> reponse = this.vuePendu.popUpMessageGagne().showAndWait();
            System.out.println("Gagné");
        }
        else{
            if(modelePendu.perdu()){
                vuePendu.getChrono().stop(); // Arrêter le chronomètre si perdu
                Optional<ButtonType> reponse = this.vuePendu.popUpMessagePerdu().showAndWait();
                System.out.println("Perdu");
            }
        }
    }
}