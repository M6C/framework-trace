package framework.trace.InternalFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameEvent;

import framework.trace.Trace;
import framework.trace.InternalFrame.implement.IntFrameParentDelay;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class IntFrameTraceDelay extends JInternalFrame implements Runnable{

  private static Thread threadInstance = null;
  private static IntFrameTraceDelay instance = null;
  private static boolean blocked = false;
  private static int delayCurrentUnit = 1000;
  private static int delayCurrent = 0;
  private static int delay = 10;
  private static IntFrameParentDelay parent;

  private JScrollPane jScrollPane1 = new JScrollPane();
  private JTextArea msg = new JTextArea();
  private BorderLayout borderLayout1 = new BorderLayout();
  private JToolBar jToolBar1 = new JToolBar();
  private JCheckBox jCheckBox1 = new JCheckBox((blocked) ? "Unlock" : "Lock",
                                               blocked);

  private IntFrameTraceDelay() {
    super("Trace Execution Window",
      true, //resizable
      true, //closable
      true, //maximizable
      true);//iconifiable
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    msg.setFont(new java.awt.Font("DialogInput", 0, 12));
    msg.setMargin(new Insets(5, 5, 5, 5));
    jScrollPane1.setMaximumSize(new Dimension(455, 125));
    jScrollPane1.setPreferredSize(new Dimension(455, 125));
    this.getContentPane().setLayout(borderLayout1);
//    jCheckBox1.setMaximumSize(new Dimension(15, 15));
//    jCheckBox1.setMinimumSize(new Dimension(15, 15));
//    jCheckBox1.setPreferredSize(new Dimension(15, 15));
    jCheckBox1.setMargin(new Insets(0, 0, 0, 0));
    jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jCheckBox1_itemStateChanged(e);
      }
    });
    this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
      public void internalFrameClosing(InternalFrameEvent e) {
        this_internalFrameClosing(e);
      }
    });
    this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
    this.getContentPane().add(jToolBar1, BorderLayout.NORTH);
    jToolBar1.add(jCheckBox1, null);
    jScrollPane1.getViewport().add(msg, null);
  }

  public static synchronized IntFrameTraceDelay getInstance(IntFrameParentDelay pParent) {
    parent = pParent;
    if(parent!=null)
      delay = parent.getDelay();
    createInstance();
    createThread();
    return instance;
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int aDelay) {
    delay = aDelay;
  }

  public synchronized void INFO(String text) {
    OUT(text);
    Trace.DEBUG(text);
  }

  public synchronized void WARNING(String text) {
    OUT(text);
    Trace.WARNING(text);
  }

  public synchronized void ERROR(String text) {
    OUT(text);
    Trace.ERROR(text);
  }

  protected void OUT(String text) {
    delayCurrent = (blocked) ? 1 : delay/delayCurrentUnit;
    msg.append(text);
    // Reinitialise l'affichage de la fenêtre Trace
    repaintInstance();
    // Reinitialise l'affichage de la fenêtre Principale
    if(parent!=null){
      parent.repaintInstance();
    }
    msg.setCaretPosition(msg.getText().length());
  }

  protected static void repaintInstance() {
    if ((instance!=null)&&(instance.getGraphics()!=null))
      instance.paint(instance.getGraphics());
  }

  protected static void createInstance() {
    if (instance == null) {
      instance = new IntFrameTraceDelay();
      if (parent!=null)
        parent.createInternalFrame(instance);
      instance.pack();
      instance.show();
    }
  }

  protected static void destroyInstance() {
    if (instance != null) {
      instance.dispose();
      // Reinitialise l'affichage de la fenêtre Principale
      if (parent!=null){
        parent.repaintInstance();
        parent.destroyInternalFrame(instance);
      }
      instance = null;
    }
  }

  protected static void createThread() {
//    destroyThread();
    if (threadInstance==null) {
      threadInstance = new Thread(instance);
      threadInstance.setPriority(Thread.MIN_PRIORITY);
      threadInstance.start();
    }
  }

  protected static void destroyThread() {
    if (threadInstance!=null) {
		threadInstance.stop();
//      threadInstance.destroy();
      threadInstance = null;
    }
  }

  protected static void sleepThread() {
//	try { threadInstance.sleep(delayCurrentUnit); }
	try { Thread.sleep(delayCurrentUnit); }
    catch (InterruptedException ex) { ex.printStackTrace(); }
  }

  public void run() {
    while(delayCurrent>0){
      // Test si la coche de bloquage de la fenetre est cochée
      if (!blocked)
        setTitle("Trace Execution Window will be killed (in "+(delayCurrent)+" seconde"+((delayCurrent--)>1 ? "s" : "")+")");
      else
        setTitle("Trace Execution Window will never be killed");
      sleepThread();
    }
    destroyInstance();
    destroyThread();
    System.gc();
  }

  protected void jCheckBox1_itemStateChanged(ItemEvent e) {
    blocked = (e.getStateChange()==ItemEvent.SELECTED);
    jCheckBox1.setText((blocked) ? "Unlock" : "Lock");
  }

  protected void this_internalFrameClosing(InternalFrameEvent e) {
    destroyInstance();
    destroyThread();
    System.gc();
  }

  public void finalize() throws IOException {
    Trace.DEBUG("TraceFramse finalize");
  }
}
