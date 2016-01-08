package framework.trace.InternalFrame;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.event.InternalFrameEvent;

import framework.trace.Trace;
import framework.trace.InternalFrame.implement.IntFrameParent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class IntFramePleaseWait extends JInternalFrame implements Runnable{

  private static Thread threadInstance = null;
  private static IntFramePleaseWait instance = null;
  private static IntFrameParent parent = null;
  private static int instanceCount = 0;
  private static int delay = 1000;
  private JLabel jLabel1 = new JLabel();
  private boolean isContinue = true;

  private IntFramePleaseWait() {
    super("Please Wait Window",
      false, //resizable
      false, //closable
      false, //maximizable
      false);//iconifiable
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jLabel1.setText("Please Wait...");
    this.addInternalFrameListener(new IntFramePleaseWait_this_internalFrameAdapter(this));
    this.getContentPane().add(jLabel1, BorderLayout.CENTER);
    instanceCount = 0;
  }

  public synchronized void doStart() {
    isContinue = true;
    instanceCount++;
//    Trace.DEBUG("Window \"IntFramePleaseWait\" is alive");
  }

  public synchronized void doStop() {
//    Trace.DEBUG("Window \"IntFramePleaseWait\" is dead");
    if (--instanceCount==0) {
      isContinue = false;
      destroyInstance();
      destroyThread();
      System.gc();
    }
  }

  public static synchronized IntFramePleaseWait getInstance(IntFrameParent pParent) {
    parent = pParent;
    createInstance();
    createThread();
    return instance;
  }

  protected static void repaintInstance() {
    if (instance.getGraphics()!=null)
      instance.paint(instance.getGraphics());
  }

  protected static void repaintParent() {
    // Reinitialise l'affichage de la fenêtre Principale
    parent.repaintInstance();
  }

  protected static void createInstance() {
    if (instance == null) {
      instance = new IntFramePleaseWait();
      parent.createInternalFrame(instance, IntFrameParent.LOCATION_CENTER);
    }
  }

  protected static void destroyInstance() {
    if (instance != null) {
      instance.dispose();
      // Reinitialise l'affichage de la fenêtre Principale
      repaintParent();
      parent.destroyInternalFrame(instance);
      instance = null;
    }
  }

  protected static void createThread() {
//    destroyThread();
    if (threadInstance==null) {
      threadInstance = new Thread(instance);
//      threadInstance.setPriority(Thread.MIN_PRIORITY);
      threadInstance.start();
    }
  }

  protected static void destroyThread() {
    if (threadInstance!=null) {
      threadInstance.stop();
//      threadInstance.destroy();
      threadInstance = null;
/*
      Thread t = threadInstance;
      threadInstance = null;
      t.stop();
      t = null;
*/
    }
  }

  protected static void sleepThread() {
    if (instance!=null) {
      instance.setTitle("Window \"IntFramePleaseWait\" is sleeping");
      instance.pack();
    }
    try { threadInstance.sleep(delay); }
    catch (InterruptedException ex) { ex.printStackTrace(); }
  }

  public void run() {
    while(isContinue){
      repaintParent();
      //repaintInstance();
      sleepThread();
    }
  }

  protected void this_internalFrameClosing(InternalFrameEvent e) {
    destroyInstance();
    destroyThread();
  }

  public void finalize() throws IOException {
    Trace.DEBUG("IntFramePleaseWait finalize");
  }
}

class IntFramePleaseWait_this_internalFrameAdapter extends javax.swing.event.InternalFrameAdapter {
  IntFramePleaseWait adaptee;

  IntFramePleaseWait_this_internalFrameAdapter(IntFramePleaseWait adaptee) {
    this.adaptee = adaptee;
  }
  public void internalFrameClosing(InternalFrameEvent e) {
    adaptee.this_internalFrameClosing(e);
  }
}
