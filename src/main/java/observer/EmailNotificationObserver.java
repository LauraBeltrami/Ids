package observer;

import Model.InvitoEvento;
import org.springframework.stereotype.Component;

@Component // Importante: deve essere un Bean Spring
public class EmailNotificationObserver implements InvitoObserver {

    @Override
    public void onInvitoCreato(InvitoEvento invito) {
        System.out.println("📧 [OBSERVER - EMAIL] Invio mail a: " +
                invito.getVenditore().getEmail());
        System.out.println("   Oggetto: Sei stato invitato all'evento " +
                invito.getEvento().getTitolo());
        System.out.println("   Messaggio: " + invito.getNota());
    }
}
