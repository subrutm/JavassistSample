package sample.main;

import java.util.ArrayList;
import java.util.List;

import ij.ImageJ;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class CJavassistTest {
	public static void main(String [] args) {
		cp = ClassPool.getDefault();
		
		System.out.println("Hack start!!");

		try {
			//***************************************************
			// ij.IJを書き換えるテスト
			// 関数【public static void showStatus(String s)】が
			// コールされたら、ログを出力するように変更する
			//　※デフォルトでは出ない
			//***************************************************
			CtClass clazz_IJ = cp.get("ij.IJ");
			List<String> pramaterType_IJ = new ArrayList<String>();
			pramaterType_IJ.add("java.lang.String");
//			pramaterType_IJ.add(" java.lang.String"); //※エラーケースです
			CtMethod method_IJ = getMethod(clazz_IJ, "showStatus", pramaterType_IJ);

			if(method_IJ != null) {
				method_IJ.insertBefore("sample.patch.CPatchSamle.showStatus($1);");
				clazz_IJ.toClass();
			}
			
			//***************************************************
			// ij.ImageJを書き換えるテスト
			// 関数【void showStatus(String s)】が
			// コールされたら、ログを出力するように変更する
			//　※デフォルトでは出ない
			//***************************************************
			CtClass clazz_ImageJ = cp.get("ij.ImageJ");
			List<String> pramaterType_ImageJ = new ArrayList<String>();
			pramaterType_ImageJ.add("java.lang.String");
			CtMethod method_ImageJ = getMethod(clazz_ImageJ, "showStatus", pramaterType_ImageJ);

			if(method_ImageJ != null) {
				method_ImageJ.insertBefore("sample.patch.CPatchSamle.showStatus2($1);");
				clazz_ImageJ.toClass();
			}

			System.out.println("Hack Complete!!");
		} catch (Exception e) {
			outputExcepionLog(e);
		}
		
		ij = new ImageJ();
	}
	
	//メソッドインスタンスを取得する
	static CtMethod getMethod(CtClass baseClass, String methodName, List<String> paramTypeList) {
		CtMethod result = null;

		try {
			if(!paramTypeList.isEmpty()) {
				CtClass[] clazz = new CtClass[paramTypeList.size()]; 
	
				for(int i = 0; i < paramTypeList.size(); i++) {
					clazz[i] = cp.getCtClass(paramTypeList.get(i));
				}
				result = baseClass.getDeclaredMethod(methodName, clazz);
			} else {
				result = baseClass.getDeclaredMethod(methodName);
			}
		} catch (Exception e) {
			outputExcepionLog(e);
		}
		return result;
	}

	//Exception ログ出力　ユーティリティ
	static void outputExcepionLog(Exception e) {
		StringBuilder sb = new StringBuilder();
		Throwable t = e;
		
		do {
			sb.append("Exception Type: " + e.getClass().getName() + "\n");
			sb.append("Message: " + e.getMessage() + "\n");
			sb.append("StackTrace:\n");
			
			for( StackTraceElement ste : e.getStackTrace() )
				sb.append("\tat" + ste.toString() + "\n");
		
			t = t.getCause();
		} while (t != null);
		
		System.out.println(sb.toString());
	}
/*
	//これを有効にして実行すると、ImageJの書き換えが失敗する
	public static ImageJ getImageJIns() {
		return ij;
	}
*/
	static ClassPool cp;
	static ImageJ ij;
}
