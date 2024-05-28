# Mimi's_Wardrobe

🎀Mimi's Wardrobe è l'app che risponderà tutte le volte alla tua domanda: ❔"Che cosa indosso oggi?"❔

Registra i tuoi indumenti ed i tuoi accessori, consultali, filtrali! 👗🕶️

Crea i tuoi outfit, ordinali per occasione, segna quando li indossi,
e sfrutterai al meglio ogni angolo del tuo armadio! 🛍️👠

Mimi's Wardrobe svolge al posto tuo il noioso compito di preparare la valigia, così, per la tua vacanza, dovrai pensare solo a divertirti. 🧳☀

Mimi's Wardrobe ti fornisce le informazioni meteo di dove andrai, e ti consiglia cosa indossare in base alla temperatura! 🌡 👙🧥

Che aspetti?! Vuoi un'altra volta arrivare in ritardo all'aperitivo perchè sei davanti all'armadio ⁉️
Mantieni anche tu il tuo armadio a portata di app 📱

## Installazione e configurazione

🔧 ⚙️

1. Clona il repository da GitHub.
2. Assicurati di avere PostgreSQL installato sul tuo sistema e crea un database
3. Crea nel progetto il file `env.properties` con le variabili d'ambiente necessarie ( SERVER_PORT, PG_USERNAME, PG_PASSWORD, PG_URL con relativo riferimento alla porta in cui gira il frontend, es. jdbc:postgresql://localhost:5432/wardrobe, CLOUDINARY_NAME, CLOUDINARY_SECRET, CLOUDINARY_KEY, JWT_SECRET, MAILGUN_API_KEY, MAILGUN_DOMAIN_NAME)
4. Aggiungi nel file application.properties la riga spring.config.import=file:env.properties
5. Esegui l'applicazione nel tuo IDE

## API Endpoints

L'applicazione espone API endpoints per interagire con il database. La documentazione completa è disponibile in formato OpenAPI (Swagger).

Puoi visualizzarla documentazione utilizzando Swagger Editor:

1. Vai a [Swagger Editor](https://editor.swagger.io/).
2. Clicca su `File -> Import File`.
3. Seleziona il file `openapi.json` dalla directory `/docs`.

Oppure puoi visualizzarla direttamente dal browser aprendo il file `openapi.json` con un visualizzatore di OpenAPI.


## Link al repository front-end

🖇️
https://github.com/MichelaVivacqua/mimis_wardrobe_fe

## Link alla versione deployata:

https://amused-jeniece-mimiswardrobe-dcb5d9c7.koyeb.app/

front-end: https://mimis-wardrobe-fe.vercel.app/MyNavbar

## Contatti

Per domande, segnalazioni di bug o suggerimenti, contattami all'indirizzo email [vivacquamichela@gmail.com] !

