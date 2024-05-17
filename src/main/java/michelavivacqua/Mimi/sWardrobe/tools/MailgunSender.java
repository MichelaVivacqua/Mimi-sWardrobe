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
                .queryString("text", "Grazie " + recipient.getName() + " per esserti registrato! Adesso saprai sempre in anticipo cosa indossare, e farai sempre un figurone")
                .asJson();

        System.out.println(response.getBody());
    }

}