package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainLayoutController {

	@FXML TextField usrKey;
	@FXML Button decrypt;
	String key;
	
	public static int randomRange(int n1, int n2) {
	    return (int)(Math.random() * (n2 - n1 + 1)) + n1;
	}
	
	@FXML private void initialize() {}
	
	public void onDecrypt() {
		key = usrKey.getText();
		String workingDir = System.getProperty("user.dir");
		System.out.println(workingDir);
		
		File folder = new File(workingDir);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> pathList = new ArrayList<>();
		
	    for (File f : listOfFiles) {
	      if (f.isFile()) {
	    	  if (f.getName().charAt(0) != '.') {
	    		  pathList.add(f.getPath());
	    		  System.out.println("File Found: " + pathList.get(pathList.size()-1));
	    	  } else {
	    		  System.out.println("Hidden File Was Skipped");
	    	  }
	      } else if (f.isDirectory()) {
	        System.out.println("Dir Skipped: " + f.getName());
	      }
	    }
		
	    byte[] fileData = new byte[10485760]; //Max 10MB
	    boolean limit = false;
	    int dotIndex;
	    String newFilePath, extension;
	    
		for (String filePath : pathList) {
			dotIndex = filePath.indexOf("_Encrypted.");
			newFilePath = filePath.substring(0, dotIndex);
			extension = filePath.substring(dotIndex+1);
			newFilePath += extension;
			if (!extension.equals("txt")) limit = true;
		
			String hashedKey = getMD5(key);
			
			System.out.println("Current file : " + filePath);
			System.out.println("New File Created : " + newFilePath);
			
			try {			
				FileInputStream fIn = new FileInputStream(filePath);
				FileOutputStream fOut = new FileOutputStream(newFilePath, false);
				long fileSize = getFileSize(filePath);
				System.out.println("fileSize : " + fileSize);
				if (fileSize < 1000) limit = false;
				
				fIn.read(fileData, 0, (int)fileSize);
				System.out.print("READ OK! ");
				
				//Encrypt
				if (limit) {
					for (int i=0; i<1000; i++) {
						fileData[i] -= hashedKey.charAt(i%32);
						fOut.write(fileData[i]);
					}
					fOut.write(fileData, 1000, (int)fileSize-1000);
				} else {
					for (int i=0; i<(int)fileSize; i++) {
						fileData[i] -= hashedKey.charAt(i%32);
						fOut.write(fileData[i]);
					}
				}
				
				System.out.println("DECRYPT OK!");
				System.out.println();
				
				fIn.close();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
		for (File f : listOfFiles) {
			System.out.println("Deleting Encrypted Files" + f.getPath());
			f.delete();
		}
	}
	
	public long getFileSize(String path) {
		File f = new File(path);
		return f.length();
	}
	public MainLayoutController() {
		
	}

	public String getMD5(String plaintext) {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			while (hashtext.length() < 32) {
			  hashtext = "0"+hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
}
