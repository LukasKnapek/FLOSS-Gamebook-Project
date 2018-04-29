package mabufudyne.gbdesigner.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {
	public static Object deepCopyObject(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			oos.flush();
			oos.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] byteData = bos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
		try {
			o = (Object) new ObjectInputStream(bais).readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return o;
	}
}
