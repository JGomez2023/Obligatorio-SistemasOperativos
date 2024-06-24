import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Clase principal que inicializa las colas y los hilos de procesamiento
public class Main {
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