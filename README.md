# alg-demo
## Mockups

<details>
  <summary>Command durch Auswahl wählen:</summary>
  <img src="images/command-1.png" alt="Command 1"/>
  <img src="images/command-2.png" alt="Command 2"/>
  <img src="images/command-3.png" alt="Command 3"/>
</details>


<details>
  <summary>Fehlerhafter Command ausführen:</summary>
  <img src="images/command-error-1.png" alt="Fehlerhafter Command 1"/>
  <img src="images/command-error-2.png" alt="Fehlerhafter Command 2"/>
  <img src="images/command-error-3.png" alt="Fehlerhafter Command 3"/>
  <img src="images/command-error-4.png" alt="Fehlerhafter Command 4"/>
</details>


<details>
  <summary>Durch History zurückspringen:</summary>
  <img src="images/history-1.png" alt="History 1"/>
  <img src="images/history-2.png" alt="History 2"/>
  <img src="images/history-3.png" alt="History 3"/>
  <img src="images/history-4.png" alt="History 4"/>
  <img src="images/history-5.png" alt="History 5"/>
</details>


<details>
  <summary>Durch History zurückspringen (abbrechen):</summary>
  <img src="images/history-1.png" alt="History abbrechen 1"/>
  <img src="images/history-2.png" alt="History abbrechen 2"/>
  <img src="images/history-3.png" alt="History abbrechen 3"/>
  <img src="images/history-abort-1.png" alt="History abbrechen 4"/>
  <img src="images/history-abort-2.png" alt="History abbrechen 5"/>
</details>

### Funktionalitäten
- Switch zwischen verschiedenen Algorithmen möglich.
  - Als erstes Umsetzung für Binary Search
  - Zweiter Algorithmus MergeSort(Noch nicht zu 100%)
- Anzeige des aktuellen Status des Algorithmus in der Mitte. 
  - Zeigt je nach Algorithmus auch gerade die Variablen(wie hier im Beispiel mit Binary Search).
    - Bearbeiten des Arrays vom Algorithmus möglich.
    - Aktuelle Values vom Algorithmus speichern 
    - Letzte gespeicherte Config laden
- Anzeigen von aktuellen Variablenbelegungen.
- Textfeld für Eingabe eines Commands für den Algorithmus mit Sendeknopf.
  - Absenden & ausführen des Commands durch den Sendeknopf.
- History aller abgesetzten Commands. 
  - Durch Klicken auf ein History-Element wird der Status nach dieses Commands wieder hergestellt und ist in der Mitte sichtbar. 
  - Durch Klicken auf das Kopier-Icon eines History Elements den verwendeten Command in das Textfeld einfügen.

## ToDos
- [ ] How to Feedbackmarkt
- [ ] How to Doku
- [ ] Doku up-to-date bringen
- [ ] Commands können durch Enter-Taste auf Button oder Textfield abgesetzt werden.
- [ ] Binary Search ist Horizontal in der Mitte
- [ ] Es können Commands mit Variablen, +, (), / genutzt werden.
- [ ] In der History gibt es einen Eintrag für den Initialzustand.
- [ ] Der Zustand des Algorithmus kann durch die History verändert werden.
- [ ] Binary Search Algorithmus
    - [x] Der Zustand des Algorithmus wird angezeigt.
    - [ ] Commands können auf den Algorithmus angewendet werden.
    - [ ] Das Array vom BinarySearch kann bearbeitet werden.
    - [ ] Das Bearbeiten des Arrays wird validiert.
    - [ ] Die Arraygrösse kann verändert werden.
    - [ ] Der Zustand des Algorithmus kann gespeichert werden.
    - [ ] Der Zustand des Algorithmus kann aus dem gespeicherten Zustand wiederhergestellt werden.
- [ ] Commands
    - [x] Es gibt Commandvorschläge, welche ausgewählt werden können.
    - [x] Commands können selbst eingegeben werden.
    - [x] Commands können abgesetzt werden.
    - [ ] Commands werden überprüft.
- [ ] History
    - [x] Die History wird angezeigt.
    - [ ] In der History gibt es einen Eintrag für den Initialzustand.
    - [x] Für jeden abgesetzten Command wird ein Historyeintrag erstellt.
    - [x] Überläuft die History den Bildschirm, so ist sie scrollable.
    - [x] Commands aus der History können kopiert werden. 
    - [ ] Der Zustand des Algorithmus kann durch die History verändert werden.

## Fragen & Antworten
### Was kann der Benutzer machen, was falsch ist? Wie reagiert das System?
- Er kann einen Command abgeben, welcher falsch ist oder nichts mit dem Algorithmus zu tun hat.
    - Die Applikation merkt, dass der Command falsch ist oder keinen Effekt hat. In der History ist der falsche Command ersichtlich. In der zweiten Zeile dieses History-Elements steht jedoch nicht der Effekt einer erfolgreichen Zuweisung, sondern eine sprechende Fehlermeldung, die den Benutzer dazu leitet, einen richtigen Command einzugeben.
- Er kann durch die History in einen alten Zustand zurückspringen und von dort aus einen neuen Command absetzen.
  - Es kommt eine Warnung die bestätigt werden muss, dass das System im Begriff ist den Zustand ab dem aktuellen Zeitpunkt aus zu Überschreiben. Wenn bestätigt, löscht die Applikation alle abgesetzten Commands nach dem, welcher in der History ausgewählt ist und fügt den neuen Command danach hinzu.
### Wie kann ich den Benutzer leiten, so dass er intuitiv weiss, was zu tun ist?
- Die Variablen welche im Algorithmus verwendet werden können sind schon benannt. Für BinarySearch gibt es Beispielsweise die Variablen:
  - I = null
  - J = null
  - M = null
  Es können keine neuen Variablen erstellt werden. 
  Der Algorithmus kann durch dann durch das Zuweisen von Werten zu diesen Variablen ausprobiert werden.
- Anstelle eines normalen Textfeldes für die Eingabe der Commands, verwende ich ein Textfeld, welches schon vordefinierte Beispiele für jede Variable bietet, für alle möglichen Variablenzuweisungen.
  - Mögliche Commands:
    - Zuweisungen zu den existierenden Variablen mit demselben Datentyp:
        - I = 0;
        - J = 15;
        - M = (i + j) / 2;
    - Unzulässige Commands:
      - I = "ein String";
        - Nicht derselbe Datentyp
      - I = 15
        - Kein Semicolon am Schluss
      - 15;
        - Keine Zuweisung einer existierenden Variable
      - X = 5;
        - Keine Zuweisung einer existierenden Variable
      - M = i + 1;
        - Geht nicht, wenn i noch keinen Wert hat
