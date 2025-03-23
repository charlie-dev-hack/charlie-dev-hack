import javax.swing.JOptionPane;

public class Emulador {
    public static void main(String[] args) {
        // Verificamos los argumentos para saber si usar FIFO o RR
        String opcion = "";
        if (args.length == 1) {
            opcion = args[0];
        } else if (args.length == 2) {
            opcion = args[0] + " " + args[1];
        } else {
            // Mostrar diálogo para seleccionar la opción si no hay argumentos válidos
            String[] options = {"FIFO", "Round Robin"};
            int selectedOption = JOptionPane.showOptionDialog(
                null,
                "Seleccione el algoritmo de planificación:",
                "Algoritmo de Planificación",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );

            // Verificar la opción seleccionada
            if (selectedOption == 0) {
                opcion = "FIFO";
            } else if (selectedOption == 1) {
                opcion = "Round Robin";
            } else {
                System.out.println("No se seleccionó una opción válida.");
                System.exit(0);
            }
        }

        // Cargamos la interfaz
        Interfaz fr = new Interfaz(opcion);
    }
}