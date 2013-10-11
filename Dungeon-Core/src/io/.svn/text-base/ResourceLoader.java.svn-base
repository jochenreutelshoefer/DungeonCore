package io;
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public final class ResourceLoader {

	private static ResourceLoader defaultResourceLoader;

	/**
	 * 
	 * @uml.property name="bufferSize" 
	 */
	private int bufferSize = 8192;

	public ResourceLoader() {
	}
	public static ResourceLoader getDefaultResourceLoader() {
		if (defaultResourceLoader == null) {
			defaultResourceLoader = new ResourceLoader();
		}
		return defaultResourceLoader;
	}

	/**
	 * 
	 * @uml.property name="bufferSize"
	 */
	public void setBufferSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Illegal buffersize: " + size);
		}
		bufferSize = size;
	}

	/**
	 * 
	 * @uml.property name="bufferSize"
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	public InputStream getResourceStream(URL url) throws IOException {
		return url.openStream();
	}
	public InputStream getResourceStream(File file) throws IOException {
		return new FileInputStream(file);
	}
	public InputStream getResourceStream(String packageName, String fileName)
		throws IOException {
		String resource = "/" + packageName.replace('.', '/') + "/" + fileName;
		return getResourceStream(resource);
	}
	public InputStream getResourceStream(String resource) throws IOException {
		InputStream in = getClass().getResourceAsStream(resource);
		if (in == null) {
			throw new IOException("Unknown resource: " + resource);
		}
		return in;
	}
	public InputStream getResourceStream(
		String packageName,
		String fileName,
		ClassLoader loader)
		throws IOException {
		String resource = "/" + packageName.replace('.', '/') + "/" + fileName;
		return getResourceStream(resource, loader);
	}
	public InputStream getResourceStream(String resource, ClassLoader loader)
		throws IOException {
		InputStream in = loader.getResourceAsStream(resource);
		if (in == null)
			throw new IOException("Unknown Resource: " + resource);
		return in;
	}
	protected byte[] loadResourceData(InputStream in) throws IOException {
		byte[] buffer = new byte[0];
		byte[] cache = new byte[bufferSize];
		while (true) {
			int len = in.read(cache);
			if (len <= 0) {
				break;
			}
			byte[] newBuffer = new byte[buffer.length + len];
			System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
			System.arraycopy(cache, 0, newBuffer, buffer.length, len);
			buffer = newBuffer;
		}
		in.close();
		return buffer;
	}
	public byte[] getResourceData(URL url) throws IOException {
		return loadResourceData(getResourceStream(url));
	}
	public byte[] getResourceData(File file) throws IOException {
		return loadResourceData(getResourceStream(file));
	}
	public byte[] getResourceData(String packageName, String fileName)
		throws IOException {
		return loadResourceData(getResourceStream(packageName, fileName));
	}
	public byte[] getResourceData(String resource) throws IOException {
		return loadResourceData(getResourceStream(resource));
	}
	public byte[] getResourceData(
		String packageName,
		String fileName,
		ClassLoader loader)
		throws IOException {
		return loadResourceData(
			getResourceStream(packageName, fileName, loader));
	}
	public byte[] getResourceData(String resource, ClassLoader loader)
		throws IOException {
		return loadResourceData(getResourceStream(resource, loader));
	}
	protected Image createImage(byte[] data) throws IOException {
		return Toolkit.getDefaultToolkit().createImage(data);
	}
	public Image getResourceImage(URL url) throws IOException {
		return createImage(getResourceData(url));
	}
	public Image getResourceImage(File file) throws IOException {
		return createImage(getResourceData(file));
	}
	public Image getResourceImage(String packageName, String fileName)
		throws IOException {
		return createImage(getResourceData(packageName, fileName));
	}
	public Image getResourceImage(String resource) throws IOException {
		return createImage(getResourceData(resource));
	}
	public Image getResourceImage(
		String packageName,
		String fileName,
		ClassLoader loader)
		throws IOException {
		return createImage(getResourceData(packageName, fileName, loader));
	}
	public Image getResourceImage(String resource, ClassLoader loader)
		throws IOException {
		return createImage(getResourceData(resource, loader));
	}
	public ImageIcon getResourceIcon(URL url) throws IOException {
		return new ImageIcon(getResourceImage(url));
	}
	public ImageIcon getResourceIcon(File file) throws IOException {
		return new ImageIcon(getResourceImage(file));
	}
	public ImageIcon getResourceIcon(String packageName, String fileName)
		throws IOException {
		return new ImageIcon(getResourceImage(packageName, fileName));
	}
	public ImageIcon getResourceIcon(String resource) throws IOException {
		return new ImageIcon(getResourceImage(resource));
	}
	public ImageIcon getResourceIcon(
		String packageName,
		String fileName,
		ClassLoader loader)
		throws IOException {
		return new ImageIcon(getResourceImage(packageName, fileName, loader));
	}
	public ImageIcon getResourceIcon(String resource, ClassLoader loader)
		throws IOException {
		return new ImageIcon(getResourceImage(resource, loader));
	}
	public String getResourceString(URL url) throws IOException {
		return new String(getResourceData(url));
	}
	public String getResourceString(File file) throws IOException {
		return new String(getResourceData(file));
	}
	public String getResourceString(String packageName, String fileName)
		throws IOException {
		return new String(getResourceData(packageName, fileName));
	}
	public String getResourceString(String resource) throws IOException {
		return new String(getResourceData(resource));
	}
	public String getResourceString(
		String packageName,
		String fileName,
		ClassLoader loader)
		throws IOException {
		return new String(getResourceData(packageName, fileName, loader));
	}
	public String getResourceString(String resource, ClassLoader loader)
		throws IOException {
		return new String(getResourceData(resource, loader));
	}
	public String getResourceEncodedString(URL url, String encoding)
		throws IOException {
		return new String(getResourceData(url), encoding);
	}
	public String getResourceEncodedString(File file, String encoding)
		throws IOException {
		return new String(getResourceData(file), encoding);
	}
	public String getResourceEncodedString(
		String packageName,
		String fileName,
		String encoding)
		throws IOException {
		return new String(getResourceData(packageName, fileName), encoding);
	}
	public String getResourceEncodedString(String resource, String encoding)
		throws IOException {
		return new String(getResourceData(resource), encoding);
	}
	public String getResourceEncodedString(
		String packageName,
		String fileName,
		ClassLoader loader,
		String encoding)
		throws IOException {
		return new String(
			getResourceData(packageName, fileName, loader),
			encoding);
	}
	public String getResourceEncodedString(
		String resource,
		ClassLoader loader,
		String encoding)
		throws IOException {
		return new String(getResourceData(resource, loader), encoding);
	}
}
