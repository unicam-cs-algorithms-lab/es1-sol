package it.unicam.cs.asdl.es1sol;

/**
 * Rappresenta il risultato della risoluzione, nel campo reale, di una specifica
 * {@link EquazioneSecondoGrado}.
 * <p>
 * Un'istanza può descrivere uno dei tre casi possibili: nessuna soluzione
 * reale, due soluzioni coincidenti oppure due soluzioni reali distinte.
 * <p>
 * La classe è immutabile: lo stato viene stabilito dal costruttore e può essere
 * soltanto osservato tramite i metodi pubblici. Un oggetto di questa classe può
 * quindi essere prodotto dalla business logic e successivamente interpretato
 * da front-end differenti senza che questi possano modificarlo.
 *
 * @author Luca Tesei
 */
public class SoluzioneEquazioneSecondoGrado {

    /* Equazione a cui appartiene la soluzione. */
    final EquazioneSecondoGrado e;

    /* Prima e seconda soluzione; NaN quando il valore non è definito. */
    private final double s1;
    private final double s2;

    /* Descrivono quale dei tre casi di soluzione è rappresentato. */
    private final boolean emptySolution;
    private final boolean oneSolution;

    /**
     * Costruisce il risultato di un'equazione priva di soluzioni reali.
     *
     * @param e equazione a cui si riferisce il risultato
     * @throws NullPointerException se {@code e} è {@code null}
     */
    public SoluzioneEquazioneSecondoGrado(EquazioneSecondoGrado e) {
        if (e == null)
            throw new NullPointerException(
                    "Tentativo di creare una soluzione di una equazione nulla");
        this.e = e;
        this.emptySolution = true;
        this.oneSolution = false;
        this.s1 = Double.NaN;
        this.s2 = Double.NaN;
    }

    /**
     * Costruisce il risultato di un'equazione con due soluzioni reali
     * coincidenti.
     *
     * @param e equazione a cui si riferisce il risultato
     * @param s1 valore della soluzione doppia
     * @throws NullPointerException se {@code e} è {@code null}
     */
    public SoluzioneEquazioneSecondoGrado(EquazioneSecondoGrado e, double s1) {
        if (e == null)
            throw new NullPointerException(
                    "Tentativo di creare una soluzione di una equazione nulla");
        this.e = e;
        this.emptySolution = false;
        this.oneSolution = true;
        this.s1 = s1;
        this.s2 = Double.NaN;
    }

    /**
     * Costruisce il risultato di un'equazione con due soluzioni reali distinte.
     *
     * @param e equazione a cui si riferisce il risultato
     * @param s1 prima soluzione reale
     * @param s2 seconda soluzione reale
     * @throws NullPointerException se {@code e} è {@code null}
     */
    public SoluzioneEquazioneSecondoGrado(EquazioneSecondoGrado e, double s1,
                                          double s2) {
        if (e == null)
            throw new NullPointerException(
                    "Tentativo di creare una soluzione di una equazione nulla");
        this.e = e;
        this.emptySolution = false;
        this.oneSolution = false;
        this.s1 = s1;
        this.s2 = s2;
    }

    /**
     * Restituisce la prima soluzione reale memorizzata.
     *
     * @return la prima soluzione, oppure {@link Double#NaN} se non è definita
     */
    public double getS1() {
        return s1;
    }

    /**
     * Restituisce la seconda soluzione reale memorizzata.
     *
     * @return la seconda soluzione, oppure {@link Double#NaN} se non è definita
     */
    public double getS2() {
        return s2;
    }

    /**
     * Determina se l'equazione non possiede soluzioni reali.
     *
     * @return {@code true} se la soluzione è vuota, {@code false} altrimenti
     */
    public boolean isEmptySolution() {
        return emptySolution;
    }

    /**
     * Determina se le due soluzioni reali coincidono.
     *
     * @return {@code true} se è presente una soluzione doppia,
     *         {@code false} altrimenti
     */
    public boolean isOneSolution() {
        return oneSolution;
    }

    /**
     * Verifica l'uguaglianza logica con un altro risultato.
     *
     * @param obj oggetto con cui effettuare il confronto
     * @return {@code true} se i due oggetti rappresentano lo stesso risultato
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SoluzioneEquazioneSecondoGrado))
            return false;
        SoluzioneEquazioneSecondoGrado other = (SoluzioneEquazioneSecondoGrado) obj;
        return e.equals(other.e)
                && emptySolution == other.emptySolution
                && oneSolution == other.oneSolution
                && Double.doubleToLongBits(s1) == Double.doubleToLongBits(other.s1)
                && Double.doubleToLongBits(s2) == Double.doubleToLongBits(other.s2);
    }

    /**
     * Restituisce un codice hash coerente con {@link #equals(Object)}.
     *
     * @return il codice hash di questo risultato
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + e.hashCode();
        result = prime * result + (emptySolution ? 1231 : 1237);
        result = prime * result + (oneSolution ? 1231 : 1237);
        long temp = Double.doubleToLongBits(s1);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(s2);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Restituisce una descrizione testuale dell'equazione e del relativo
     * risultato. Il metodo non stampa direttamente sullo standard output.
     *
     * @return rappresentazione testuale del risultato
     */
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("=== Soluzione di Equazione di Secondo Grado ===\n");
        s.append("Equazione originale: ").append(e).append("\n");
        if (isEmptySolution())
            s.append("Soluzione vuota.");
        else if (isOneSolution())
            s.append("Due soluzioni coincidenti: ").append(s1);
        else
            s.append("Prima soluzione: ").append(s1)
                    .append("\nSeconda soluzione: ").append(s2);
        s.append("\n=== Fine Soluzione ===");
        return s.toString();
    }
}
