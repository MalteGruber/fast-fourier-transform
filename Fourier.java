package fft;
public class Fourier {
	
	/*Use a simple DFT to verify our FFT implementation*/
	public static Complex[] DFT(Complex samp[]){
		
		Complex X[] = new Complex[samp.length];
		for (int k= 0; k < samp.length; k++) {
			X[k]=new Complex(0, 0);
			for (int i = 0; i < samp.length; i++) {
				X[k]=X[k].plus(samp[i].times((new Complex(0, -2*Math.PI*(double)i*(double)k/(double)samp.length)).exp()));
			}
		}
		return X;
	}
	/* The FFT implementation! :) */
	public static Complex[] FFT(Complex samp[]){
		int N=samp.length;
		
		//Trivial case: 
		if(N==1)
			return samp;

		Complex even[] = new Complex[N/2];
		Complex odd[] = new Complex[N/2];		
		
		//Divide the input into even and odd samples
		for (int i = 0; i < N/2; i++) {
			even[i]=samp[2*i];
			odd[i]=samp[2*i+1];
		}
		
		//Lets perform the sub fft:s
		Complex e[] = FFT(even);
		Complex o[] = FFT(odd);
		
		Complex freq[] = new Complex[N];
		
		/*For every frequency bin add the freq bins of the
		 * sub fft:s. This is the heart of the fft.. */
		for (int k = 0; k < N/2; k++) {	
			Complex W=(new Complex(0, -2*Math.PI*k/N)).exp();
			freq[k]=e[k].plus(o[k].times(W));
			
			//For the k>N/2 the twindle factor is negative (only change!)
			freq[k+N/2]=e[k].minus(o[k].times(W));		
		}			
		return freq;	
	}

}

