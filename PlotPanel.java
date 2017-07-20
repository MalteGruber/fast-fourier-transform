package fft;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class PlotPanel extends JPanel {
	
	double w=640,h=140;
	double plotH = h * 0.7;
	double samples[];
	double min, max;

	boolean centerZero = true;
	boolean showArea = true;
	Color color;
	public double scaleFactor = 1;
	String desc = "desc";

	public PlotPanel(Color color, String string,int w,int h) {

		super(new GridBagLayout());
		this.w=w;
		this.h=h;
		
		this.color = color;
		desc = string;
		samples = new double[1024];
		
		for (int i = 0; i < samples.length; i++) {
			samples[i] = Math.sin(10 * (double) i / (double) samples.length);
		}
		
		setPreferredSize(new Dimension((int) w, (int) h));
		setBackground(Color.white);
	}

	public void setSamples(double s[]) {
		this.samples = s;
		invalidate();
		repaint();
	}

	@Override
	public void paint(Graphics g2) {

		super.paint(g2);
		Graphics2D g = (Graphics2D) g2;
		g2.setColor(color);
		g2.drawRect(0, 0, (int) w - 1, (int) h - 1);

		g2.drawString(desc, 7, 14);
		int prevX = 0, prevY = 0;
		for (int i = 0; i < getLen(); i++) {
			int xPos = (int) (w * (double) i / (double) getLen());
			int yPos = (int) (scaleFactor * plotH * (double) samples[i] / 2 + offset());
			yPos = (int) (h - yPos);
			g2.drawLine(xPos, yPos, prevX, prevY);
			prevX = xPos;
			prevY = yPos;
		}
	}

	boolean showFullWindow = true;

	private int getLen() {
		if (showFullWindow)
			return samples.length;
		return (int) w;

	}

	private double offset() {
		if (centerZero)
			return h / 2;
		return 10;
	}

	public void setSamples(Complex[] s) {
		double d[] = new double[s.length];

		for (int i = 0; i < s.length; i++) {
			d[i] = s[i].real();
		}
		setSamples(d);

	}

	public void setSamplesMod(Complex[] s) {
		double d[] = new double[s.length];

		for (int i = 0; i < s.length; i++) {
			d[i] = s[i].mod();
		}
		setSamples(d);
	}

}
