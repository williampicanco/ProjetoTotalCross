package br.com.appTarefas.ui;

import totalcross.io.IOException;
import totalcross.io.IllegalArgumentIOException;
import totalcross.io.Stream;
import totalcross.net.HttpStream;
import totalcross.net.URI;
import totalcross.ui.Container;
import totalcross.ui.ListBox;
import totalcross.xml.SyntaxException;
import totalcross.xml.XmlTokenizer;

public class ServerContainer extends Container implements Runnable {

	private static final class XmlInfo extends XmlTokenizer {

	    private ListBox lbLog;
	    private String ultimaTag;

	    public XmlInfo(ListBox lbLog, Stream stream) throws SyntaxException,
	        IOException
	    {
	      this.lbLog = lbLog;
	      tokenize(stream);
	    }

	    private void doLog(String name, String info) {
	      lbLog.add(name + ": " + info);
	    }

	    protected void foundAttributeName(byte[] input, int offset, int count) {
	      doLog("Attributo", new String(input, offset, count));
	    }

	    protected void foundAttributeValue(byte[] input, int offset, int count,
	        byte dlm)
	    {
	      doLog("Valor", new String(input, offset, count));
	    }

	    protected void foundEndEmptyTag() {
	      doLog("Fim", ultimaTag);
	    }

	    protected void foundEndTagName(byte[] input, int offset, int count) {
	      doLog("Fim", new String(input, offset, count));
	    }

	    protected void foundStartTagName(byte[] input, int offset, int count) {
	      ultimaTag = new String(input, offset, count);
	      doLog("Tag", ultimaTag);
	    }

	}
	
	private boolean estaRodando = true;
	
	private ListBox lbLog;
	
	public void initUI() {
		    // TODO Auto-generated method stub
		    super.initUI();
		    add(lbLog = new ListBox(), LEFT, TOP, FILL, FILL);

		    new Thread(this).start();
	}
	
	public void run() {

	    while (estaRodando) {
	      try {
	        lbLog.removeAll();
	        new XmlInfo(lbLog, new HttpStream(new URI(
	            "http://localhost/ServerTC/exporta.jsp")));
	      }
	      catch (IllegalArgumentIOException e) {
	        e.printStackTrace();
	      }
	      catch (SyntaxException e) {
	        e.printStackTrace();
	      }
	      catch (IOException e) {
	        e.printStackTrace();
	      }
	    }

	  }
		
}
