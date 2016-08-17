package io;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.awt.Image;
import java.awt.image.*;
import java.awt.Toolkit;

/**
 * JarResources: 
 * maps all resources included in a Zip or Jar file. 
 */
public final class JarResources 
    {

    // external debug flag
    public boolean debugOn=true;

	/**
	 * 
	 * @uml.property name="htSizes"
	 * @uml.associationEnd multiplicity="(0 1)" qualifier="getName:java.lang.String java.lang.Integer"
	 */
	// jar resource mapping tables
	private Hashtable htSizes = new Hashtable();

	/**
	 * 
	 * @uml.property name="htJarContents"
	 * @uml.associationEnd multiplicity="(0 1)" qualifier="name:java.lang.String [B"
	 */
	private Hashtable htJarContents = new Hashtable();

    // a jar file
    private String jarFileName;

    /**
      * creates a JarResources. It extracts all resources from a Jar
      * into an internal hashtable, keyed by resource names.
      * @param jarFileName a jar or zip file
      */
    public JarResources(String jarFileName)
	{
	this.jarFileName=jarFileName;
	init();
	}

//  ACCESSORS
    /**
      * Extracts a jar resource as a byte array.
      * @param name a resource name.
      */
  public final byte[] getAsResource(String name)
	{
	return (byte[])htJarContents.get(name);
	}

    /**
      * Extracts a jar resource as a property.
      * @param name a resource name.
      */
  public final Properties getAsProperty(String name)
	{
	Properties props = new Properties();
	try{
		props.load(new ByteArrayInputStream(getAsResource(name)));
	}catch(IOException ioe){}
	return props;
	}

    /**
      * Extracts a jar resource as an object.
      * @param name a resource name.
      */
  public final Object getAsObject(String name)
	{
	Object obj = null;
/*	try{
		byte[] classBytes = getAsResource(name);
		Class meta = ClassLoader.defineClass(name, classBytes, 0,
classBytes.length);
		obj = meta.newInstance();
	}catch(IllegalAccessException iae){}
	catch(InstantiationException ie){}*/
	return obj;
	}

   /**
      * Extracts a jar resource as an image.
      * @param name a resource name.
      */
  public final Image getAsImage(String name)
	{
	return Toolkit.getDefaultToolkit().createImage(getAsResource(name));
		}

   /**
      * Extracts a jar resource as a part of an image.
      * @param name a resource name.
      */
  public final Image getAsCropImage(String name,int cropX, int cropY, int
cropW, int cropH)
	{
	Image img = Toolkit.getDefaultToolkit().createImage( new
FilteredImageSource(getAsImage(name).getSource(), new CropImageFilter(cropX,
cropY, cropW, cropH)) );
	return img;
	}


    /** initializes internal hash tables with Jar file resources.  */
    private void init()
	{
	try
	    {
	    // extracts just sizes only. 
	    /* Old Local File Version
	    ZipFile zf=new ZipFile(jarFileName);
	    Enumeration e=zf.entries();
	    while (e.hasMoreElements())
		{
		ZipEntry ze=(ZipEntry)e.nextElement();

		if (debugOn)
		    {
		    System.out.println(dumpZipEntry(ze));
		    }

		htSizes.put(ze.getName(),new Integer((int)ze.getSize()));
		}
	    zf.close();

	    // extract resources and put them into the hashtable.
	    
	    
	    FileInputStream fis=new FileInputStream(jarFileName);
	    BufferedInputStream bis=new BufferedInputStream(fis);
	    */
	    
	    // Network Version CDR 
	    // Server relative Pfad
	    
	    BufferedInputStream bis=new
BufferedInputStream(getClass().getResourceAsStream(jarFileName));
	    ZipInputStream zis=new ZipInputStream(bis);
	    ZipEntry ze=null;
	    while ((ze=zis.getNextEntry())!=null)
		{
		if (ze.isDirectory())
		    {
		    continue;
		    }

		if (debugOn)
		    {
		    //System.out.println("ze.getName()="+ze.getName()+
			//	       ","+"getSize()="+ze.getSize() );
		    }

		int size=(int)ze.getSize();
		// -1 means unknown size.
		if (size==-1)
		    {
		    size=((Integer)htSizes.get(ze.getName())).intValue();
		    }

		htSizes.put(ze.getName(),new Integer(size));
		byte[] b=new byte[(int)size];
		int rb=0;
		int chunk=0;
		while (((int)size - rb) > 0)
		    {
		    chunk=zis.read(b,rb,(int)size - rb);
		    if (chunk==-1)
			{
			break;
			}
		    rb+=chunk;
		    }

		// add to internal resource hashtable
		htJarContents.put(ze.getName(),b);

		if (debugOn)
		    {
		    //'System.out.println( ze.getName()+"  rb="+rb+
			//		",size="+size+
			//		",csize="+ze.getCompressedSize() );
		    }
		}
	    }
	catch (NullPointerException e)
		{
		//System.out.println("done.");
		}
	catch (FileNotFoundException e)
	    {
	    e.printStackTrace();
	    }
	catch (IOException e)
	    {
	    e.printStackTrace();
	    }
	}

    /**
      * Dumps a zip entry into a string.
      * @param ze a ZipEntry
      */
    private String dumpZipEntry(ZipEntry ze)
	{
	StringBuffer sb=new StringBuffer();
	if (ze.isDirectory())
	    {
	    sb.append("d ");
	    }
	else
	    {
	    sb.append("f ");
	    }

	if (ze.getMethod()==ZipEntry.STORED)
	    {
	    sb.append("stored   ");
	    }
	else
	    {
	    sb.append("defalted ");
	    }

	sb.append(ze.getName());
	sb.append("	");
	sb.append(""+ze.getSize());
	if (ze.getMethod()==ZipEntry.DEFLATED)
	    {
	    sb.append("/"+ze.getCompressedSize());
	    }

	return (sb.toString());
	}

   

    }	// End of JarResources class.
    	


