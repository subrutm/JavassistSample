package sample.patch;

public class CPatchSamle {
	CPatchSamle() {
	}
	
	public static void showStatus(String s) {
		System.out.println("ij.IJ - showStatus: " + s);
	}
	
	public static void showStatus2(String s) {
		System.out.println("ij.ImageJ - showStatus: " + s);
	}
}
