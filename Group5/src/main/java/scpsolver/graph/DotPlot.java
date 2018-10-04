package scpsolver.graph;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;
import javax.swing.JPanel;

import scpsolver.util.NonZeroElementIterator;
import scpsolver.util.SparseMatrix;

public class DotPlot {

	
	public void dotPlot(SparseMatrix sm) {
		/*int rows = sm.getRowNum();
		int cols = sm.getColNum();
		BufferedImage image = new BufferedImage(rows,cols,BufferedImage.TYPE_BYTE_BINARY);
		WritableRaster wr = image.getRaster();
		NonZeroElementIterator nze = sm.getNonZeroElementIterator();
		while (nze.hasNext()) {
			Double double1 = (Double) nze.next();
			wr.setSample(nze.getActuali() , nze.getActualj(), 0, 1);
		}*/
		JFrame jframe = new JFrame("Dot Plot");
		jframe.setSize(500,500);
		//jframe.add(new DrawingPanel(image));
		jframe.add(new SparseDrawingPanel(sm));
		jframe.setVisible(true);
	}
	
	class DrawingPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Image img;
		
		public DrawingPanel(Image img) {
			this.img = img;
		}
		
	   public void paintComponent (Graphics g) {
			   super.paintComponent (g);

			   // Use the image width & height to find the starting point
			/*   int imgX = getSize ().width/2 - img.getWidth (this);
			   int imgY = getSize ().height/2 - img.getHeight (this);
			 */
			   //Draw image centered in the middle of the panel    
			   g.drawImage (img, 0, 0, getSize().width, getSize().height, this);
	    } // paintComponent

	}
	
	class SparseDrawingPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -9144254916888457063L;
		SparseMatrix m;
		
		public SparseDrawingPanel(SparseMatrix m) {
			this.m = m;
		}
		
	   public void paintComponent (Graphics g) {
			   super.paintComponent (g);
			   int rows = m.getRowNum();
			   int cols = m.getColNum();
			   int w = getSize().width;
			   int h = getSize().height;
			   BufferedImage img = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
			   int wscale = (cols > w)? cols/w +1:1;
			   int hscale = (rows > h)? rows/h +1:1;
		//	   System.out.println(rows + " " +  cols);  
		//	   System.out.println(wscale + " " +  hscale);
			   byte[][] scaled = new byte[h][w];
			   NonZeroElementIterator nze = m.getNonZeroElementIterator();
			   while (nze.hasNext()) {
					nze.next();
			//		System.out.println((int) nze.getActuali()/hscale + " " + (int) nze.getActualj()/wscale);
					try {
						scaled[(int) nze.getActuali()/hscale ][(int) nze.getActualj()/wscale ] = (byte) 255;
					} catch (ArrayIndexOutOfBoundsException e) {
					//	System.out.println((int) nze.getActuali()/hscale + " " + (int) nze.getActualj()/wscale);
						
						// TODO: handle exception
					}
					
			   }
			   WritableRaster wr = img.getRaster();
			   for (int i = 0; i < scaled.length; i++) {
				for (int j = 0; j < scaled[i].length; j++) {
				   wr.setSample(j, i, 0, 255-scaled[i][j]);
				}
				
			}
			   //Draw image centered in the middle of the panel    
			  
			   g.drawImage (img, 0, 0, this);
	    } // paintComponent

	}
	
	public static void main(String[] args) {
		DotPlot dp = new DotPlot();
		ReverseCuthillMcKee rmck = new ReverseCuthillMcKee();
		
		dp.dotPlot(SparseMatrix.readMTX(args[0]));
	}
}
