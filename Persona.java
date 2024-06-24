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