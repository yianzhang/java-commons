package me.yczhang.util;

import java.io.*;

public class SerDeUtil {

	public static byte[] serialize(Object obj) throws IOException {
		if (obj==null)
			return null;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		byte[] bs = baos.toByteArray();
		oos.close();
		baos.close();
		
		return bs;
	}
	
	public static Object deserialize(byte[] bs) throws IOException, ClassNotFoundException {
		if (bs==null || bs.length==0)
			return null;
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bs);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object obj = ois.readObject();
		ois.close();
		bais.close();
		
		return obj;
	}
	
}
