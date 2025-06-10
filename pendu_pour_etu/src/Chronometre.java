import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Permet de gérer un Text associé à une Timeline pour afficher un temps écoulé
 */
public class Chronometre extends Text{
    /**
     * timeline qui va gérer le temps
     */
    private Timeline timeline;
    /**
     * la fenêtre de temps
     */
    private KeyFrame keyFrame;
    /**
     * le contrôleur associé au chronomètre
     */
    private ControleurChronometre actionTemps;
    /**
     * temps restant en millisecondes
     */
    private long tempsRestant = 120_000; // 2 minutes en millisecondes
    /**
     * référence vers la vue du jeu pour signaler la fin du temps
     */
    private Pendu vuePendu;

    /**
     * Constructeur permettant de créer le chronomètre
     * avec un label initialisé à "02:00"
     * Ce constructeur crée la Timeline, la KeyFrame et le contrôleur
     */
    public Chronometre(){
        this.setText("02min 00s"); 
        this.setFont(new Font("Arial", 24)); 
        this.setTextAlignment(TextAlignment.CENTER); 
        this.actionTemps = new ControleurChronometre(this);
        this.keyFrame = new KeyFrame(Duration.seconds(1), e -> {
            tempsRestant -= 1000;
            if (tempsRestant <= 0) {
                tempsRestant = 0;
                this.timeline.stop();
                if (vuePendu != null) {
                    vuePendu.tempsEcoule();
                }
            }
            setTime(tempsRestant);
        });
        this.timeline = new Timeline(this.keyFrame);
        this.timeline.setCycleCount(Animation.INDEFINITE); 
        setTime(tempsRestant);
    }

    /**
     * Permet au controleur de mettre à jour le text
     * la durée est affichée sous la forme m:s
     * @param tempsMillisec la durée depuis à afficher
     */
    public void setTime(long tempsMillisec){
        long secondes = tempsMillisec / 1000;
        long minutes = secondes / 60;
        secondes = secondes % 60;
        this.setText(String.format("%02dmin %02ds", minutes, secondes));
    }

    /**
     * Permet de démarrer le chronomètre
     */
    public void start(){
        this.timeline.play();
    }
    
    /**
     * Permet d'arrêter le chronomètre
     */
    public void stop(){
        this.timeline.stop();
    }
    
    /**
     * Permet de remettre le chronomètre à 2 minutes et le redémarrer
     */
    public void resetTime(){
        this.timeline.stop();
        this.tempsRestant = 120_000;
        setTime(tempsRestant);
        this.timeline.play();
    }
    
    /**
     * Permet d'associer une vue du jeu pour signaler la fin du temps
     * @param vuePendu la vue du jeu
     */
    public void setVuePendu(Pendu vuePendu) {
        this.vuePendu = vuePendu;
    }
    
    /**
     * Vérifie si le temps est écoulé
     * @return true si le temps est écoulé
     */
    public boolean tempsEcoule() {
        return tempsRestant <= 0;
    }
}