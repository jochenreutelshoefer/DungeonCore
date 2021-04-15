package de.jdungeon.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class DeepCopyUtil {

		/**
		 * Returns a copy of the object, or null if the object cannot
		 * be serialized.
		 */
		public static Object copy(Object orig) {
			return null;
			// temporarily switched off for GWT spike
			/*
			Object obj = null;

			try {
				// Write the object out to a byte array
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos);
				out.writeObject(orig);
				out.flush();
				out.close();

				// Make an input stream from the byte array and read
				// a copy of the object back in.
				ObjectInputStream in = new ObjectInputStream(
						new ByteArrayInputStream(bos.toByteArray()));
				obj = in.readObject();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			catch(ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
			return obj;

			 */
		}


}
