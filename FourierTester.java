package fft;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;

public class FourierTester extends JFrame {
	/*Size of transform*/
	static int N = 512;

	public FourierTester() {
		super("Fourier Transform");
		renderGui();
	}

	private void renderGui() {

		setPreferredSize(new Dimension(660, 590));
		setLayout(new FlowLayout());

		PlotPanel timePanel, freqPanel, freqPanelDFT, timePanelRAW;
		/* Configure custom plot panels */
		getContentPane().setBackground(Color.white);
		timePanelRAW = new PlotPanel(Color.black,
				"Raw Time Domain Signal (DC to Nyquist/2)",640, 130);
		timePanel = new PlotPanel(Color.black,
				"Windowed Time Domain Signal (Hanning)",640, 130);
		freqPanel = new PlotPanel(Color.black, "FFT Implementation",640, 130);
		freqPanelDFT = new PlotPanel(Color.black,
				"DFT Implementation (Reference)",640, 130);

		timePanel.showArea = false;

		freqPanel.centerZero = false;
		freqPanel.showArea = false;
		freqPanel.scaleFactor = 0.002 * (2048d / (double) N);

		freqPanelDFT.centerZero = false;
		freqPanelDFT.showArea = false;
		freqPanelDFT.scaleFactor = 0.002 * (2048d / (double) N);

		getContentPane().add(timePanelRAW);
		getContentPane().add(timePanel);
		getContentPane().add(freqPanelDFT);
		getContentPane().add(freqPanel);

		pack();
		setVisible(true);

		Complex complexTimeDomain[] = new Complex[N];
		
		/*Lazy execution on main thread, but since we just want to look
		 * its fine... :) 	 * */
		for (int loop = 0; loop < N; loop += 3) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			double timeDomain[] = new double[N];
			double timeDomainRaw[] = new double[N];

			/* Quick hack to be able to display windowed and un-widowed */
			for (int i = 0; i < N; i++) {
				timeDomain[i] = (Math.cos(2 * 3.1415 * ((double) loop / N)
						* (N / 2) * (double) i / ((double) N)));
				timeDomainRaw[i] = timeDomain[i];
			}

			timePanelRAW.setSamples(timeDomainRaw);

			/* Apply windowing function */
			timeDomain = hanning(timeDomain);

			for (int i = 0; i < N; i++) {
				complexTimeDomain[i] = new Complex(timeDomain[i], 0);
			}

			timePanel.setSamples(complexTimeDomain);
			/* Test the FFT */
			Complex resFFT[] = Fourier.FFT(complexTimeDomain);
			freqPanel.setSamplesMod(resFFT);
			/* Test the DFT */
			Complex resDFT[] = Fourier.DFT(complexTimeDomain);
			freqPanelDFT.setSamplesMod(resDFT);
		}

	}

	public static double[] hanning(double[] tdom) {
		for (int i = 0; i < tdom.length; i++) {
			tdom[i] *= 0.5
					* (1 - Math.cos((2 * Math.PI * i) / (tdom.length - 1)));
		}
		return tdom;
	}

	public static void main(String[] arrrrgs) {
		new FourierTester();
	}

}