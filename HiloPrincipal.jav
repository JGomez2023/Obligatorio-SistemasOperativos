import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Hilo principal que lee el archivo de entrada y clasifica los eventos en las colas correspondientes
class HiloPrincipal extends Thread {
    private final BlockingQueue<Persona> colaAlta;
    private final BlockingQueue<Persona> colaMedia;
    private final BlockingQueue<Persona> colaBaja;

    public HiloPrincipal(BlockingQueue<Persona> colaAlta, BlockingQueue<Persona> colaMedia, BlockingQueue<Persona> colaBaja) {
        this.colaAlta = colaAlta;
        this.colaMedia = colaMedia;
        this.colaBaja = colaBaja;
    }

    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                boolean esMujer = Boolean.parseBoolean(partes[0]);
                boolean movilidadReducida = Boolean.parseBoolean(partes[1]);
                boolean embarazada = Boolean.parseBoolean(partes[2]);
                boolean esVIP = Boolean.parseBoolean(partes[3]);
                boolean esEstudiante = Boolean.parseBoolean(partes[4]);
                boolean esPersonal = Boolean.parseBoolean(partes[5]);
                boolean esVisitante = Boolean.parseBoolean(partes[6]);
                boolean esDesconocido = Boolean.parseBoolean(partes[7]);
                long tiempoReconocimiento = Long.parseLong(partes[8]);

                Persona persona = new Persona(esMujer, movilidadReducida, embarazada, esVIP, esEstudiante,
                        esPersonal, esVisitante, esDesconocido, tiempoReconocimiento);

                if (esDesconocido || (!esVIP && !esEstudiante && !esPersonal && !esVisitante)) {
                    System.out.println("Alerta: Persona desconocida o no permitida detectada.");
                } else if (esVIP || movilidadReducida || embarazada) {
                    colaAlta.put(persona);
                } else if (esEstudiante || esPersonal) {
                    colaMedia.put(persona);
                } else if (esVisitante) {
                    colaBaja.put(persona);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}