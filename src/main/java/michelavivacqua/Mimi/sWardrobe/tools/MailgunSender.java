package michelavivacqua.Mimi.sWardrobe.tools;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailgunSender {
    private String apiKey;
    private String domainName;

    public MailgunSender(@Value("${mailgun.apikey}") String apiKey, @Value("${mailgun.domainname}") String domainName){
        this.apiKey = apiKey;
        this.domainName = domainName;
    }

    public void sendRegistrationEmail(Utente recipient){
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/"+ this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", "vivacquamichela@gmail.com")
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Registrazione Completata!")
                .queryString("text", "Grazie " + recipient.getName() + " per esserti registrato! Adesso saprai sempre in anticipo cosa indossare, e farai sempre un figurone!" +
                        "Mimi's Wardrobe è l'app che risponderà tutte le volte alla tua domanda: ❔\"Che cosa indosso oggi?\"❔\n" +
                        "Registra i tuoi indumenti ed i tuoi accessori, consultali, filtrali!" +
                        "Crea i tuoi outfit, ordinali per occasione, segna quando li indossi,\n" +
                        "e sfrutterai al meglio ogni angolo del tuo armadio! " +
                        "Mimi's Wardrobe svolge al posto tuo il noioso compito di preparare la valigia, così, per la tua vacanza, dovrai pensare solo a divertirti." +
                        "Mimi's Wardrobe ti fornisce le informazioni meteo di dove andrai, e ti consiglia cosa indossare in base alla temperatura!" +
                        "Che aspetti?! Vuoi un'altra volta arrivare in ritardo all'aperitivo perchè sei davanti all'armadio ?" +
                        "Mantieni anche tu il tuo armadio a portata di app")


                .asJson();

        System.out.println(response.getBody());
    }

}