import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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