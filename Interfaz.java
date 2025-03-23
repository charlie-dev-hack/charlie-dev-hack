import java.util.Vector;
import java.util.NoSuchElementException;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.awt.Color;
import java.awt.geom.*;
import java.awt.image.*;

public class Interfaz extends JFrame implements ActionListener {
    Acciones cpu; //obtenemos una copia del objeto/clase Acciones    
    Memoria mapa;

    JButton b_inicia, b_buscar, b_agregar;  //botones

    //usamos un menos para otras opciones de usuario
    JMenuBar el_menu;
    JMenu menu_arch, menu_metodo, menu_bloqueo;
    JMenuItem b_nuevo, b_abrir, b_salir, b_guardar;
    JRadioButtonMenuItem  r_fifo,r_RR, r_aleat, r_banquero;

    JFileChooser openFileDialog;//cuando abramos de archivo
    String fileName="";//string para guardar la ruta del archivo

    //algunos labels
    JLabel t_status, t_status2, t_status3, t_status4;
    JLabel status, status2, status3, status4;
    JLabel ingresa_id_l;
    JLabel in_datos;  
    JLabel tiempo, ti, despacha, despacha_1, block, block_1;
    JLabel r1, r2, r3, r4, r5, r_1, r_2, r_3, r_4, r_5;
    
    //areas ed ingreso de datos
    JTextArea in_datos_ta;
    JTextField ingresa_id;

    //areas de despliegue de datos
    JTextArea procesos;

    //algunos paneles
    JPanel encolado;
    JPanel ingresa_1;
    JPanel ingresa_2;
    JPanel cpu_count;
    JPanel info_general;
    JPanel izq;
    JPanel der;
    
    //usamos el objeto timer de java.awt para llevar un conteo de tiempo

    Timer timer;
    
    boolean pausado = true; //al principio todo esta detenido

/**************************************************************************************
***************************************************************************************
    
                  INTERFAZ
                  
***************************************************************************************
***************************************************************************************/

    public Interfaz(String st){

	//establece simulacion, envia el argumento FIFO/RR a la instancia
	//para establecer tipo de estrategia de CPU utilizada
	cpu = new Acciones(st);

        //este delay sirve para ajustar la velocidad de la animacion
        //y se agrega al timer, la ventaja es que usa threads
        // ver http://java.sun.com/j2se/1.4.2/docs/api/java/util/Timer.html
 	int delay = 100; //en milisegundos
        timer = new Timer(delay,this);

	setTitle("Simulacion de Scheduler / Sistemas Operativos");
        setSize(1010,600); //width, height  -- tam de interfaz

        //labels
        t_status = new JLabel("Estado");
        t_status2 = new JLabel("Temp. Rest.");
        t_status3 = new JLabel("Identificador");
        t_status4 = new JLabel("Nombre");
        status = new JLabel("   ----   ");
        status2 = new JLabel("   ----   ");
        status3 = new JLabel("   ----   ");
        status4 = new JLabel("   ----   ");
        ingresa_id_l = new JLabel("Ingresa ID:");
        in_datos = new JLabel("Formato Establecido");
       	tiempo =  new JLabel("Tiempo:  ");
	ti = new JLabel(""+0); //inicia en cero
	despacha = new JLabel ("Despacha: ");
	despacha_1 = new JLabel("");
	block = new JLabel ("Bloqueo: ");
	block_1 = new JLabel("");
	
	r1 = new JLabel ("R1: ");
	r2 = new JLabel ("R2: ");
	r3 = new JLabel ("R3: ");
	r4 = new JLabel ("R4: ");
	r5 = new JLabel ("R5: ");

	r_1 = new JLabel ("");
	r_2 = new JLabel ("");
	r_3 = new JLabel ("");
	r_4 = new JLabel ("");
	r_5 = new JLabel ("");
	
	r1.setVisible(false);
        r2.setVisible(false);
        r3.setVisible(false);
        r4.setVisible(false);
        r5.setVisible(false);
	r_1.setVisible(false);
        r_2.setVisible(false);
        r_3.setVisible(false);
        r_4.setVisible(false);
        r_5.setVisible(false);

        //areas de ingreso de datos
        ingresa_id = new JTextField();
        in_datos_ta = new JTextArea();
        JScrollPane scroll = new JScrollPane(in_datos_ta);
        scroll.setPreferredSize(new Dimension(100,100));

        //inicializamos los botones
	b_inicia = new JButton(" \n Inicia / Pausa \n ");

	b_inicia.addActionListener(this);
	b_inicia.setToolTipText("Play/Pausar");    //texto explicativo
        b_inicia.setAlignmentX(Component.LEFT_ALIGNMENT);

	b_buscar = new JButton("Buscar");
	b_buscar.addActionListener(this);
	b_buscar.setToolTipText("Busca datos del id ingresado");

	b_agregar = new JButton("Agregar");
	b_agregar.addActionListener(this);
	b_agregar.setToolTipText("Agrega proceso.");

        Logo comp = new Logo("compu.gif");

	//paneles
        ingresa_1 = new JPanel();
        TitledBorder tBorder1 =  BorderFactory.createTitledBorder("Datos de Proceso");
	ingresa_1.setBorder( tBorder1);
	ingresa_1.setLayout(new GridLayout(3,4));//rows x columns
	ingresa_1.add(t_status);
        ingresa_1.add(t_status2);
        ingresa_1.add(t_status3);
        ingresa_1.add(t_status4);
        ingresa_1.add(status);
        ingresa_1.add(status2);
        ingresa_1.add(status3);
        ingresa_1.add(status4);
        ingresa_1.add(ingresa_id_l);
        ingresa_1.add(ingresa_id);
        ingresa_1.add(b_buscar);
        ingresa_1.add(comp);
        
        encolado = new JPanel();  //los procesos
        
        ingresa_2 = new JPanel();
        TitledBorder tBorder2 =  BorderFactory.createTitledBorder("Ingresa Proceso");
	ingresa_2.setBorder( tBorder2);
	ingresa_2.setLayout(new BoxLayout(ingresa_2,BoxLayout.Y_AXIS));//rows x columns
	ingresa_2.add(in_datos);
        ingresa_2.add(scroll);
        ingresa_2.add(b_agregar);
        

	cpu_count = new JPanel();
	TitledBorder tBorder =  BorderFactory.createTitledBorder("Process Log");
	cpu_count.setBorder(tBorder);
	cpu_count.setLayout(new GridLayout(8,8));//rows x columns
	cpu_count.add(tiempo);
	cpu_count.add(ti);
	cpu_count.add(despacha);
        cpu_count.add(despacha_1);
        cpu_count.add(block);
        cpu_count.add(block_1);
        
        cpu_count.add(r1);
        cpu_count.add(r_1);
        cpu_count.add(r2);
        cpu_count.add(r_2);
        cpu_count.add(r3);
        cpu_count.add(r_3);
        cpu_count.add(r4);
        cpu_count.add(r_4);
        cpu_count.add(r5);
        cpu_count.add(r_5);

        //construimos los menus
	el_menu = new JMenuBar(); //barra principal
	setJMenuBar(el_menu);

	// menu constructor
	menu_arch = new JMenu("Archivo");

	b_nuevo = new JMenuItem("Procesos Aleatorios");
	b_nuevo.addActionListener(this);
	menu_arch.add(b_nuevo);

	b_abrir = new JMenuItem("Abrir de Archivo...");
	b_abrir.addActionListener(this);
	menu_arch.add(b_abrir);

	b_salir  = new JMenuItem("Salir");
	b_salir.addActionListener(this);
	menu_arch.add(b_salir);
	
	b_guardar  = new JMenuItem("Guardar Simulacion");
	b_guardar.addActionListener(this);
	menu_arch.add(b_guardar);

	el_menu.add(menu_arch);

	menu_metodo = new JMenu("Metodo");
	menu_bloqueo = new JMenu("Bloqueo");

	ButtonGroup algogroup = new ButtonGroup();
	r_fifo = new JRadioButtonMenuItem("FIFO");
	r_fifo.setSelected(true);
	r_fifo.setToolTipText("Utiliza FIFO");
	algogroup.add(r_fifo);
	r_fifo.addActionListener(this);
	menu_metodo.add(r_fifo);
	
	//boton de bloqueo
	ButtonGroup algogroup2 = new ButtonGroup();
	r_aleat = new JRadioButtonMenuItem("Aleatorio");
	r_aleat.setSelected(true);
	r_aleat.setToolTipText("Utiliza bloqueo aleatorio");
	algogroup2.add(r_aleat);
	r_aleat.addActionListener(this);
	menu_bloqueo.add(r_aleat);


	r_RR = new JRadioButtonMenuItem("Round Robin");
	r_RR.setToolTipText("Utiliza Round Robin");
	algogroup.add(r_RR);
	r_RR.addActionListener(this);
	menu_metodo.add(r_RR);
	
	//otro radio_button
	r_banquero = new JRadioButtonMenuItem("Banquero");
	r_banquero.setToolTipText("Utiliza algoritmo de Banquero");
	algogroup2.add(r_banquero);
	r_banquero.addActionListener(this);
	menu_bloqueo.add(r_banquero);

        el_menu.add(menu_metodo);
  	el_menu.add(menu_bloqueo);
  	

        buildFileDialog(); //para cargar archivos
        llenar_cola_listos();
	actualiza_valores();

	Container principal = getContentPane() ;
        principal.setLayout(new GridLayout(1,0));
	
	JPanel izq = new JPanel();
	izq.setLayout(new BorderLayout());
	izq.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	
	JPanel der = new JPanel();
	der.setLayout(new BorderLayout());
	der.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

	JPanel arriba = new JPanel();
	arriba.setLayout(new BorderLayout());
	arriba.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	arriba.add(encolado,"North");  //procesos
	arriba.add(ingresa_1,"South"); //busca de proceso

	JPanel en_medio = new JPanel();
	en_medio.setLayout(new BorderLayout());
	en_medio.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	en_medio.add(cpu_count, "North"); //contador


        //---------------------------------------
        //           parte grafica
        //---------------------------------------
         Logo log = new Logo("guindous.gif");
         Logo log2 = new Logo("histo.gif");
         mapa = new Memoria(cpu.getProcesos());
        //---------------------------------------
        //---------------------------------------

      
        en_medio.add(b_inicia, "South");  //boton inicio animacion

	JPanel abajo = new JPanel();
	abajo.setLayout(new GridLayout(0,2));
	abajo.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	abajo.add(ingresa_2,"West");  //ingresar datos
        abajo.add(en_medio,"East");   //boton

        Color col2 = new Color(0,255,0);
        Color col3 = new Color(0,0,0);
        //color de TextArea
        procesos = new JTextArea();
        procesos.setEditable(false);
        procesos.setBackground(col3);
        procesos.setForeground(col2);
        Font fuente = new Font("Dialog", 1, 12);
        procesos.setFont(fuente);
        
        procesos.setText("  \n  Loading... ");
        JScrollPane scroll2 = new JScrollPane(procesos);
        scroll2.setPreferredSize(new Dimension(80,300));
 
        JPanel info_general = new JPanel();
        info_general.setLayout(new BoxLayout(info_general, BoxLayout.Y_AXIS));
	info_general.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	info_general.add(log);
	info_general.add(scroll2);  //estado procesos

        izq.add(arriba, "North");
        izq.add(log2); //histograma

	izq.add(abajo,  "South");

	der.add(mapa);
	
	principal.add(izq);
	principal.add(info_general);
        principal.add(der);

	addWindowListener(
			  new WindowAdapter() {
				  public void windowClosing(WindowEvent e) { 
				      System.exit(0); 
				  } 
			      } 
			  );
	setVisible(true);
    }


/**************************************************************************************
***************************************************************************************

                  METODOS DE CLASE

***************************************************************************************
***************************************************************************************/

    //pone los procesos en una cola
    public void llenar_cola_listos(){


	Vector v = cpu.getProcesos();
	Color col2 = new Color (238,238,238); //color de fondo tipo gris-windows

 	encolado.setBackground(col2);
 	encolado.setOpaque(true);
	encolado.setSize(150,350); 
	encolado.setPreferredSize(new Dimension(650,150));
	FlowLayout f1 = new FlowLayout(FlowLayout.LEFT);
	encolado.setLayout(f1);
	
	//va poniendo los procesos disponibles
	for( int i = 0; i < v.size() ; i++){
            //antes era un nodo pero le damos un cast
	    PanelProceso p = new PanelProceso( (Proceso) v.get(i) );
	    encolado.add(p,"Center");  //los vamos agregando

	}

    }

    //despliega en el textArea la informacion de todos los procesos activos
    public void desplegar_procesos(){

        Vector y = cpu.getProcesos();

        String texto =  "  C:> Simulados Scheduler  \n\n" +
                        "  Sistemas Operativos              \n" +
                        "\n\n" +
                        "  ----------------------------------------\n" +
                        "  ----------------------------------------\n" +
                        "  -- Procesos --\n" +
                        "____________________________________________\n" +
                        "  ID     Nombre    Memoria         T-Rest       Estado        \n" +
                        "____________________________________________\n";
        
        for( int i = 0; i < y.size() ; i++){

             Proceso pro = (Proceso) y.get(i);
             
             if (pro.isDone() == false){

                texto = texto + "  " +
                //caracteristicas por agregar
                pro.getPID() +  "             "  +
                pro.getNombre() + "              "  +
                pro.getMemoria() + "KB" + "          " +
                pro.getTRestante() + "           " +
                pro.getEstado()  + "\n";
             } else {
               //no imprime su estado para evitar tener en 'memoria' esta parte
             }
        }
        texto = texto + "____________________________________________\n";
        procesos.setText(texto);
    }
    
    //quita la representacion visual de cola de listos
    public void vaciar_cola_listos(){
	encolado.removeAll();
    }


   //este metodo busca la instancia correspondiente al id ingresado
   //y devuelve sus valores
    public void estado_proceso(){

       Vector v = cpu.getProcesos(); //obtenemos el vector con TODOS los procesos
       long compara = Integer.parseInt(ingresa_id.getText());
       //si el id ingresado coincide con alguno del vector, entonces obtenemos ese nodo
       Proceso p = new Proceso();
       int i;
       for( i = 0; i < v.size() ; i++){
            p = (Proceso) v.get(i);  //le damos un cast para que el nodo sea proceso
            long id = p.getPID();
            if (id == compara){
              break;
            }
	}
       //si se llega al fin del vector y no hay resultado
       if (i == v.size()){
          status.setText("ID invalido");
          status2.setText("");
          status3.setText("");
          status4.setText("");
          
       }else{
          //desplegamos los valores que nos piden
          status2.setText("" + p.getTRestante());
          status3.setText("" + p.getPID());
          status4.setText("" + p.getNombre());
          //el estado del proceso es un poco mas complicado porque tiene 3 estados
          //corriendo, listo, bloqueado y le agregamos terminado

          status.setText(p.getEstado());
       }
    }


  //administrador de eventos, de desarrollo grafico
    public void actionPerformed(ActionEvent e){
        //inicia la animacion y el contador
        if( e.getSource() == b_inicia ){

            if(pausado == false){
                pausado = true;
                stop_func();
                b_inicia.setSelected(false);

                ingresa_id.setEditable(true);
                in_datos_ta.setVisible(true);

            }
            else{
                pausado = false;
                startAnimation();
                b_inicia.setSelected(true);
                //en caso de que queramos desactivas la visibilidad de los
                //botones durante la ejecucion (inicialmente en false)
                ingresa_id.setEditable(true);
                in_datos_ta.setVisible(true);
            }

        }

        //buscamos el status del proceso
        else if( e.getSource() == b_buscar){
            try {
                estado_proceso();
                //cuando no tiene dato a buscar y se da clic se genera una excepcion inofensiva
                //simplemente la ignoramos
            }catch(NumberFormatException nfe) {
              status.setText("ID invalido");
            }catch(NoSuchElementException abc){}
        }

        //agregamos los procesos desde la interfaz
        else if (e.getSource() == b_agregar) {
            try {
                cpu.agrega_proceso(in_datos_ta.getText());
                vaciar_cola_listos(); // borra los procesos
                llenar_cola_listos(); // los vuelve a agregar
                mapa.actualiza(cpu.getProcesos());
                actualiza_valores();
                repaint();
            } catch (IllegalArgumentException ex) {
                in_datos_ta.setText("Error en el formato: " + ex.getMessage());
            }
        }

        //inicia el contador.
        //Esta funcion es como si fuera un boton, solo que se valida cada ciclo
        else if( e.getSource() == timer ){
            cpu.sigCicloEjec();
            desplegar_procesos();
            actualiza_valores();
            repaint();
        }

        //Ejecutamos FIFO
        else if( e.getSource() == r_fifo){
            cpu.setAlgorithm(Acciones.FIFO);

        }
        //Para ejecutar Round-Robin
        else if( e.getSource() == r_RR){
            cpu.setAlgorithm(Acciones.ROUNDROBIN);

        }
        //Ejecutamos bloqueo aleatorio
        else if( e.getSource() == r_aleat){
             cpu.setBloqueo(Acciones.bloqueo_aleatorio);
             r_1.setVisible(false);
             r_2.setVisible(false);
             r_3.setVisible(false);
             r_4.setVisible(false);
             r_5.setVisible(false);

             r1.setVisible(false);
             r2.setVisible(false);
             r3.setVisible(false);
             r4.setVisible(false);
             r5.setVisible(false);

        }
        //Ejecutamos bloqueo con algoritmo de banquero
        else if( e.getSource() == r_banquero){
             cpu.setBloqueo(Acciones.bloqueo_banquero);
             r_1.setVisible(true);
             r_2.setVisible(true);
             r_3.setVisible(true);
             r_4.setVisible(true);
             r_5.setVisible(true);
             r1.setVisible(true);
             r2.setVisible(true);
             r3.setVisible(true);
             r4.setVisible(true);
             r5.setVisible(true);

        }

        //comando para ingresar valores aleatorios
        else if( e.getSource() == b_nuevo){
            int algo = cpu.getAlgorithm();
            int blo = cpu.getBloqueo();
            cpu = new Acciones(); //constructor para hacer pruebas
            cpu.setAlgorithm(algo);
            cpu.setBloqueo(blo);
            vaciar_cola_listos();
            llenar_cola_listos();
            mapa.actualiza(cpu.getProcesos());
            actualiza_valores();
            repaint();
        }

        //comando si queremos abrir desde archivo
        else if( e.getSource() == b_abrir){

            int returnVal = openFileDialog.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) { 
                File fileName=openFileDialog.getSelectedFile();
                cpu = new Acciones(fileName);
                vaciar_cola_listos();
                llenar_cola_listos();
                mapa.actualiza(cpu.getProcesos());
                actualiza_valores();
                repaint();
            } 
        }

        // Guardar la simulación en un archivo
        else if( e.getSource() == b_guardar){
            guardarSimulacion();
        }

        //para salirnos de la simulacion
        else if( e.getSource() == b_salir){
            //stop_func();
            dispose();
            System.out.println("El programa ha terminado exitosamente.");
            System.exit(0);
        }

    }

    //para comenzar animacion, esta sincronizado con el boton de inicio
   

    //para pausar animacion, esta sincronizado timer - b_inicia
    public synchronized void stop_func() {

        if (timer.isRunning()) {
            timer.stop();
        }
    }

    // Método para guardar la simulación en un archivo
    public void guardarSimulacion() {
        try {
        	SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd_HHmmss");
        	Date fecha = new Date();
        	String fechaActual = formato.format(fecha);

            // Crear el nombre del archivo
            String fileName = "SimProceso_" + fechaActual + ".txt";

            // Crear el archivo y el escritor de archivos
            File file = new File(fileName);
            FileWriter writer = new FileWriter(file);

            // Escribir el contenido del archivo
            writer.write(cpu.getProcesos().size() + "\n");
            Vector<Proceso> procesos = cpu.getProcesos();
            for (Proceso proceso : procesos) {
                writer.write( "Proceso "+proceso.getNombre() + ", " + proceso.getTInicial() + "\n");
            }

            // Cerrar el escritor de archivos
            writer.close();

            // Mostrar mensaje de éxito
            System.out.println("Simulación guardada correctamente en: " + fileName);
        } catch (IOException e) {
            System.out.println("Error al guardar la simulación: " + e.getMessage());
        }
    }

    //para comenzar animacion, esta sincronizado con el boton de inicio
    public synchronized void startAnimation() {
        if (pausado) {

        } else {

            if (!timer.isRunning()) {
                timer.start();
            }
        }
    }
    

    

    //actualiza display de conteo
    public void actualiza_valores(){
	ti.setText(Integer.toString((int)cpu.getTotalTime()));
	r_1.setText(Integer.toString((int)cpu.getRecurso(1)));
	r_2.setText(Integer.toString((int)cpu.getRecurso(2)));
	r_3.setText(Integer.toString((int)cpu.getRecurso(3)));
	r_4.setText(Integer.toString((int)cpu.getRecurso(4)));
	r_5.setText(Integer.toString((int)cpu.getRecurso(5)));
	//para saber que algoritmo de dispatch utiliza
	if (cpu.getAlgorithm() == 1)
	   despacha_1.setText("FIFO");
        else
           despacha_1.setText("R-Robin");
        //para saber que algoritmo de bloqueo utiliza
        if (cpu.getBloqueo() == 3)
	   block_1.setText("Aleatorio");
        else
           block_1.setText("Banquero");
           
    }

    //para seleccionar archivo
    void buildFileDialog(){
	openFileDialog = new JFileChooser("."); // a partir de esta localidad
	
    }

} // --Interfaz

class PanelProceso extends JPanel{

    //creamos de procesos de la Clase Proceso
    Proceso proc;

    //valores de tamano de las barritas
    static final int grosor_barra  = 30; //grosor de barra de procesos
    static final int pos_id_y = 120; //posicion de los identificadores en y
    static final int altura_barra = 120; //posicion de las barras en y

    //colores que usaremos
    Color segmento;
    JLabel identificador;
    JLabel porcentaje;


    //constructor default que genera un proceso
    PanelProceso(){
	proc = new Proceso();
	initPanel();

    }
    //colocar el proceso en el panel
    PanelProceso( Proceso p){
	proc = p;
	initPanel();
    }


    //construir el panel  de los procesos a entrar
    void initPanel(){
	setAlignmentX(Component.LEFT_ALIGNMENT);
        setLayout(new BorderLayout());

	identificador = new JLabel("p"+ (int)proc.getPID());
	porcentaje = new JLabel("");

        identificador.setHorizontalAlignment(SwingConstants.CENTER);
        porcentaje.setHorizontalAlignment(SwingConstants.CENTER);

	setSize(grosor_barra,pos_id_y);
        add(porcentaje, "South");
	add(identificador,"North");
    }

    //cuando el proceso termine, que lo quite si no, que actualice el avance
    public void paintComponent(Graphics g){
	super.paintComponent(g);

	if ( proc.isDone() == true){
	    setVisible(false); //lo ocultamos cuando termine
	}
	else {
	    dibuja_segmento(g); 

	}
    }

    //dibuja el avance con color de llenado, el proceso activo tiene otro color
    void dibuja_segmento(Graphics g){

	int alt_inicial=0;
        int completado=0;
	int gro=0;
	int porciento = 0;

        gro  = (int) grosor_barra-2; 
	alt_inicial = (int) proc.getTInicial();
	completado = (int) proc.getTRestante();

	porciento =  (completado * 100)/ alt_inicial ;


        //cuando esta bloqueado se pone rojo
        //cuando no esta activo pone un color verdoso obscuro
        //cuando esta activo lo pone verde claro
        if (proc.isBlocked() == true)
           segmento = new Color (180, 10, 10);
        else if (proc.isRunning() == true)
           segmento = new Color (0, 134, 74);
        else
           segmento = new Color (51,102,102);

	identificador.setForeground( new Color (51,102,102) ); //color del label
	porcentaje.setForeground( new Color (255, 255, 0) );
	porcentaje.setText( "" + porciento + "%");
	
            //contorno de barrita
	    g.setColor(new Color(190,190,190));
	    g.fillRect(0,altura_barra - alt_inicial,gro,alt_inicial);
	    //relleno
	    g.setColor(segmento);
	    g.fillRect(0,altura_barra - completado,gro,completado);
   }

    public Dimension getPreferredSize(){
	return ( new Dimension(30,pos_id_y));

    }
} // --  PanelProceso

//clase logo (de una direccion de url obtiene una imagen)
class Logo extends JPanel{

    String ruta = "";

    Logo(String r){
       ruta = r;
    }

    public void paintComponent(Graphics g){
	super.paintComponent(g);

	    dibuja(g);
    }

    void dibuja(Graphics g){

                 //creamos la imagen
                 Image icon;
                 Toolkit T = this.getToolkit();
                 icon = T.getImage(ruta);
                 g.drawImage(icon, 3, 3, this);


   }
} // --  Logo

//clase logo (de una direccion de url obtiene una imagen)
class Memoria extends JPanel{
    int verde = 0;
    int rojo = 150;
    int azul = 0;

    Vector v = null;
    int desplazamientoX = 0;
    int desplazamientoY = 0;
    int centinela = 0;
    int id_actual = 0;
    
    int activeIndex= -1;
    
    int tamanoPagina = 64;
    int counter = tamanoPagina;
    
    Color llenador = new Color(180,180,180);
    boolean original = true;
    
    Color llenador2;


    //constructor default
    Memoria(){
       //nada hasta el momento
    }
    //leemos el vector
    Memoria( Vector vec){
       v = vec;

    }

    ///////////////////////////////////////////////////////////////////
    //////////////////////// Componente Grafico ///////////////////////
    
        boolean pagina[] = {false, false, false, false, false};
        boolean doble[] = {false, false, false, false, false};
        int inicio[] = {0,80,160,240,320};
        int llenado[] = {0,0,0,0,0};
        Color colores[] = {new Color(0,0,0),new Color(100,0,0),new Color(0,0,0),new Color(100,0,0),new Color(0,0,0)};

        
    ///////////////////////////////////////////////////////////////////

    public void paintComponent(Graphics g){
	super.paintComponent(g);
	
         dibujaGrid(g);//dibujamos el grid
         dibujaProcesos(g);//despues dibujamos los procesos uno por uno
    }

   //se utiliza tecnica de paginacion
   void dibujaProcesos(Graphics g){

       Proceso p = null;

       for (int i=0; i< v.size(); i++){
         p = (Proceso) v.get(i);

         if (p.isRunning() == true){
           asignaEspacio(p.getMemoria(), p.getPID(), g);
         }//if
         
       }//for
   }//dibujaProceso
   
   
   void asignaEspacio(int espacioMem, int identidad, Graphics g){
      if (identidad == id_actual){

        for (int i = 0; i < 5; i++){

             dibujaEspacio(inicio[i],llenado[i], colores[i], identidad, espacioMem, inicio[activeIndex], g);
          
        }

      //si la identidad cambia y tambien inicio de programa
      } else {

        id_actual = identidad;
        activeIndex++;
        
        if (activeIndex >= 5)
           activeIndex = -0;
        
        if (espacioMem < 64){

          cambiaColor2();
          colores[centinela] = llenador2;
          pagina[centinela] = true;
          llenado[centinela] = espacioMem;
          centinela++;
          
          if (centinela >= 5)
             centinela = 0;

            if (doble[centinela] == true){

               doble[centinela] = false;
               llenado[centinela] = 0;

             }
        } else {

           cambiaColor2();
           colores[centinela] = llenador2;
           pagina[centinela] = true;
           llenado[centinela] = tamanoPagina;
           centinela++;
           
            if (centinela >= 5)
             centinela = 0;
           
           colores[centinela] = llenador2;
           pagina[centinela] = true;
           llenado[centinela] = espacioMem - 64;
           doble[centinela] = true;
           centinela++;
           
            if (centinela >= 5)
              centinela = 0;
              
        }
        
    }//else
   }//void
   
   
   
   void dibujaEspacio(int inicio, int largo, Color c, int processID, int espacio, int comodin, Graphics g){

        int dX = 0;
        int dY = inicio;
        int finito = comodin + espacio;

        for (int i = 0; i < largo; i++){

         g.setColor(c);
         g.fillRect(dX, dY,20,20);
         g.setColor(new Color(0,0,0));
         g.drawRect(dX, dY,20,20);
         dX = dX + 20;

         //vuelta de carro
	    if (dX > 300){
	       dX = 0;
               dY = dY + 20;
           }
         }

        String el_texto1 = "Proceso ejecutandose: ID-" + processID;
        String el_texto2 = "Espacio Requerido: " +  espacio;
        String el_texto3 = "Direccion de Memoria Inicial: (0," +  comodin + ")";        
        String el_texto4 = "Direccion de Memoria Final: " +  finito;
        
        g.drawString(el_texto1, 60,450);
        g.drawString(el_texto2, 60,465);
        g.drawString(el_texto3, 60,480);
        g.drawString(el_texto4, 60,495);
   }


   void dibujaGrid(Graphics g){

      counter = tamanoPagina;
      int dX = 0;
      int dY = 0;

      //primero dibujamos el grid
       for (int i = 0; i < 320 ; i++){
            //llenamos la pagina

            if (counter == 0){
               counter = tamanoPagina;
               cambiaColor();
            }
            g.setColor(llenador);
            g.fillRect(dX, dY, 20,20);
            counter--;
            //----------------------
            
            //el simpel cuadrito
            g.setColor(new Color(0,0,0));
            g.drawRect(dX, dY,20,20);
	    dX = dX + 20;

	    if (dX > 300){
	       dX = 0;
               dY = dY + 20;
           }


       }//for


   }
   
   void cambiaColor(){

       if (original == true){
          llenador = new Color (160,160,160);
          original = false;
       } else {
         llenador = new Color (180,180,180);
         original = true;
       }

   }
   
   void cambiaColor2(){
         llenador2 = new Color(rojo,verde,azul);
         
         if (rojo == 150){
           verde = 150;
           rojo = 0;
           azul = 0;
         }

         else if (verde == 150){
           verde = 0;
           rojo = 0;
           azul = 150;
         }
         
         else if (azul == 150){
           verde = 0;
           rojo = 150;
           azul = 0;
         }
   }
   
  public void actualiza(Vector v2){
    v = v2;
  }

} // --  Memoria
