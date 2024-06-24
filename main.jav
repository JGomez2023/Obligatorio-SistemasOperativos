import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Clase Persona que contiene las propiedades de cada persona identificada por las cámaras
class Persona {
    boolean esMujer;
    boolean movilidadReducida;
    boolean embarazada;
    boolean esVIP;
    boolean esEstudiante;
    boolean esPersonal;
    boolean esVisitante;
    boolean esDesconocido;
    long tiempoReconocimiento;

    public Persona(boolean esMujer, boolean movilidadReducida, boolean embarazada, boolean esVIP,
                   boolean esEstudiante, boolean esPersonal, boolean esVisitante, boolean esDesconocido,
                   long tiempoReconocimiento) {
        this.esMujer = esMujer;
        this.movilidadReducida = movilidadReducida;
        this.embarazada = embarazada;
        this.esVIP = esVIP;
        this.esEstudiante = esEstudiante;
        this.esPersonal = esPersonal;
        this.esVisitante = esVisitante;
        this.esDesconocido = esDesconocido;
        this.tiempoReconocimiento = tiempoReconocimiento;
    }

    @Override
    public String toString() {
        return String.format("esMujer: %b, movilidadReducida: %b, embarazada: %b, esVIP: %b, esEstudiante: %b, esPersonal: %b, esVisitante: %b, esDesconocido: %b, tiempoReconocimiento: %d",
                esMujer, movilidadReducida, embarazada, esVIP, esEstudiante, esPersonal, esVisitante, esDesconocido, tiempoReconocimiento);
    }
}

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

// Hilo de procesamiento que procesa las personas de las colas de prioridad
class HiloProcesamiento extends Thread {
    private final BlockingQueue<Persona> cola;
    private final String nombreCola;
    private final BlockingQueue<Persona> colaAlta;
    private int contadorProcesados = 0;

    public HiloProcesamiento(BlockingQueue<Persona> cola, String nombreCola, BlockingQueue<Persona> colaAlta) {
        this.cola = cola;
        this.nombreCola = nombreCola;
        this.colaAlta = colaAlta;
    }

    public void run() {
        try {
            while (true) {
                Persona persona = cola.take();
                procesarPersona(persona);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para procesar una persona
    private void procesarPersona(Persona persona) throws InterruptedException {
        System.out.println("Procesando persona en " + nombreCola + ": " + persona);
        Thread.sleep(persona.tiempoReconocimiento); // Simula el tiempo de procesamiento
        contadorProcesados++;
        if (nombreCola.equals("Cola Media") && contadorProcesados == 3) {
            colaAlta.put(persona); // Retroalimentación: promueve a la cola alta
            contadorProcesados = 0;
            System.out.println("Promoviendo persona a Cola Alta debido a retroalimentación.");
        }
    }
}

// Clase principal que inicializa las colas y los hilos de procesamiento
public class ControlAccesoFacial {
    public static void main(String[] args) {
        BlockingQueue<Persona> colaAlta = new LinkedBlockingQueue<>();
        BlockingQueue<Persona> colaMedia = new LinkedBlockingQueue<>();
        BlockingQueue<Persona> colaBaja = new LinkedBlockingQueue<>();

        HiloPrincipal hiloPrincipal = new HiloPrincipal(colaAlta, colaMedia, colaBaja);
        HiloProcesamiento hiloAlta = new HiloProcesamiento(colaAlta, "Cola Alta", colaAlta);
        HiloProcesamiento hiloMedia = new HiloProcesamiento(colaMedia, "Cola Media", colaAlta);
        HiloProcesamiento hiloBaja = new HiloProcesamiento(colaBaja, "Cola Baja", colaAlta);

        hiloPrincipal.start();
        hiloAlta.start();
        hiloMedia.start();
        hiloBaja.start();
    }
}