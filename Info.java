/*****************************************************************************\
*      Código java para exibir Recursos do Sistema disponíveis para a JVM     *
\*****************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Info
{   
    private JFrame f1;
    public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    public Runtime t = Runtime.getRuntime();
    public final int X = d.width, Y = d.height;

    public String getInfo()
    {
        StringBuilder r = new StringBuilder();
        r.append("Sistema Operacional: ");
        r.append(System.getProperty("os.name"));
        r.append(", versão ");
        r.append(System.getProperty("os.version"));
        r.append(System.getProperty("line.separator"));
        r.append("Arquitetura: ");
        r.append(System.getProperty("os.arch"));
        r.append(System.getProperty("line.separator"));
        r.append("Usuário logado: ");
        r.append(System.getProperty("user.name"));
        r.append(System.getProperty("line.separator"));
        r.append("Resolução de Tela: ");
        r.append(X);
        r.append(" x ");
        r.append(Y);
        r.append(System.getProperty("line.separator"));
        r.append("JAVA Instalado em: ");
        r.append(System.getProperty("java.home"));
        r.append(System.getProperty("line.separator"));
        r.append("Versão: ");
        r.append(System.getProperty("java.version"));
        r.append(System.getProperty("line.separator"));
        r.append("Local do Programa: ");
        r.append(System.getProperty("user.dir"));
        r.append(System.getProperty("line.separator"));
        r.append("Recursos Disponíveis para o JAVA: ");
        r.append(t.availableProcessors());
        
        if(t.availableProcessors() == 1 ) 	
            r.append(" núcleo de CPU, ");
        else
            r.append(" núcleos de CPU, ");

        r.append(t.maxMemory() / 1048576);
        r.append(" MB de RAM ");
        r.append("(");
        r.append(t.freeMemory() / 1048576);
        r.append(" MB disponíveis)");
        r.append(System.getProperty("line.separator"));
        r.append("A codificação atual é ");
        r.append(System.getProperty("file.encoding"));

        return r.toString();
    }

    public static void main(String[] args)
    {                
        SwingUtilities.invokeLater(() -> { new Info().Mostrar(); });
    }


    public void Mostrar()
    {
        f1 = new JFrame("Informações do Sistema");
        f1.setPreferredSize(new Dimension(X / 3, Y / 4));
        JScrollPane p1 = new JScrollPane();
        JTextArea a1 = new JTextArea();
        a1.setLineWrap(true);
        a1.setWrapStyleWord(true);
        JPanel p2 = new JPanel();
        p2.setMaximumSize(new Dimension(d.width,100));
        JButton b1 = new JButton("Ok");
        p1.setViewportView(a1);
        p2.add(b1);
        Container c1 = f1.getContentPane();
        c1.setLayout(new BoxLayout(c1,BoxLayout.Y_AXIS));
        f1.add(p1); f1.add(p2);
        f1.pack();
        f1.setVisible(true);
        f1.setLocationRelativeTo(null);

        a1.setText(new Info().getInfo());
        a1.setEditable(false);

        b1.addActionListener((ActionEvent e) -> { f1.dispose(); });
    }
    

    public synchronized void exception(Exception e)
    //Janela para exibição dos erros do sistema        
    {
        String exception = e+"\n";
        exception = exception.replaceAll("Exception: ", "Exception:\n");
        // exception = exception.replaceAll("; ", ";\n");
        exception = exception.replaceAll("MySQL ", "MySQL\n");
        JFrame frame = new JFrame("Erro");     
        JTextArea exit = new JTextArea();
        exit.setForeground(Color.RED);
        exit.setBackground(Color.BLACK);
        exit.setCaretColor(Color.RED);
        exit.setSelectedTextColor(Color.RED);
        exit.setSelectionColor(Color.BLACK);
        exit.setWrapStyleWord(true);
        exit.setEditable(true);
        // exit.setPreferredSize(new Dimension(780,596));
        JScrollPane p = new JScrollPane(exit);
        p.setPreferredSize(new Dimension(780,596));
        frame.add(p);
        frame.setPreferredSize(new Dimension(800,600));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        StackTraceElement[] stack;
        stack = e.getStackTrace();
        
        for(int i = 0; i < stack.length; i++)
        {
            exception += "\tat "+
                        stack[i].getClassName()+"."   +
                        stack[i].getMethodName()+"("  +
                        stack[i].getFileName()+":"    +
                        stack[i].getLineNumber()+")";

            if(i < stack.length - 1)
            {
                exception += "\n";
            }
        }    
        exit.setText(exception);    
    }
    

    public synchronized void consoleExit(String command, InputStream is)
    // Janela para exibição de erros em subprocessos invocados pelo sistema
    {
        JFrame frame = new JFrame("Console");     
        JTextArea exit = new JTextArea();
        exit.setForeground(Color.WHITE);
        exit.setBackground(Color.BLACK);
        exit.setCaretColor(Color.WHITE);
        exit.setSelectedTextColor(Color.WHITE);
        exit.setSelectionColor(Color.BLACK);
        exit.setWrapStyleWord(true);
        exit.setEditable(true);
        // exit.setPreferredSize(new Dimension(780,596));
        JScrollPane p = new JScrollPane(exit);
        p.setPreferredSize(new Dimension(780, 596));
        frame.add(p);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);                      
        String line, total = command + "\n";
        
        try
        {
            while((line = br.readLine()) != null) 
            {
                total += line + "\n";
            }
            exit.setText(total);
        }
        catch(IOException io)
        {
            exception(io);
        }
    }
}