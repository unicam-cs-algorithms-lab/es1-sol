package it.unicam.cs.asdl.es1sol;

/**
 * Rappresenta un'equazione di secondo grado nella forma
 * {@code a x^2 + b x + c = 0}.
 * <p>
 * Questa classe fa parte della <em>business logic</em> dell'applicazione:
 * modella un'entità del dominio e non si occupa in alcun modo di input/output
 * o dell'interazione con l'utente.
 * <p>
 * I coefficienti costituiscono lo stato dell'oggetto. Lo stato è incapsulato
 * in campi privati e, poiché i campi sono {@code final}, un'istanza di questa
 * classe è immutabile: dopo la costruzione i coefficienti possono essere
 * osservati tramite i metodi pubblici, ma non possono essere modificati.
 * <p>
 * La classe ridefinisce inoltre {@link #equals(Object)}, {@link #hashCode()},
 * {@link #compareTo(EquazioneSecondoGrado)} e {@link #toString()} per fornire,
 * rispettivamente, uguaglianza logica, codice hash coerente con l'uguaglianza,
 * ordinamento naturale e rappresentazione testuale dell'oggetto.
 *
 * @author Luca Tesei
 */
public class EquazioneSecondoGrado
        implements Comparable<EquazioneSecondoGrado> {

    /*
     * Soglia numerica utilizzata per stabilire se il coefficiente a è
     * sufficientemente vicino a zero da non poter rappresentare un'equazione
     * di secondo grado.
     */
    private static final double EPSILON = 1.0E-15;

    /* Stato immutabile dell'oggetto. */
    private final double a;
    private final double b;
    private final double c;

    /**
     * Costruisce un'equazione di secondo grado con i coefficienti indicati.
     *
     * @param a coefficiente del termine {@code x^2}; deve soddisfare
     *          {@code |a| >= EPSILON}
     * @param b coefficiente del termine {@code x}
     * @param c termine noto
     * @throws IllegalArgumentException se {@code |a| < EPSILON}
     */
    public EquazioneSecondoGrado(double a, double b, double c) {
        if (Math.abs(a) < EPSILON)
            throw new IllegalArgumentException(
                    "L'equazione di secondo grado non può avere coefficiente a uguale a zero");
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Restituisce il coefficiente del termine di secondo grado.
     *
     * @return il coefficiente {@code a}
     */
    public double getA() {
        return a;
    }

    /**
     * Restituisce il coefficiente del termine di primo grado.
     *
     * @return il coefficiente {@code b}
     */
    public double getB() {
        return b;
    }

    /**
     * Restituisce il termine noto.
     *
     * @return il coefficiente {@code c}
     */
    public double getC() {
        return c;
    }

    /**
     * Verifica l'uguaglianza logica con un altro oggetto.
     * <p>
     * Due equazioni sono considerate uguali se hanno gli stessi tre
     * coefficienti. Il confronto non coincide con l'identità di riferimento:
     * due oggetti distinti nello heap possono quindi essere logicamente uguali.
     *
     * @param obj oggetto con cui confrontare questa equazione
     * @return {@code true} se {@code obj} rappresenta la stessa equazione,
     *         {@code false} altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EquazioneSecondoGrado))
            return false;
        EquazioneSecondoGrado other = (EquazioneSecondoGrado) obj;
        return Double.doubleToLongBits(this.a) == Double.doubleToLongBits(other.a)
                && Double.doubleToLongBits(this.b) == Double.doubleToLongBits(other.b)
                && Double.doubleToLongBits(this.c) == Double.doubleToLongBits(other.c);
    }

    /**
     * Restituisce un codice hash coerente con {@link #equals(Object)}.
     * <p>
     * Oggetti uguali secondo {@code equals} devono produrre lo stesso codice
     * hash, requisito fondamentale per l'uso corretto nelle collezioni basate
     * su hashing.
     *
     * @return il codice hash di questa equazione
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(a);
        // Combina con XOR i 32 bit alti e i 32 bit bassi del long,
        // ottenendo un valore int a cui contribuiscono tutti i 64 bit.
        /*
                       long temp (64 bit)
        ┌──────────────┬──────────────┐
        │  32 bit ALTI │ 32 bit BASSI │
        └──────────────┴──────────────┘
                │              │
                └────── XOR ───┘
                       │
                       ▼
                    32 bit
                       │
                    (int)
         */
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(c);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /**
     * Confronta questa equazione con un'altra secondo l'ordinamento naturale
     * lessicografico dei coefficienti: prima {@code a}, poi {@code b}, infine
     * {@code c}.
     * <p>
     * Il confronto dei coefficienti usa {@link Double#compare(double, double)}.
     * In questo modo l'ordinamento è coerente con il criterio adottato da
     * {@link #equals(Object)} anche per valori particolari di tipo
     * {@code double}, come {@code 0.0}, {@code -0.0} e {@code NaN}.
     * In particolare, {@code compareTo} restituisce zero se e solo se le due
     * equazioni sono considerate uguali da {@code equals}.
     *
     * @param o equazione con cui effettuare il confronto
     * @return un valore negativo se questa equazione precede {@code o}, zero se
     *         le equazioni sono uguali, un valore positivo se questa equazione
     *         segue {@code o}
     * @throws NullPointerException se {@code o} è {@code null}
     */
    @Override
    public int compareTo(EquazioneSecondoGrado o) {
        if (o == null)
            throw new NullPointerException("Tentativo di confrontare con null");
        int comparison = Double.compare(this.a, o.a);
        if (comparison != 0)
            return comparison;
        comparison = Double.compare(this.b, o.b);
        if (comparison != 0)
            return comparison;
        return Double.compare(this.c, o.c);
    }

    /**
     * Restituisce una rappresentazione testuale leggibile dell'equazione.
     * <p>
     * Come ogni {@code toString()}, questo metodo costruisce e restituisce una
     * stringa: non effettua direttamente alcuna stampa sullo standard output.
     *
     * @return l'equazione scritta nella forma algebrica usuale
     */
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(a).append(" x^2");
        if (b > 0)
            s.append(" + ").append(b).append(" x");
        else if (b < 0)
            s.append(" - ").append(Math.abs(b)).append(" x");
        if (c > 0)
            s.append(" + ").append(c);
        else if (c < 0)
            s.append(" - ").append(Math.abs(c));
        s.append(" = 0");
        return s.toString();
    }
}
