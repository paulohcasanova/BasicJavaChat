package Redes;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Server extends Thread{
	
	/*
	 * Att basicos e estáticos
	 * clients -> usado para armazenar o BufferedWriter de cada cliente conectado
	 * server -> usado para a criação do servidor
	 */
	private static ArrayList<BufferedWriter> clients;
	private static ServerSocket server;
	private String name;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;
	
	/*
	 * construtor
	 */
	public Server(Socket con) {
		this.con = con;
		try {
			this.in = con.getInputStream();
			this.inr = new InputStreamReader(in);
			this.bfr = new BufferedReader(inr);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Metodo para 
	 * a) adicionar clientes em uma lista para notificar todos conectados das mensagens
	 * b) repassar as mensagens para os clientes conectados
	 */
	@Override
	public void run() {
		try {
			String msg;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			
			clients.add(bfw);
			name = msg = bfr.readLine();
			
			while(!"Sair".equalsIgnoreCase(msg) && msg != null) {
				msg = bfr.readLine();
				sendToAll(bfw, msg);
				System.out.println(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Método para envio
	 */
	public void sendToAll(BufferedWriter bwOut, String msg) throws IOException{
		BufferedWriter bwS;
		for(BufferedWriter bw : clients) {
			bwS = (BufferedWriter) bw;
			if(!(bwOut == bwS)) {
				bw.write(name + " :: " + msg + "\r\n");
				bw.flush();
			}
		}
	}
	/*
	 * main 
	 */
	public static void main(String[] args) {
		try{
		    JLabel lblMessage = new JLabel("Porta do Servidor:");
		    JTextField txtPorta = new JTextField("12345");
		    Object[] texts = {lblMessage, txtPorta };  
		    JOptionPane.showMessageDialog(null, texts);
		    server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
		    clients = new ArrayList<BufferedWriter>();
		    JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText());
		    
		     while(true){
		       System.out.println("Aguardando conexão...");
		       Socket con = server.accept();
		       System.out.println("Cliente conectado...");
		       Thread t = new Server(con);
		        t.start();
		    }
		                              
		  }catch (Exception e) {
		    
		    e.printStackTrace();
		  }

	}

}
