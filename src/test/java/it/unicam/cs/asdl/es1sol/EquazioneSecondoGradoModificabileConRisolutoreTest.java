package it.unicam.cs.asdl.es1sol;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test JUnit 5 della classe
 * {@link EquazioneSecondoGradoModificabileConRisolutore}.
 * <p>
 * I test costituiscono parte del materiale dell'esercitazione: ogni metodo di
 * test descrive uno scenario e controlla il comportamento osservabile tramite
 * l'API pubblica della classe. In particolare vengono coperti i casi limite
 * della risoluzione (discriminante negativo, nullo e positivo) e le transizioni
 * di stato causate dalla modifica dei coefficienti.
 *
 * @author Luca Tesei
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {

    /* Tolleranza usata nei confronti tra risultati ottenuti tramite calcoli. */
    private static final double EPSILON = 1.0E-12;

    @Test
    void testCostruttoreMemorizzaCoefficientiEIniziaNonRisolta() {
        // Un oggetto appena costruito deve conservare i coefficienti ricevuti.
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(2.0, -3.0,
                        4.0);

        assertEquals(2.0, eq.getA());
        assertEquals(-3.0, eq.getB());
        assertEquals(4.0, eq.getC());

        // La costruzione non deve risolvere automaticamente l'equazione.
        assertFalse(eq.isSolved());
    }

    @Test
    void testCostruttoreRifiutaCoefficienteANulloOVicinoAZero() {
        // a = 0 non definisce un'equazione di secondo grado.
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0.0,
                        1.0, 1.0));

        // Anche un valore con modulo inferiore alla soglia prevista va rifiutato.
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(
                        1.0E-16, 1.0, 1.0));
    }

    @Test
    void testSetAModificaIlCoefficienteEInvalidaLaSoluzione() {
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, -3.0,
                        2.0);
        eq.solve();
        assertTrue(eq.isSolved());

        // Cambiando a, la soluzione precedente non appartiene più allo stato corrente.
        eq.setA(2.0);
        assertEquals(2.0, eq.getA());
        assertFalse(eq.isSolved());

        // Finché non si risolve di nuovo, la vecchia soluzione non deve essere leggibile.
        assertThrows(IllegalStateException.class, eq::getSolution);
    }

    @Test
    void testSetARifiutaValoreNonValidoSenzaModificareLoStato() {
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, -3.0,
                        2.0);
        eq.solve();
        SoluzioneEquazioneSecondoGrado soluzionePrimaDelTentativo =
                eq.getSolution();

        // Un aggiornamento non valido deve essere segnalato tramite eccezione.
        assertThrows(IllegalArgumentException.class, () -> eq.setA(0.0));

        // Poiché il coefficiente non è stato cambiato, lo stato valido precedente resta intatto.
        assertEquals(1.0, eq.getA());
        assertTrue(eq.isSolved());
        assertSame(soluzionePrimaDelTentativo, eq.getSolution());
    }

    @Test
    void testSetBModificaIlCoefficienteEInvalidaLaSoluzione() {
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, -3.0,
                        2.0);
        eq.solve();

        eq.setB(5.0);

        assertEquals(5.0, eq.getB());
        assertFalse(eq.isSolved());
        assertThrows(IllegalStateException.class, eq::getSolution);
    }

    @Test
    void testSetCModificaIlCoefficienteEInvalidaLaSoluzione() {
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, -3.0,
                        2.0);
        eq.solve();

        eq.setC(10.0);

        assertEquals(10.0, eq.getC());
        assertFalse(eq.isSolved());
        assertThrows(IllegalStateException.class, eq::getSolution);
    }

    @Test
    void testGetSolutionPrimaDiSolveLanciaEccezione() {
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, 0.0,
                        -1.0);

        // getSolution non deve effettuare implicitamente il calcolo.
        assertFalse(eq.isSolved());
        assertThrows(IllegalStateException.class, eq::getSolution);
    }

    @Test
    void testSolveConDeltaNegativoProduceSoluzioneVuota() {
        // x^2 + 1 = 0 non ha soluzioni reali: delta = -4.
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, 0.0,
                        1.0);

        eq.solve();
        SoluzioneEquazioneSecondoGrado sol = eq.getSolution();

        assertTrue(eq.isSolved());
        assertTrue(sol.isEmptySolution());
        assertFalse(sol.isOneSolution());
        assertTrue(Double.isNaN(sol.getS1()));
        assertTrue(Double.isNaN(sol.getS2()));
    }

    @Test
    void testSolveConDeltaNulloProduceDueSoluzioniCoincidenti() {
        // x^2 - 2x + 1 = 0 ha la soluzione doppia x = 1.
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, -2.0,
                        1.0);

        eq.solve();
        SoluzioneEquazioneSecondoGrado sol = eq.getSolution();

        assertTrue(eq.isSolved());
        assertFalse(sol.isEmptySolution());
        assertTrue(sol.isOneSolution());
        assertEquals(1.0, sol.getS1(), EPSILON);
        assertTrue(Double.isNaN(sol.getS2()));
    }

    @Test
    void testSolveConDeltaPositivoProduceDueSoluzioniDistinte() {
        // x^2 - 3x + 2 = 0 ha radici 1 e 2.
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, -3.0,
                        2.0);

        eq.solve();
        SoluzioneEquazioneSecondoGrado sol = eq.getSolution();

        assertTrue(eq.isSolved());
        assertFalse(sol.isEmptySolution());
        assertFalse(sol.isOneSolution());

        // Non imponiamo quale delle due radici debba essere memorizzata in s1.
        boolean ordine12 = Math.abs(sol.getS1() - 1.0) < EPSILON
                && Math.abs(sol.getS2() - 2.0) < EPSILON;
        boolean ordine21 = Math.abs(sol.getS1() - 2.0) < EPSILON
                && Math.abs(sol.getS2() - 1.0) < EPSILON;
        assertTrue(ordine12 || ordine21);
    }

    @Test
    void testSolveNonRicalcolaSeLoStatoNonCambia() {
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, -3.0,
                        2.0);

        eq.solve();
        SoluzioneEquazioneSecondoGrado prima = eq.getSolution();

        // La specifica richiede che una seconda solve, senza modifiche, non ricalcoli.
        eq.solve();
        SoluzioneEquazioneSecondoGrado seconda = eq.getSolution();

        assertSame(prima, seconda);
    }

    @Test
    void testModificaERisoluzioneSuccessivaAggiornanoLaSoluzione() {
        EquazioneSecondoGradoModificabileConRisolutore eq =
                new EquazioneSecondoGradoModificabileConRisolutore(1.0, -3.0,
                        2.0);
        eq.solve();
        SoluzioneEquazioneSecondoGrado vecchia = eq.getSolution();

        // Cambiamo l'equazione in x^2 - 2x + 1 = 0.
        eq.setB(-2.0);
        eq.setC(1.0);
        assertFalse(eq.isSolved());

        eq.solve();
        SoluzioneEquazioneSecondoGrado nuova = eq.getSolution();

        assertTrue(eq.isSolved());
        assertNotSame(vecchia, nuova);
        assertTrue(nuova.isOneSolution());
        assertEquals(1.0, nuova.getS1(), EPSILON);
    }
}
