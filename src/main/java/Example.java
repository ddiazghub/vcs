import svcs.Vcs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class Example {
    public static void main(String[] args) throws IOException {
        // Se crea una instancia del control del versiones en la carpeta actual.
        // Si se quiere crear en otra carpeta se cambia el valor de la variable root.
        Ventana ventana = new Ventana();
        ventana.setVisible(true);


        // Se crea un nuevo archivo para añadir al control de versiones
        //String testFile = "test.txt";
        //Path testPath = Path.of(testFile);
        //String content = "Hello World " + new Random().nextLong() + "!!!";
        //Files.writeString(testPath, content);

        // Se añade el archivo test.txt al control de versiones
       // vcs.add(testFile);

        // Se realiza un commit de los cambios
        //String message = "Commit " + vcs.getCommits().size();
        //vcs.commit(message);

        // Se imprime el log
        //System.out.println(vcs.getLog());

        // Checkout
        //List<String> lastCommits = vcs.getLastCommits();
        //vcs.checkout(lastCommits.get(0)); // Checkout al anterior commit
        //vcs.checkout(lastCommits.get(1)); // se retorna al commit mas reciente
    }
}