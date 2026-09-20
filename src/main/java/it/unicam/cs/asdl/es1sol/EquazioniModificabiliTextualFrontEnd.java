package it.unicam.cs.asdl.es1sol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Front-end testuale dell'applicazione per la risoluzione di equazioni di
 * secondo grado modificabili.
 * <p>
 * La classe rappresenta il livello di interazione con l'utente: legge dati
 * dallo standard input, li converte in valori numerici, richiama esclusivamente
 * l'API pubblica della business logic e presenta i risultati sullo standard
 * output. La classe {@link EquazioneSecondoGradoModificabileConRisolutore}, al
 * contrario, non contiene codice di input/output.
 * <p>
 * Questa e' la versione di soluzione dell'esercitazione: il bug intenzionalmente
 * presente nel template e' stato corretto. Il punto didattico principale e'
 * osservare che la variabile {@code retry} rappresenta lo stato di un singolo
 * tentativo di lettura e deve quindi essere reimpostata prima di ogni nuovo
 * tentativo.
 *
 * @author Luca Tesei
 */
public class EquazioniModificabiliTextualFrontEnd {

    /*
     * La stessa soglia usata dalla business logic per considerare nullo il
     * coefficiente a. Il controllo anticipato serve qui soltanto a fornire un
     * messaggio di errore immediato e specifico all'utente.
     */
    private static final double EPSILON = 1.0E-15;

    /**
     * Avvia il front-end testuale. Per ogni equazione legge i tre coefficienti,
     * risolve l'equazione tramite la business logic, stampa il risultato e
     * chiede all'utente se desidera continuare.
     *
     * @param args argomenti della linea di comando; non vengono utilizzati
     */
    public static void main(String[] args) {
        EquazioneSecondoGradoModificabileConRisolutore eq = null;
        boolean terminate = false;

        /*
         * BufferedReader incapsula lo standard input e permette di leggere una
         * riga di testo alla volta. La conversione da String a double viene
         * effettuata separatamente tramite Double.parseDouble().
         */
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in));

        // Valori iniziali non significativi: vengono sostituiti dall'input.
        double a = 1;
        double b = 1;
        double c = 1;

        while (!terminate) {
            /*
             * Lettura del coefficiente a. Il ciclo do-while e' adatto perche'
             * almeno un tentativo di lettura deve essere effettuato.
             */
            boolean retry = false;
            do {
                /*
                 * Questo assegnamento e' essenziale: retry descrive l'esito del
                 * tentativo corrente, non di quelli precedenti.
                 */
                retry = false;

                System.out.println(
                        "Inserisci il valore del parametro a e premi INVIO");
                try {
                    String aInput = input.readLine();
                    a = Double.parseDouble(aInput);

                    /*
                     * Il front-end anticipa il controllo per poter spiegare
                     * subito all'utente il motivo del rifiuto. La business logic
                     * esegue comunque lo stesso controllo e rimane quindi
                     * responsabile della validita' del proprio stato.
                     */
                    if (Math.abs(a) < EPSILON) {
                        System.out.println(
                                "Errore: Il valore del parametro a non puo' essere zero! Ritenta...");
                        retry = true;
                    }
                } catch (IOException e) {
                    System.err.println("Errore di Input/Output!");
                    System.exit(1);
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Errore: Il valore del parametro a deve essere un numero! Ritenta...");
                    retry = true;
                }
            } while (retry);

            /* Lettura del coefficiente b. */
            do {
                /* Ogni nuovo tentativo parte assumendo che possa avere esito valido. */
                retry = false;

                System.out.println(
                        "Inserisci il valore del parametro b e premi INVIO");
                try {
                    String bInput = input.readLine();
                    b = Double.parseDouble(bInput);
                } catch (IOException e) {
                    System.err.println("Errore di Input/Output!");
                    System.exit(1);
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Errore: Il valore del parametro b deve essere un numero! Ritenta...");
                    retry = true;
                }
            } while (retry);

            /* Lettura del coefficiente c. */
            do {
                /* Anche qui il flag deve riferirsi esclusivamente al tentativo corrente. */
                retry = false;

                System.out.println(
                        "Inserisci il valore del parametro c e premi INVIO");
                try {
                    String cInput = input.readLine();
                    c = Double.parseDouble(cInput);
                } catch (IOException e) {
                    System.err.println("Errore di Input/Output!");
                    System.exit(1);
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Errore: Il valore del parametro c deve essere un numero! Ritenta...");
                    retry = true;
                }
            } while (retry);

            /*
             * Alla prima iterazione viene creato l'oggetto di business logic.
             * Nelle iterazioni successive viene riutilizzato lo stesso oggetto,
             * modificandone lo stato attraverso la sua API pubblica.
             */
            if (eq == null)
                eq = new EquazioneSecondoGradoModificabileConRisolutore(a, b,
                        c);
            else {
                eq.setA(a);
                eq.setB(b);
                eq.setC(c);
            }

            /*
             * Il front-end non conosce la formula risolutiva: chiede il servizio
             * alla business logic e si limita poi a visualizzare il risultato.
             */
            eq.solve();
            SoluzioneEquazioneSecondoGrado sol = eq.getSolution();
            System.out.println(sol);

            /* Chiede se deve iniziare una nuova iterazione dell'applicazione. */
            System.out.println(
                    "Vuoi risolvere un'altra equazione? Inserisci 's' o 'S' per continuare, qualsiasi altro carattere per uscire e premi INVIO");
            String resp = null;
            try {
                String responseInput = input.readLine();
                resp = responseInput.trim().toUpperCase();
            } catch (IOException e) {
                System.err.println("Errore di Input/Output!");
                System.exit(1);
            }

            if (!resp.equals("S"))
                terminate = true;
        }
    }

    /*
     * SPIEGAZIONE DEL BUG PRESENTE NEL TEMPLATE
     *
     * Il malfunzionamento era dovuto alla variabile booleana retry. Quando un
     * tentativo di lettura falliva, per esempio perche' l'utente inseriva una
     * stringa non convertibile in double, retry veniva impostata a true. Nel
     * template il valore non veniva pero' riportato a false all'inizio del
     * tentativo successivo.
     *
     * Di conseguenza, anche dopo un successivo inserimento corretto, retry
     * rimaneva true e la condizione del do-while provocava una nuova iterazione:
     * una volta entrato nello stato di retry, il ciclo poteva quindi non
     * terminare piu'.
     *
     * La correzione consiste nel porre retry = false all'inizio di ogni
     * iterazione di ciascun ciclo di lettura. In questo modo il flag descrive
     * soltanto l'esito del tentativo corrente: viene impostato nuovamente a
     * true solo se quel tentativo produce effettivamente un errore.
     */
}
