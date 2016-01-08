package framework.trace.InternalFrame.implement;

import javax.swing.JInternalFrame;

public interface IntFrameParent {
  public final static int LOCATION_NORMAL = 0;
  public final static int LOCATION_CENTER = 1;

  public void createInternalFrame(JInternalFrame frame);
  public void createInternalFrame(JInternalFrame frame, int location);
  public void repaintInstance();
  public void destroyInternalFrame(JInternalFrame frame);
}
