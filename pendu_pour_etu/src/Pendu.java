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

import java.util.*;
import java.io.File;


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
        //this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);//pour linux
        this.modelePendu = new MotMystere("C:/Users/tagsm/Desktop/Bureau/Pendue/pendu_pour_etu/dictionnaire de mot windows/mot.txt", 3, 10, MotMystere.FACILE, 10);//pour windows
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");
        this.niveaux = Arrays.asList("Facile", "Moyen", "Difficile", "Expert");
        this.chrono = new Chronometre();
        this.pg = new ProgressBar();
        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ", new ControleurLettres(this.modelePendu, this), 7);
        this.leNiveau = new Text();
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
        boutonParametres.setOnAction(new ControleurParamètre(this.modelePendu, this));
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
        this.pg.setStyle("-fx-accent: rgb(50, 179, 253);");
        this.motCrypte.setTextAlignment(TextAlignment.CENTER);
        this.leNiveau.setStyle("-fx-font-size: 32px;");
        this.motCrypte.setStyle("-fx-font-size: 32px;");
        Button newmot = new Button("Nouveau mot");
        this.boutonMaison.setDisable(false);
        this.boutonMaison.setStyle("");
        this.boutonParametres.setDisable(true);
        this.boutonParametres.setStyle("");
        newmot.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        vbox.getChildren().addAll(this.motCrypte, this.dessin, pg, this.clavier);
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
        this.boutonMaison.setDisable(true);
        this.boutonMaison.setStyle("");
        boutonParametres.setDisable(false);
        boutonParametres.setStyle("");
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

    private HBox fenetreParametres(){
        HBox res = new HBox(20);
        this.boutonMaison.setDisable(false);
        this.boutonMaison.setStyle("");
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
        this.fenetre.setCenter(this.fenetreParametres());
    }

    /** lance une partie */
    public void lancePartie(){
        this.modelePendu = new MotMystere("C:/Users/tagsm/Desktop/Bureau/Pendue/pendu_pour_etu/dictionnaire de mot windows/mot.txt", 3, 10,modelePendu.getNiveau(), 10);
        this.dessin= new ImageView(new Image("../img/pendu0.png"));
        this.pg = new ProgressBar();
        this.chrono = new Chronometre();
        this.chrono.setVuePendu(this);  // Associer la vue au chronomètre
        this.clavier= new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ",new ControleurLettres(modelePendu, this), 7);
        this.leNiveau.setText("Niveau "+ niveaux.get(modelePendu.getNiveau()));
        this.pg = new ProgressBar(0);
        motCrypte = new Text(modelePendu.getMotCrypte());
        this.chrono.start();  // Démarrer le chronomètre
        this.modeJeu();
    }   

    /**
     * Action à effectuer lorsque le temps est écoulé
     * Désactive toutes les touches du clavier et affiche un message de fin de partie
     */
        public void tempsEcoule() {
        Set<String> toutesLettres = new HashSet<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (char lettre : alphabet.toCharArray()) {
            toutesLettres.add(String.valueOf(lettre));
        }
        this.clavier.desactiveTouches(toutesLettres);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Temps écoulé");
        alert.setHeaderText("Le temps est écoulé !");
        alert.setContentText("Vous avez perdu ! Le mot était : " + this.modelePendu.getMotATrouve());
        alert.showAndWait();
    }

    /**
     

    raffraichit l'affichage selon les données du modèle*/
    public void majAffichage(){
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.dessin.setImage( new Image("/pendu"+(this.modelePendu.getNbErreursMax()-this.modelePendu.getNbErreursRestants())+".png"));
        this.pg.setProgress(1.0-(double)this.modelePendu.getNbErreursRestants()/this.modelePendu.getNbErreursMax());
        clavier.desactiveTouches(modelePendu.getLettresEssayees());
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Règle du Jeux!!!");
        alert.setContentText("Le jeu du Pendu consiste à deviner un mot caché en proposant des lettres une par une.\n Chaque bonne lettre est révélée, tandis qu'une mauvaise ajoute \n une partie au dessin du pendu. \n Le joueur gagne s’il trouve le mot avant que le dessin soit complet.");
        return alert;
    }
    
    public Alert popUpMessageGagne() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Jeu du Pendu");
    alert.setHeaderText("Vous avez gagné :)");
    alert.setContentText("Bravo ! Vous avez gagné !");
    Set<String> ttLettres = new HashSet<>();
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    for (char lettreAlpha : alpha.toCharArray()) {
        ttLettres.add(String.valueOf(lettreAlpha));
    }
    this.clavier.desactiveTouches(ttLettres);
    return alert;
}

    public Alert popUpMessagePerdu() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Jeu du Pendu"); 
    alert.setHeaderText("Vous avez perdu :("); 
    alert.setContentText("Le mot était : " + this.modelePendu.getMotATrouve() + "\nDommage ! Vous ferez mieux la prochaine fois !");
    Set<String> ttLettres = new HashSet<>();
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    for (char lettreAlpha : alpha.toCharArray()) {
        ttLettres.add(String.valueOf(lettreAlpha));
    }
    this.clavier.desactiveTouches(ttLettres);
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
