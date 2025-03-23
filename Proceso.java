import java.io.*;
import java.util.Random;

public class Proceso {
    int PID = 0; //identificador de proceso
    static int nextPID = 0; //siguiente proceso en la cola
    int temp_restante=0; //tiempo que utiliza el proceso [0 - 100]
    int temp_inicial=0; //tiempo inicial
    int recurso = 0;
    int memoria = (int) (Math.random() * 100) + 1;;
    String nombre = "";
    
    //los 3 estados siguen a continuacion
    boolean ready = true; //checar si un proceso llego o no
    boolean done = false;  //para saber si termino
    boolean running = false;   //para ver si esta programado
    //estados agregados para la segunda fase del proyecto
    boolean blocked = false;
    
    //valores booleanos para que un proceso no pueda tomar mas de dos recursos
    boolean r1 = false;
    boolean r2 = false;
    boolean r3 = false;
    boolean r4 = false;
    boolean r5 = false;
  
    private static int contadorNombre = 1;
    // constructor Default - genera un proceso y llena los valores
    public Proceso() {
        this.PID = ++nextPID;
        this.temp_restante = (int) (Math.random() * 90) + 10;
        this.temp_inicial = this.temp_restante;
        this.memoria = (int) (Math.random() * 100) + 1;
        this.nombre = "P" + contadorNombre++;
        this.ready = true;
    }
    

    //constructor por si la info viene de archivo
    //@param nombre, tiempo
    Proceso(String b, int d){

    nextPID++;
    PID = nextPID;
    temp_restante = d; //tiempo de vida
        temp_inicial = temp_restante;
        nombre = b;

     }


    //Metodos incidentes en el estado del proceso
    //en estado corriendo
    public void corriendo(){

       running = true;
       ready = false;
           blocked = false;

           temp_restante--;//se va descontando el tiempo restante
           //si el tiempo se acaba, entonces el proceso es terminado
       if( temp_restante == 0 ){
            done = true;
            running = false;
            ready = false;
            temp_restante = 0;
            
           }
    }

    //en estado de listo
    public  void listo(){
        ready = true;
    running = false;
    blocked = false;
    }

    public void bloqueado(){
        blocked = true;
        running = false;
        ready = false;
    }
    
    //Metodos de estado de proceso
    //si esta siendo ejecutado por CPU
    public boolean isRunning() {
      return running;
    }

    //si termino o no
    public boolean isDone() {
      return done;
    }

    //si esta listo
    public boolean isReady() {
      return ready;
    }

    //si esta listo
    public boolean isBlocked() {
      return blocked;
    }

    //obtiene valor de tiempo de vida
    public int getTRestante() {
      return temp_restante;
    }
    
    //valores iniciales de tiempo de vida
    public int getTInicial() {
      return temp_inicial;
    }
    
    //valor del PID
    public int getPID() {
      return PID;
    }

    public String getNombre(){
      return nombre;
    }
    
    public String getEstado(){

      String estado = "";
           if (blocked == true)
              estado = "bloqueado(" + getRecurso() +")";
           if (running == true)
              estado = "Corriendo";
           if (done == true)
              estado = "Terminado";
           if (ready == true)
              estado = "Listo";
      return estado;
    }

    public void setRecurso(int rec){

       recurso = rec;
    }
    
    public void generaRecurso(){

       int probabilidad = (int) (Math.random() * 5) + 1;
       setRecurso(probabilidad);
    }
    
    public int getRecurso(){
      return recurso;
    }

    //establece memoria en KB
    public int getMemoria(){

      return memoria;
    }
    
    public void changeBool(int i){
      switch (i){
        case 1 :
         if ( r1 == false)
           r1 = true;
         else
           r1 = false;
         break;
        case 2 :
         if ( r2 == false)
           r2 = true;
         else
           r2 = false;
         break;
        case 3 :
         if ( r3 == false)
           r3 = true;
         else
           r3 = false;
         break;
        case 4 :
         if ( r4 == false)
           r4 = true;
         else
           r4 = false;
         break;
        case 5 :
         if ( r5 == false)
           r5 = true;
         else
           r5 = false;
         break;
      }
    }
    
    public boolean getBool(int i){

    boolean recu = false;
    
      switch (i){
        case 1 : recu = r1; break;
        case 2 : recu = r2; break;
        case 3 : recu = r3; break;
        case 4 : recu = r4; break;
        case 5 : recu = r5; break;
      }
      return recu;
    }

    // Método para obtener la duración del proceso
    public int getDuracion() {
        return temp_inicial;
    }
} //--proceso