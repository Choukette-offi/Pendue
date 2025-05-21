#!\bin\bash
# on se rend dans le dossier src


#on compile les fichiers .java
javac -d bin --module-path "C:\Program Files\javafx-sdk-22\lib" --add-modules javafx.controls ./src/*.java


#on lance l'application
java -cp bin:img --module-path "C:\Program Files\javafx-sdk-22\lib" --add-modules javafx.controls Pendu