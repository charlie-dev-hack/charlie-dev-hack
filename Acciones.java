import java.util.*;
import java.io.*;
import java.text.*;
import java.lang.System;

public class Acciones{
    //constantes para saber tipo de simulacion y numero de procesos aleatorios
    public static final int ALEATORIO  = 5;
    public static final int FIFO       = 1;   //FIFO
    public static final int ROUNDROBIN = 2;   // round - robin
    public static final int bloqueo_aleatorio = 3;
    public static final int bloqueo_banquero = 4;
    
    public static final int max_recurso = 3;
    //los cinco recursos
    int recurso_1 = max_recurso;
    int recurso_2 = max_recurso;
    int recurso_3 = max_recurso;
    int recurso_4 = max_recurso;
    int recurso_5 = max_recurso;
    

    //colas de procesos, por default son siete procesos aleatorios
    Vector allProcesos   = new Vector(ALEATORIO); //lista de todos los procesos

    int currentTime=0;  //tiempo transcurrido
    int quantum=10;   //establecemos quantum
    int quantum_transcurrido = quantum;  //cuenta regresiva para el quantum
    int algorithm = this.FIFO; //algoritmo usado
    int bloqueo = this.bloqueo_aleatorio;
    int activeIndex = 0;  //indice de vector


    /*constructor que genera procesos aleatorios
     se utilizo para hacer pruebas y no quisimos borrarlo
     pues sirve como 'memoria' cuando no queremos
     agregar procesos uno por uno*/
    Acciones(){

	Proceso p;

        for(int i=0; i < ALEATORIO; i++){
	    p = new Proceso();
	    allProcesos.add(p);
	}

    }//--Acciones
    

    //constructor que lee desde la linea de comando
    public Acciones(String filename) {
        if (filename.equals("FIFO")) {
            System.out.println("Ejecutando programa con opción FIFO...");
            algorithm = this.FIFO;
        } else if (filename.equals("Round Robin")) {
            System.out.println("Ejecutando programa con opción Round Robin...");
            algorithm = this.ROUNDROBIN;
        } else {
            System.out.println("Datos de entrada no válidos.");
            System.out.println("Debe ejecutar una de las sig. formas:");
            System.out.println("java Emulador FIFO");
            System.out.println("java Emulador Round Robin");
            System.exit(0);
        }

        String archivo = filename + ".txt";
        File file = new File(archivo).getAbsoluteFile();

        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            String s = input.readLine();
            int num_procesos = Integer.parseInt(s);

            for (int i = 0; i < num_procesos; i++) {
                s = input.readLine();
                StringTokenizer st = new StringTokenizer(s, " \n\t\r,", false);
                st.nextToken();
                String nom = st.nextToken();
                int d = Integer.parseInt(st.nextToken());

                Proceso proc = new Proceso(nom, d);
                allProcesos.add(proc);
            }

            if (algorithm == this.ROUNDROBIN && (s = input.readLine()) != null) {
                int rr = Integer.parseInt(s);
                this.setQuantum(rr);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }//Acciones

   // lee datos ingresados del textArea y los agrega como procesos
  
    void agrega_proceso(String grosso) {
        Proceso proc = null;
        String s = null;
        int d = 0;
        String nom = null;

        s = grosso;
        StringTokenizer st = new StringTokenizer(s, " \n\t\r,", false);

        try {
            // Intentar leer el número de procesos
            String token = st.nextToken().trim();
            System.out.println("Número de procesos: " + token);
            int num_procesos = Integer.parseInt(token);

            for (int i = 0; i < num_procesos; i++) {
                // Verificar si hay suficientes tokens para el proceso actual
                if (!st.hasMoreTokens()) {
                    throw new IllegalArgumentException("Faltan datos para el proceso " + (i + 1));
                }
                // Nombrar el proceso
                nom = st.nextToken().trim();
                System.out.println("Nombre del proceso: " + nom);

                if (!st.hasMoreTokens()) {
                    throw new IllegalArgumentException("Duración faltante para el proceso " + nom);
                }
                token = st.nextToken().trim();
                System.out.println("Duración del proceso " + nom + ": " + token);
                d = Integer.parseInt(token);

                proc = new Proceso(nom, d); // creamos nuevos procesos
                allProcesos.add(proc); // lo agregamos al vector con todos los procesos
            }

            // Si es Round Robin, obtener el quantum
            if (st.hasMoreTokens()) {
                token = st.nextToken().trim();
                System.out.println("Quantum: " + token);
                int rr = Integer.parseInt(token);
                this.setQuantum(rr);
                algorithm = this.ROUNDROBIN;
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Error en el formato: " + nfe.getMessage());
            throw new IllegalArgumentException("Error en el formato: " + nfe.getMessage());
        } catch (NoSuchElementException nsee) {
            System.err.println("Datos insuficientes para el proceso: " + nsee.getMessage());
            throw new IllegalArgumentException("Datos insuficientes para el proceso: " + nsee.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            throw new IllegalArgumentException("Error inesperado: " + e.getMessage());
        }
    }//acciones


   //lee datos desde archivo
    Acciones(File filename){

	Proceso proc = null;
	String s = null;
        int d=0;
	String nom = "";
	
	try{
	    BufferedReader input = new BufferedReader(new FileReader(filename));
            s = input.readLine();
	   int num_procesos = Integer.parseInt(s);
	   
	   for (int i = 0; i < num_procesos; i++){

             s = input.readLine();
             StringTokenizer st = new StringTokenizer(s, " \n\t\r,", false);
             st.nextToken();
             nom = st.nextToken();
             d = Integer.parseInt(st.nextToken());
             
             proc = new Proceso(nom,d);
  	        allProcesos.add(proc);
            }
             //si es Round Robin, obten el quantum
            s = input.readLine();
            if (s != null) {

              int rr = Integer.parseInt (s);
              this.setQuantum(rr);
              algorithm = this.ROUNDROBIN;
            }

	 }

        catch(FileNotFoundException fnfe){}
	catch(IOException ioe){}
	//cuando se leen archivos FIFO trata de leer una linea mas, asi que ignoramos
	//la excepcion que genera
	catch(NumberFormatException nfe) {}
	
    }

   //liberamos el recurso que haya tenido ocupado
   void liberaRecurso(Proceso p){
      for (int i = 1; i <= 5; i++){

        //si termina sin liberar su recurso entonces lo libera
        if (p.getBool(i) == true){

           p.changeBool(i);
           incrementaRecurso(i);
        }
      }
   }


    //Metodo FIFO
    void RunFIFO(){

        Proceso p = null;
        
	try {
                p = (Proceso) allProcesos.get(activeIndex);

                if (p.isDone() == true){
                   liberaRecurso(p);
                   activeIndex++;

                   //para asegurarnos de que no se salga del vector
                   if( activeIndex == allProcesos.size() ){
                      activeIndex = 0;
                   }

	        } else if ( p.isBlocked() == true ){

                   desbloquear(p);

                } else {
                   Aplicar_Bloqueo(activeIndex);
                }
                

	}
	catch( NullPointerException e){
	}
    }

    //para desbloquear los procesos
    void desbloquear(Proceso p){

        if (bloqueo == bloqueo_aleatorio){

          int probabilidad = (int) (Math.random() * 100);
          //la probabilidad es del 10% de que sea desbloqueado
               if (probabilidad <= 10){
                  p.blocked = false;
               }
        } else if (bloqueo == bloqueo_banquero){
          //checar si puede desbloquearse en banquero

          int probabilidad2 = (int) (Math.random() * 100);
          if (probabilidad2 <= 10){

             switch (p.getRecurso()){
               case 0 :
                  break;
               case 1 :
                  p.blocked = false;
                  recurso_1++;
                  p.changeBool(1);
                  break;
               case 2 :
                  p.blocked = false;
                  recurso_2++;
                  p.changeBool(2);
                  break;
               case 3 :
                  p.blocked = false;
                  recurso_3++;
                  p.changeBool(3);
                  break;
               case 4 :
                  p.blocked = false;
                  recurso_4++;
                  p.changeBool(4);
                  break;
               case 5 :
                  p.blocked = false;
                  recurso_5++;
                  p.changeBool(5);
                  break;
             }//switch
             
          } else {
             //si no se libera entonces se va al siguiente proceso en la cola
             activeIndex++;
             if( activeIndex == allProcesos.size() ){
                      activeIndex = 0;
             }
          }//else
        }//else if
    }//desbloquear
    
    //Metodo Round Robin
    void RunRoundRobin(){

        Proceso p = null;
	
	try {

		p = (Proceso) allProcesos.get(activeIndex);
		
                if (quantum_transcurrido == 0){

                   activeIndex++;
                   quantum_transcurrido = quantum;
                   p.listo();
                   //para asegurarnos de que no se salga del vector
                   if( activeIndex == allProcesos.size() ){
                      activeIndex = 0;
                   }
                   
                } else if( p.isDone() == true ){
                   liberaRecurso(p);
                   activeIndex++;
                   quantum_transcurrido = quantum;
                   //para asegurarnos de que no se salga del vector
                   if( activeIndex == allProcesos.size() ){
                      activeIndex = 0;
                   }
	        } else if (p.isBlocked() == true ){

                   desbloquear(p);
                   
                } else {

                   Aplicar_Bloqueo(activeIndex);

                }
	    
	}catch( NullPointerException e){
	}
	
    }
    
    void RunBloqueoAleatorio(int index){

    Proceso p = null;
    p = (Proceso) allProcesos.get(index);
    
                  //en cada ejecucion exista la probabilidad de que se bloquee
                   int probabilidad = (int) (Math.random() * 1000);

                   //la probabilidad es del [.008 * tiempo_inicial]%  de que sea bloqueado
                   if (probabilidad <= 10){
                      p.bloqueado();
                      p.generaRecurso();
                      activeIndex++;

                      if( activeIndex == allProcesos.size() )
                          activeIndex = 0;

                    } else {
                      p.corriendo();
                      quantum_transcurrido--;
                    }
    }
    
    void RunBloqueoBanquero(int index){

         Proceso p = null;
         p = (Proceso) allProcesos.get(index);
         int el_recurso = 0;
         
         int probabilidad = (int) (Math.random() * 1000);
         if (probabilidad <= 200){
           p.generaRecurso();
           el_recurso = p.getRecurso();
           
           //checar por los recursos
           switch (el_recurso) {

             case 1 :
              if (recurso_1 > 0 && p.getBool(1) == false){
               recurso_1--;
               p.changeBool(1);
              }else if (p.getBool(1) == true){//no hace nada
              }else{
               p.bloqueado();
               activeIndex++;
               if( activeIndex == allProcesos.size() )
               activeIndex = 0;
              }break;
             //------------------------
             case 2 :
              if (recurso_2 > 0 && p.getBool(2) == false){
               recurso_2--;
               p.changeBool(2);
              }else if (p.getBool(2) == true){//no hace nada
              }else{
               p.bloqueado();
               activeIndex++;
               if( activeIndex == allProcesos.size() )
               activeIndex = 0;
              }break;
              //------------------------
              case 3 :
              if (recurso_3 > 0 && p.getBool(3) == false){
               recurso_3--;
               p.changeBool(3);
              }else if (p.getBool(3) == true){//no hace nada
              }else{
               p.bloqueado();
               activeIndex++;
               if( activeIndex == allProcesos.size() )
               activeIndex = 0;
              }break;
              //------------------------
              case 4 :
              if (recurso_4 > 0 && p.getBool(4) == false){
               recurso_4--;
               p.changeBool(4);
              }else if (p.getBool(4) == true){//no hace nada
              }else{
               p.bloqueado();
               activeIndex++;
               if( activeIndex == allProcesos.size() )
               activeIndex = 0;
              }break;
              //------------------------
              case 5 :
              if (recurso_5 > 0 && p.getBool(5) == false){
               recurso_5--;
               p.changeBool(5);
              }else if (p.getBool(5) == true){//no hace nada
              }else{
               p.bloqueado();
               activeIndex++;
               if( activeIndex == allProcesos.size() )
               activeIndex = 0;
              }break;
           } //switch
         }  else {
         //si no hay trabas
         //entonces corre
            p = (Proceso) allProcesos.get(activeIndex);
            p.corriendo();
            quantum_transcurrido--;
         }
    }

    //todo lo que ocurre en un ciclo
    public void sigCicloEjec(){

		Aplicar_Metodo();
	        currentTime++;

    }

    // seleccionamos el metodo y ejecutamos uno mientras los otros se va a cola de listos
    void Aplicar_Metodo(){
	switch( algorithm ){
	case FIFO :
	    RunFIFO();
	    break;
 	case ROUNDROBIN :
	    RunRoundRobin();
	    break;

	}
    }

    // seleccionamos el bloqueo
    void Aplicar_Bloqueo(int index){
	switch( bloqueo ){
	case bloqueo_aleatorio :
	    RunBloqueoAleatorio(index);
	    break;
 	case bloqueo_banquero :
	    RunBloqueoBanquero(index);
	    break;

	}
    }

    //accesores y modificadores
    public void setAlgorithm(int algo){ algorithm = algo;}  //pone el valor del metodo
    public void setBloqueo(int blo){ bloqueo = blo;}  //pone el valor del metodo
    public int getAlgorithm(){ return algorithm; }  //obtiene valor del metodo
    public int getBloqueo(){ return bloqueo; }  //obtiene valor del metodo
    public int getTotalTime() {return currentTime;}  //obtiene tiempo total que ha corrido la simulacion
    public int getQuantum() {return quantum;}   //obtiene valor de quantum
    public void setQuantum(int  v) {this.quantum = v;}   //pone el valor del quantum
    public Vector getProcesos(){ return allProcesos; }  //obtener todos los trabajos en el vector
    public int getRecurso(int i){
      int resultado = 0;
      switch (i){
        case 1: resultado = recurso_1; break;
        case 2: resultado = recurso_2; break;
        case 3: resultado = recurso_3; break;
        case 4: resultado = recurso_4; break;
        case 5: resultado = recurso_5; break;
      }
      return resultado;
    }
    
    public void incrementaRecurso(int i){

      switch (i){
        case 1: recurso_1++ ; break;
        case 2: recurso_2++ ; break;
        case 3: recurso_3++ ; break;
        case 4: recurso_4++ ; break;
        case 5: recurso_5++ ; break;
      }
    }

}// -- class Acciones
