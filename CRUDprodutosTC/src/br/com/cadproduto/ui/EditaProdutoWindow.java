package br.com.cadproduto.ui;

import totalcross.io.DataStream;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Settings;
import totalcross.ui.Button;
import totalcross.ui.ComboBox;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.Window;
import totalcross.ui.dialog.FileChooserBox;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.dialog.FileChooserBox.Filter;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.image.Image;
import br.com.cadproduto.bean.Produto;
import br.com.cadproduto.dao.ProdutoDAO;
import br.com.cadproduto.ui.util.ButtonUtil;

public class EditaProdutoWindow  extends Window implements Filter {

	public static final int MODO_INSERIR = 1;
	  public static final int MODO_ATUALIZAR = 2;
	  public static final int MODO_REMOVER = 3;
	  public static final int MODO_VER = 4;

	  private ImageControl img;
	  private Edit edtId;
	  private Edit edtNome;
	  private ComboBox cbxCategoria;
	  private int modo;
	  private String foto;
	  private Button btnSalvar, btnFoto;

	  public EditaProdutoWindow(Produto prod, int modo) {
	    this.appObj = prod;
	    this.modo = modo;
	    
	  }
	  
	  public boolean accept(File f) throws IOException {
		    return f != null && (f.isDir() || f.getPath().endsWith(".jpg"));
	  }
	  
	  private Label createLabel(String text) {
		    Label lbl = new Label(text);
		    lbl.setFont(Font.getFont(false, Font.MIN_FONT_SIZE + 4));
		    return lbl;
	  }
	  
	  public void initUI() {
		    Button btnCancelar;
		    Label lblFoto;
		    Container cnt;

		    add(createLabel("Id"), LEFT + 2, TOP + 2);
		    add(edtId = new Edit("999999"), SAME, AFTER, FILL - 2, PREFERRED);

		    add(createLabel("Produto"), SAME, AFTER + 2);
		    add(edtNome = new Edit(""), SAME, AFTER, FILL - 2, PREFERRED);

		    add(createLabel("Categoria"), SAME, AFTER + 2);
		    add(cbxCategoria = new ComboBox(new String[] { "Enlatados", "Limpeza",
		        "Gr�os", "Refrigerantes" }), SAME, AFTER, FILL - 2, PREFERRED);

		    add(lblFoto = createLabel("Foto"), SAME, AFTER + 2);

		    Button.commonGap = 2;
		    add(btnCancelar = ButtonUtil.getButton("Cancelar", ButtonUtil.IMG_PATH
		        + "/cancel.png"), RIGHT - 2, BOTTOM);
		    add(btnSalvar = ButtonUtil.getButton("Salvar", ButtonUtil.IMG_PATH
		        + "/save.png"), BEFORE - 2, SAME, SAME, SAME);
		    add(btnFoto = ButtonUtil.getButton("Foto", ButtonUtil.IMG_PATH
		        + "/pictures.png"), LEFT + 2, SAME, SAME, SAME);
		    Button.commonGap = 0;

		    cnt = new Container();
		    add(cnt, SAME, AFTER, FILL - 2, FIT - 2, lblFoto);
		    cnt.setBorderStyle(BORDER_SIMPLE);
		    cnt.add(img = new ImageControl(), LEFT, TOP, FILL, FILL);

		    edtId.setEditable(false);
		    edtId.setMode(Edit.CURRENCY);

		    img.setEventsEnabled(true);

		    btnSalvar.appId = ButtonUtil.BUTTON_SALVAR;
		    btnCancelar.appId = ButtonUtil.BUTTON_CANCELAR;
		    btnFoto.appId = ButtonUtil.BUTTON_FOTO;

		  }
	  
	  	private void onBtnFotoPressed(Event event) {
		    String image = null;
		    FileChooserBox fcb = new FileChooserBox("Selecionar Foto", new String[] {
		        "Selecionar", "Cancelar" }, this);

		    try {
		      fcb.mountTree(Settings.appPath, 1);
		    }
		    catch (IOException e) {
		      return;
		    }

		    fcb.popup();

		    try {
		      image = fcb.getAnswer().substring(0, fcb.getAnswer().length() - 1);
		      img.setImage(new Image(new File(image, File.READ_WRITE)));
		      img.repaintNow();
		      this.foto = image;
		    }
		    catch (Exception e) {
		      new MessageBox("Erro", "N�o Foi Poss�vel abrir o arquivo: " + image);
		    }

		  }
	  		
	  	private void onBtnSalvarPressed(Event event) {

	  		    if (!popularObjeto()) return;

	  		    switch (modo) {
	  		      case MODO_INSERIR: {
	  		        ProdutoDAO.INSTANCE.inserir(( Produto) appObj);
	  		        break;
	  		      }
	  		      case MODO_ATUALIZAR: {
	  		    	ProdutoDAO.INSTANCE.atualizar(( Produto) appObj);
	  		        break;
	  		      }
	  		      case MODO_REMOVER: {
	  		    	ProdutoDAO.INSTANCE.remover((Produto) appObj);
	  		        ((Produto) appObj).id = -1;
	  		        break;
	  		      }
	  		    }

	  		    unpop();

	  		  }
	  	
	  	public void onEvent(Event event) {

	  	    switch (event.type) {
	  	      case ControlEvent.PRESSED: {

	  	        switch (((Control) event.target).appId) {
	  	          case ButtonUtil.BUTTON_SALVAR: {
	  	            onBtnSalvarPressed(event);
	  	            break;
	  	          }
	  	          case ButtonUtil.BUTTON_CANCELAR: {
	  	            unpop();
	  	            break;
	  	          }
	  	          case ButtonUtil.BUTTON_FOTO: {
	  	            onBtnFotoPressed(event);
	  	            break;
	  	          }
	  	        }
	  	        break;
	  	      }
	  	    }
	  	  }
	  	
	  	
	  	protected void onPopup() {
	  	    initUI();
	  	    setRect(LEFT, TOP, FILL, FILL);
	  	    popularUI();
	  	  }
	  	
	  	private boolean popularObjeto() {

	  	    Produto p = (Produto) appObj;

	  	    if ((edtNome.getLength() <= 0) || (cbxCategoria.getSelectedIndex() < 0)) {

	  	      new MessageBox("Erro", "Voc� deve preencher corretamente o cadastro!")
	  	          .popup();
	  	      return false;
	  	    }

	  	    try {
	  	      p.id = Convert.toInt(edtId.getText());
	  	    }
	  	    catch (InvalidNumberException e) {}

	  	    p.nome = edtNome.getText();
	  	    p.categoria = cbxCategoria.getSelectedItem().toString();

	  	    try {
	  	      File file = new File(this.foto, File.READ_WRITE);
	  	      p.foto = new byte[file.getSize()];
	  	      new DataStream(file).readBytes(p.foto);
	  	    }
	  	    catch (Exception e) {}

	  	    return true;

	  	  }

	  	private void popularUI() {
	  	    switch (modo) {
	  	      case MODO_ATUALIZAR:
	  	      case MODO_REMOVER:
	  	      case MODO_VER: {
	  	        Produto p = (Produto) appObj;

	  	        edtId.setText(Convert.toString(p.id));
	  	        edtNome.setText(p.nome);
	  	        cbxCategoria.setSelectedItem(p.categoria);

	  	        try {
	  	          img.setImage(new Image(p.foto));
	  	          img.repaintNow();
	  	        }
	  	        catch (Exception e) {}

	  	        break;
	  	      }
	  	    }

	  	    switch (modo) {
	  	      case MODO_VER:
	  	      case MODO_REMOVER: {
	  	        edtId.setEnabled(false);
	  	        edtNome.setEnabled(false);
	  	        cbxCategoria.setEnabled(false);
	  	        btnFoto.setEnabled(false);
	  	        btnSalvar.setEnabled(modo == MODO_REMOVER);
	  	        break;
	  	      }
	  	    }

	  	  }
}
