package br.com.cadproduto.ui.util;

import totalcross.io.IOException;
import totalcross.ui.Button;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

public class ButtonUtil {

	public static final String IMG_PATH = "br/com/cadproduto/resources";

	  public static final int BUTTON_NOVO = 100001;
	  public static final int BUTTON_VER = 100002;
	  public static final int BUTTON_EDITAR = 100003;
	  public static final int BUTTON_REMOVER = 100004;
	  public static final int BUTTON_SALVAR = 100005;
	  public static final int BUTTON_CANCELAR = 100006;
	  public static final int BUTTON_FOTO = 100007;
	  public static final int BUTTON_ATUALIZA = 100008;
	  
	  public static final Button getButton(String label, String imgPath) {
		    Image img = null;
		    Button btn = null;

		    try {
		      img = new Image(imgPath);
		      img.transparentColor = Color.BLACK;
		      btn = new Button(label, img, Button.BOTTOM, 0);
		      btn.setFont(Font.getFont(false, Font.MIN_FONT_SIZE + 2));
		      btn.setBorder(Button.BORDER_SIMPLE);
		    }
		    catch (ImageException e) {
		      btn = new Button(label);
		    }
		    catch (IOException e) {
		      btn = new Button(label);
		    }

		    return btn;

		  }
	  
	  	private ButtonUtil() {}
	  
}
