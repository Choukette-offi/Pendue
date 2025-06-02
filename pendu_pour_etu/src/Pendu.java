import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;


/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */    
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */    
    private Button boutonMaison;
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */ 
    private Button bJouer;

    private BorderPane fenetre;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);//pour linux
        //this.modelePendu = new MotMystere("C:/Users/tagsm/Desktop/Bureau/Pendue/pendu_pour_etu/dictionnaire de mot windows/mot.txt", 3, 10, MotMystere.FACILE, 10);//pour windows
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");
        this.niveaux = Arrays.asList("Facile", "Moyen", "Difficile", "Expert");
        this.chrono = new Chronometre();
        this.pg = new ProgressBar();
        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ", new ControleurLettres(this.modelePendu, this));
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        this.fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private BorderPane titre(){
        BorderPane banniere = new BorderPane();
        HBox hbox = new HBox();
        Label titre = new Label("Jeu du Pendu");
        titre.setStyle("-fx-font-size: 32px;");
        ImageView home = new ImageView("../img/home.png");
        home.setFitHeight(30);
        ImageView para = new ImageView("../img/parametres.png");
        para.setFitHeight(30);
        ImageView info = new ImageView("../img/info.png");
        info.setFitHeight(30);
        home.setPreserveRatio(true);
        para.setPreserveRatio(true);
        info.setPreserveRatio(true);
        this.boutonMaison = new Button("", home);
        this.boutonParametres = new Button("", para);
        Button boutonInfo = new Button("", info);
        hbox.setSpacing(5);
        hbox.getChildren().addAll(this.boutonMaison, this.boutonParametres, boutonInfo);
        banniere.setRight(hbox);
        banniere.setLeft(titre);
        banniere.setStyle("-fx-background-color:rgb(223, 218, 253);");
        banniere.setPadding(new Insets(15));
        boutonMaison.setOnAction(new RetourAccueil(this.modelePendu, this));
        boutonInfo.setOnAction(new ControleurInfos(this));
        return banniere;
    }

    // /**
     // * @return le panel du chronomètre
     // */
    private TitledPane leChrono(){
    TitledPane pane = new TitledPane("Chronomètre", this.chrono);
    pane.setCollapsible(false);
    return pane;

    }

    // /**  
     // * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     // *         de progression et le clavier
     // */
    private BorderPane fenetreJeu(){
        BorderPane res = new BorderPane();
        VBox vbox = new VBox(20);
        VBox vbox2 = new VBox(20);
        this.pg.setProgress(1.0 - (double)this.modelePendu.getNbErreursRestants()/this.modelePendu.getNbErreursMax());
        this.pg.setStyle("-fx-accent: rgb(50, 179, 253);");
        Label mdp = new Label(this.modelePendu.getMotCrypte());
        mdp.setTextAlignment(TextAlignment.CENTER);
        this.leNiveau = new Text("Niveau : " + this.niveaux.get(this.modelePendu.getNiveau()));
        this.leNiveau.setStyle("-fx-font-size: 32px;");
        mdp.setStyle("-fx-font-size: 32px;");
        Button newmot = new Button("Nouveau mot");
        newmot.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        this.dessin = new ImageView(this.lesImages.get(this.modelePendu.getNbErreursMax() -this.modelePendu.getNbErreursRestants()));
        vbox.getChildren().addAll(mdp, this.dessin, pg, this.clavier);
        vbox.setPadding(new Insets(40));
        vbox2.getChildren().addAll(this.leNiveau, this.leChrono(),newmot);
        vbox2.setPadding(new Insets(40));
        res.setCenter(vbox);
        res.setRight(vbox2);
        return res;
    }

    // /**
     // * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     // */
    private VBox fenetreAccueil(){   
        VBox res = new VBox(20);
        VBox tgourp = new VBox(10);
        this.bJouer = new Button("Lancer une partie");
        this.bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        RadioButton boutonF =new RadioButton("Facile");
        RadioButton boutonM =new RadioButton("Moyen");
        RadioButton boutonD =new RadioButton("Difficile");
        RadioButton boutonE =new RadioButton("Expert");
        boutonF.setOnAction(new ControleurNiveau(this.modelePendu));
        boutonM.setOnAction(new ControleurNiveau(this.modelePendu));
        boutonD.setOnAction(new ControleurNiveau(this.modelePendu));
        boutonE.setOnAction(new ControleurNiveau(this.modelePendu));
        ToggleGroup group = new ToggleGroup();
        boutonF.setToggleGroup(group);
        boutonM.setToggleGroup(group);
        boutonD.setToggleGroup(group);
        boutonE.setToggleGroup(group);
        tgourp.getChildren().addAll(boutonF, boutonM, boutonD, boutonE);
        TitledPane t = new TitledPane("Niveau de difficulté", tgourp);
        t.setCollapsible(false);
        res.getChildren().addAll(this.bJouer, t);
        res.setPadding(new Insets(20));
        return res;
    }

    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire){
        for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
            File file = new File(repertoire+"/pendu"+i+".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil(){
        this.fenetre.setCenter(this.fenetreAccueil());
    }
    
    public void modeJeu(){
        this.fenetre.setCenter(this.fenetreJeu());
    }
    
    public void modeParametres(){
        // A implémenter
    }

    /** lance une partie */
    public void lancePartie(){
        // A implementer
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(){
        // A implementer
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono(){
        return this.chrono;
    }

    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }
        
    public Alert popUpReglesDuJeu(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }
    
    public Alert popUpMessageGagne(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);        
        return alert;
    }
    
    public Alert popUpMessagePerdu(){
        // A implementer    
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
