package Redes;

import java.io.*;
import java.net.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Client extends JFrame implements ActionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	private JTextArea textObj;
	private JTextField txtMsg;
	private JButton btnSend;
	private JButton btnSair;
	private JLabel lblHistory;
	private JLabel lblMsg;
	private JPanel pnlContent;
	private Socket socket;
	private OutputStream ou;
	private Writer ouw;
	private BufferedWriter bfw;
	private JTextField txtIP;
	private JTextField txtPort;
	private JTextField txtName;
	
	/*
	 * construtor 
	 */
	public Client() throws IOException{
		JLabel lblMessage = new JLabel("Verificar");
		txtIP = new JTextField("127.0.0.1");
		txtPort = new JTextField("12345");
		txtName = new JTextField("Cliente");
		Object[] texts = {lblMessage, txtIP, txtPort, txtName};
		JOptionPane.showMessageDialog(null,  texts);
		 pnlContent = new JPanel();
		 textObj = new JTextArea(10, 20);
		 textObj.setEditable(false);;
		 textObj.setBackground(new Color(240, 240, 240));
		 txtMsg = new JTextField(20);
		 lblHistory = new JLabel("Historico");
		 lblMsg = new JLabel("Mensagem");
		 btnSend = new JButton("Enviar");
		 btnSend.setToolTipText("Enviar Mensagem");
		 btnSair = new JButton("Sair");
		 btnSair.setToolTipText("Sair do Chat");
		 btnSend.addActionListener(this);
		 btnSair.addActionListener(this);
		 btnSend.addKeyListener(this);
		 txtMsg.addKeyListener(this);
		 JScrollPane scroll = new JScrollPane(textObj);
		 textObj.setLineWrap(true);
		 pnlContent.add(lblHistory);
		 pnlContent.add(scroll);
		 pnlContent.add(lblMsg);
		 pnlContent.add(txtMsg);
		 pnlContent.add(btnSair);
		 pnlContent.add(btnSend);
		 pnlContent.setBackground(Color.LIGHT_GRAY);
		 textObj.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
	     txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));                    
	     setTitle(txtName.getText());
	     setContentPane(pnlContent);
	     setLocationRelativeTo(null);
	     setResizable(false);
	     setSize(250,300);
	     setVisible(true);
	     setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	/*
	 * Metodo para conectar
	 */
	public void connect() throws IOException{
		socket = new Socket(txtIP.getText(), Integer.parseInt(txtPort.getText()));
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);
		bfw = new BufferedWriter(ouw);
		bfw.write(txtName.getText() + "\r\n");;
		bfw.flush();
	}
	/*
	 * Metodo de envio de mensagem 
	 */
	public void sendMessage(String msg) throws IOException{
		if(msg.equals("Sair")) {
			bfw.write("Desconectando\r\n");
			textObj.append("Desconectando \r\n");
		}else {
			//bfw.write(msg + "\r\n");
			bfw.write(txtName.getText() + " diz :: " + txtMsg.getText() + "\r\n");
			textObj.append(txtName.getText() + " diz :: " + txtMsg.getText() + "\r\n");
		}
		bfw.flush();
		txtMsg.setText("");
	}
	/*
	 * Método para (escutar) atualizações de mensagens
	 */
	public void listen() throws IOException{
		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader bfr = new BufferedReader(inr);
		String msg = "";

		while(!"Sair".equalsIgnoreCase(msg)) {
			if(bfr.ready()) {
				msg = bfr.readLine();
				System.out.println("essa merda tá ready nessa msg : " + msg);
				if(msg.equals("Sair")) {
					textObj.append("Servidor caiu. \r\n");
				}else {
					textObj.append(msg = "\r\n");
				}
			}
		}
	}
	/*
	 * Desconectar do servidor
	 */
	public void disconnect() throws IOException{
		sendMessage("Sair");
		this.bfw.close();
		this.ouw.close();
		this.ou.close();
		this.socket.close();
		this.dispose();
	}
	
	public static void main(String[] args) throws IOException {
		Client app = new Client();
		app.connect();
		app.listen();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				sendMessage(txtMsg.getText());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if(e.getActionCommand().equals(btnSend.getActionCommand())) {
				sendMessage(txtMsg.getText());
			}else if(e.getActionCommand().equals(btnSair.getActionCommand())){
				disconnect();
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		
	}

}
