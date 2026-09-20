package it.unicam.cs.asdl.es1sol;

/**
 * Rappresenta un'equazione di secondo grado modificabile e incorpora il
 * servizio necessario a calcolarne le soluzioni reali.
 * <p>
 * Questa classe e' un esempio di oggetto di <em>business logic</em>: mantiene
 * uno stato interno, espone un'API pubblica e non interagisce direttamente con
 * l'utente. L'input e l'output sono responsabilita' di un eventuale front-end.
 * <p>
 * A differenza di {@link EquazioneSecondoGrado}, questa classe e'
 * <strong>mutabile</strong>: i coefficienti possono essere modificati tramite
 * i metodi {@link #setA(double)}, {@link #setB(double)} e
 * {@link #setC(double)}. La soluzione eventualmente calcolata appartiene pero'
 * allo stato precedente; per questo ogni modifica invalida la soluzione
 * corrente e rende necessario invocare nuovamente {@link #solve()}.
 * <p>
 * Il campo logico {@code solved} descrive quindi una proprieta' dello stato:
 * vale {@code true} solo quando {@code lastSolution} e' la soluzione dei
 * coefficienti correnti. Tentare di ottenere una soluzione non aggiornata e'
 * un errore di stato e viene segnalato tramite {@link IllegalStateException},
 * senza effettuare input/output.
 *
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a Casa
 */
public class EquazioneSecondoGradoModificabileConRisolutore {

    /*
     * Soglia numerica utilizzata per stabilire se un valore double e'
     * sufficientemente vicino a zero da essere considerato nullo in questa
     * esercitazione.
     */
    private static final double EPSILON = 1.0E-15;

    /* Stato corrente dell'equazione. */
    private double a;
    private double b;
    private double c;

    /* Indica se lastSolution e' valida per i coefficienti correnti. */
    private boolean solved;

    /* Ultima soluzione calcolata; e' significativa solo se solved e' true. */
    private SoluzioneEquazioneSecondoGrado lastSolution;

    /**
     * Costruisce un'equazione di secondo grado modificabile con i coefficienti
     * indicati.
     * <p>
     * Il coefficiente {@code a} non puo' essere considerato nullo, altrimenti
     * l'equazione non sarebbe di secondo grado. Subito dopo la costruzione
     * l'equazione non e' ancora risolta: per poter ottenere una soluzione e'
     * necessario invocare {@link #solve()}.
     *
     * @param a coefficiente del termine {@code x^2}; deve soddisfare
     *          {@code |a| >= EPSILON}
     * @param b coefficiente del termine {@code x}
     * @param c termine noto
     * @throws IllegalArgumentException se {@code |a| < EPSILON}
     */
    public EquazioneSecondoGradoModificabileConRisolutore(double a, double b,
            double c) {
        /*
         * La validazione appartiene alla business logic: un oggetto di questa
         * classe non deve mai poter rappresentare uno stato non valido.
         */
        if (Math.abs(a) < EPSILON)
            throw new IllegalArgumentException(
                    "L'equazione di secondo grado non puo' avere coefficiente a uguale a zero");

        this.a = a;
        this.b = b;
        this.c = c;

        /*
         * Al momento della costruzione non e' ancora stata calcolata alcuna
         * soluzione per i coefficienti memorizzati.
         */
        this.solved = false;
        this.lastSolution = null;
    }

    /**
     * Restituisce il valore corrente del coefficiente del termine di secondo
     * grado.
     *
     * @return il coefficiente corrente {@code a}
     */
    public double getA() {
        return a;
    }

    /**
     * Modifica il coefficiente del termine di secondo grado.
     * <p>
     * Una modifica dello stato invalida l'eventuale soluzione precedentemente
     * calcolata: dopo l'operazione {@link #isSolved()} restituisce
     * {@code false} finche' non viene chiamato di nuovo {@link #solve()}.
     *
     * @param a nuovo valore del coefficiente {@code a}; deve soddisfare
     *          {@code |a| >= EPSILON}
     * @throws IllegalArgumentException se {@code |a| < EPSILON}
     */
    public void setA(double a) {
        /*
         * Il controllo viene eseguito prima di modificare lo stato: se il
         * parametro non e' valido, l'oggetto conserva i coefficienti che aveva
         * prima della chiamata.
         */
        if (Math.abs(a) < EPSILON)
            throw new IllegalArgumentException(
                    "L'equazione di secondo grado non puo' avere coefficiente a uguale a zero");

        this.a = a;

        /*
         * Anche se numericamente il nuovo valore coincidesse con il precedente,
         * una chiamata a un metodo modificatore viene qui trattata come una
         * modifica dello stato e rende non valida la soluzione memorizzata.
         */
        this.solved = false;
    }

    /**
     * Restituisce il valore corrente del coefficiente del termine di primo
     * grado.
     *
     * @return il coefficiente corrente {@code b}
     */
    public double getB() {
        return b;
    }

    /**
     * Modifica il coefficiente del termine di primo grado.
     * <p>
     * Dopo la modifica l'eventuale soluzione precedentemente calcolata non e'
     * piu' valida per lo stato corrente e deve quindi essere ricalcolata.
     *
     * @param b nuovo valore del coefficiente {@code b}
     */
    public void setB(double b) {
        this.b = b;
        this.solved = false;
    }

    /**
     * Restituisce il valore corrente del termine noto.
     *
     * @return il coefficiente corrente {@code c}
     */
    public double getC() {
        return c;
    }

    /**
     * Modifica il termine noto.
     * <p>
     * Dopo la modifica l'eventuale soluzione precedentemente calcolata non e'
     * piu' valida per lo stato corrente e deve quindi essere ricalcolata.
     *
     * @param c nuovo valore del coefficiente {@code c}
     */
    public void setC(double c) {
        this.c = c;
        this.solved = false;
    }

    /**
     * Determina se l'equazione, nello stato corrente, e' gia' stata risolta.
     *
     * @return {@code true} se la soluzione memorizzata e' valida per i
     *         coefficienti correnti, {@code false} altrimenti
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Calcola le soluzioni reali dell'equazione definita dai coefficienti
     * correnti.
     * <p>
     * Il discriminante {@code delta = b^2 - 4ac} determina i tre casi da
     * gestire: nessuna soluzione reale se {@code delta < 0}, una soluzione
     * reale doppia se {@code delta = 0}, due soluzioni reali distinte se
     * {@code delta > 0}. Il risultato viene rappresentato tramite un oggetto
     * {@link SoluzioneEquazioneSecondoGrado}.
     * <p>
     * Se l'equazione e' gia' stata risolta e nessun coefficiente e' cambiato,
     * il metodo non ricalcola inutilmente la soluzione.
     */
    public void solve() {
        /*
         * Se la soluzione memorizzata corrisponde gia' ai coefficienti
         * correnti, non c'e' alcun lavoro da fare.
         */
        if (this.solved)
            return;

        double delta = this.b * this.b - 4 * this.a * this.c;

        if (Math.abs(delta) < EPSILON) {
            /*
             * Delta considerato nullo: esiste una sola soluzione reale, di
             * molteplicita' due, x = -b / (2a).
             */
            this.lastSolution = new SoluzioneEquazioneSecondoGrado(
                    new EquazioneSecondoGrado(this.a, this.b, this.c),
                    (-this.b) / (2 * this.a));
        } else if (delta < 0) {
            /*
             * Delta negativo: non esistono soluzioni reali. L'oggetto
             * SoluzioneEquazioneSecondoGrado rappresenta esplicitamente anche
             * questo caso, evitando di usare null come risultato.
             */
            this.lastSolution = new SoluzioneEquazioneSecondoGrado(
                    new EquazioneSecondoGrado(this.a, this.b, this.c));
        } else {
            /* Delta positivo: esistono due soluzioni reali distinte. */
            double sqrtDelta = Math.sqrt(delta);
            this.lastSolution = new SoluzioneEquazioneSecondoGrado(
                    new EquazioneSecondoGrado(this.a, this.b, this.c),
                    (-this.b + sqrtDelta) / (2 * this.a),
                    (-this.b - sqrtDelta) / (2 * this.a));
        }

        /*
         * Da questo momento lastSolution e' coerente con i coefficienti
         * correnti e puo' essere restituita da getSolution().
         */
        this.solved = true;
    }

    /**
     * Restituisce la soluzione valida per i coefficienti correnti.
     * <p>
     * Il metodo non risolve implicitamente l'equazione: il chiamante deve prima
     * invocare {@link #solve()}. Questo permette di distinguere chiaramente
     * l'operazione che modifica lo stato calcolando una soluzione da quella che
     * si limita a leggere il risultato gia' disponibile.
     *
     * @return la soluzione calcolata per i coefficienti correnti
     * @throws IllegalStateException se l'equazione non e' stata ancora risolta
     *         oppure se almeno un coefficiente e' stato modificato dopo
     *         l'ultima chiamata a {@code solve()}
     */
    public SoluzioneEquazioneSecondoGrado getSolution() {
        if (!this.solved)
            throw new IllegalStateException(
                    "Richiesta di soluzione di equazione non risolta");

        return this.lastSolution;
    }
}
