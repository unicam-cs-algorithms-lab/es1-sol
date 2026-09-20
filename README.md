# Esercitazione 1 - Richiami di Java, OOP, Javadoc e Unit Testing

## Obiettivo dell'esercitazione

Questa prima esercitazione riprende alcuni concetti di Java e programmazione orientata agli oggetti necessari per il Laboratorio di Algoritmi e Strutture Dati.

Il progetto usa come esempio un'applicazione per la risoluzione di equazioni di secondo grado e permette di lavorare, in particolare, sui seguenti aspetti:

- oggetti come entità dotate di **stato** e **metodi**;
- **incapsulamento** dello stato e uso di API pubbliche;
- differenza tra oggetti **immutabili** e **mutabili**;
- separazione tra **business logic** e **front-end**;
- uso delle **eccezioni** per segnalare condizioni anomale;
- documentazione delle API tramite **Javadoc**;
- verifica del comportamento tramite **unit testing con JUnit 5**;
- comprensione del flusso di controllo tramite attività di **debug**.

I commenti Javadoc presenti nelle classi fanno parte del materiale didattico e devono essere letti insieme al codice.

---

## Struttura del progetto

Il progetto Maven contiene, tra le altre, le seguenti classi.

### `EquazioneSecondoGrado`

Rappresenta un'equazione di secondo grado **immutabile**. I coefficienti vengono fissati al momento della costruzione e non possono essere modificati successivamente.

La classe mostra anche esempi di ridefinizione dei metodi `equals`, `hashCode`, `compareTo` e `toString`.

### `SoluzioneEquazioneSecondoGrado`

Rappresenta in forma strutturata il risultato della risoluzione di un'equazione. Può descrivere:

- nessuna soluzione reale;
- due soluzioni coincidenti;
- due soluzioni reali distinte.

Anche questa classe è immutabile.

### `EquazioneSecondoGradoModificabileConRisolutore`

È la classe principale da completare nell'esercitazione.

Rappresenta un'equazione **mutabile**: i coefficienti possono cambiare nel tempo e l'oggetto incorpora anche la capacità di calcolare e memorizzare la soluzione relativa allo stato corrente.

### `EquazioniModificabiliTextualFrontEnd`

È un front-end testuale che interagisce con l'utente tramite standard input e standard output e utilizza l'API pubblica della classe precedente.

La classe contiene intenzionalmente un **bug** nella gestione dei tentativi di inserimento dei coefficienti. Individuare e correggere il bug è parte dell'esercitazione.

### `EquazioneSecondoGradoModificabileConRisolutoreTest`

Contiene test JUnit 5 relativi alle funzionalità che devono essere implementate. I test sono anche esempi di come definire scenari di test, casi limite e proprietà osservabili di un oggetto.

---

## Parte 1 - Completamento della business logic

Completare **esclusivamente** i punti contrassegnati con:

```java
// TODO implementare
```

nella classe:

```text
EquazioneSecondoGradoModificabileConRisolutore.java
```

Devono essere implementati:

- il costruttore;
- `setA(double a)`;
- `setB(double b)`;
- `setC(double c)`;
- `solve()`;
- `getSolution()`.

### Stato dell'oggetto e soluzione corrente

Un oggetto appena costruito deve risultare **non risolto**.

Quando uno dei coefficienti viene modificato, l'eventuale soluzione calcolata in precedenza non è più valida per il nuovo stato. Di conseguenza l'oggetto deve tornare nello stato **non risolto**.

Il metodo `solve()` calcola la soluzione dell'equazione definita dai coefficienti correnti. Se l'equazione è già risolta e i coefficienti non sono cambiati, non è necessario eseguire nuovamente il calcolo.

Il metodo `getSolution()` deve restituire il risultato soltanto quando la soluzione memorizzata è valida per lo stato corrente. In caso contrario deve lanciare `IllegalStateException`.

### Validità del coefficiente `a`

Per essere di secondo grado, l'equazione deve avere coefficiente `a` diverso da zero. Nel progetto un valore viene considerato nullo quando:

```text
|a| < 1.0E-15
```

Il costruttore e `setA` devono quindi lanciare `IllegalArgumentException` in questo caso.

### Risoluzione dell'equazione

Usare il discriminante:

```text
delta = b^2 - 4ac
```

per distinguere i tre casi:

- `delta < 0`: nessuna soluzione reale;
- `delta = 0`: due soluzioni coincidenti;
- `delta > 0`: due soluzioni reali distinte.

Il risultato deve essere rappresentato mediante un oggetto `SoluzioneEquazioneSecondoGrado`.

---

## Parte 2 - Esecuzione e comprensione dei test JUnit

Eseguire la classe:

```text
EquazioneSecondoGradoModificabileConRisolutoreTest
```

prima di completare i `TODO`: diversi test dovranno inizialmente fallire.

Dopo l'implementazione, tutti i test devono passare.

Non limitarsi a usare i test come indicatore verde/rosso: leggere i metodi di test e i relativi commenti per capire:

- quale scenario viene costruito;
- quali operazioni vengono eseguite;
- quale proprietà viene verificata con ogni `assert`;
- perché sono necessari test distinti per discriminante negativo, nullo e positivo;
- come si verifica che una modifica dello stato invalidi una soluzione precedente.

---

## Parte 3 - Ricerca del bug nel front-end testuale

La classe:

```text
EquazioniModificabiliTextualFrontEnd.java
```

contiene intenzionalmente un bug.

Il programma funziona normalmente se tutti i coefficienti vengono inseriti correttamente al primo tentativo. Se invece, durante l'inserimento di `a`, `b` o `c`, è necessario almeno un nuovo tentativo a causa di un input non valido, il comportamento successivo può non essere corretto e il programma può continuare a richiedere input anche dopo aver ricevuto un valore valido.

### Attività richiesta

1. Eseguire il front-end e riprodurre il problema.
2. Usare il debugger di IntelliJ IDEA oppure seguire manualmente il valore delle variabili coinvolte nel controllo del ciclo.
3. Individuare l'origine del bug.
4. Correggere il codice mantenendo invariata la separazione tra front-end e business logic.
5. In fondo alla classe, completare il commento predisposto spiegando brevemente:
    - l'origine del bug;
    - perché provocava il comportamento osservato;
    - la modifica effettuata per risolverlo.

Non è sufficiente indicare soltanto la riga modificata: la spiegazione deve mostrare di aver compreso il flusso di controllo che produce il problema.

---

## Indicazioni generali

- Usare **Java 8**.
- Non modificare le firme dei metodi forniti.
- Non eliminare o aggirare i controlli richiesti dalla specifica.
- La business logic non deve leggere da tastiera né stampare messaggi per comunicare risultati o errori.
- Gli errori relativi allo stato o ai parametri devono essere segnalati tramite le eccezioni indicate nei Javadoc.
- È possibile usare i test JUnit come guida durante l'implementazione.

---

## Consegna

La consegna deve essere effettuata nel **compito corrispondente presente nel Moodle del corso**.

Devono essere consegnati **esattamente due file**:

1. `EquazioneSecondoGradoModificabileConRisolutore.java`
    - con tutti i `TODO implementare` completati;

2. `EquazioniModificabiliTextualFrontEnd.java`
    - con il bug corretto;
    - con, in fondo alla classe, il commento che descrive l'origine del bug e la soluzione adottata.

Non è necessario consegnare le altre classi del progetto né la classe di test.

Prima della consegna verificare che:

- il progetto compili;
- tutti i test JUnit passino;
- il front-end gestisca correttamente anche input errati seguiti da un nuovo tentativo valido;
- i due file da consegnare mantengano package e nomi originali.
