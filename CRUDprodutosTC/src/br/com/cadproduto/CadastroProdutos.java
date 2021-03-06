package br.com.cadproduto;

import totalcross.sys.Settings;
import totalcross.ui.MainWindow;
import totalcross.ui.MenuBar;
import totalcross.ui.MenuItem;
import totalcross.ui.UIColors;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import br.com.cadproduto.ui.ListaProdutosContainer;

public class CadastroProdutos extends MainWindow {

	public CadastroProdutos() {
	    super("Cadastro Produtos", BORDER_LOWERED);
	    setBackColor(UIColors.controlsBack = Color.brighter(Color.BRIGHT,
	        Color.LESS_STEP));
	    setUIStyle(Settings.Vista);
	  }
	
	public void initUI() {
	    add(new ListaProdutosContainer(), LEFT, TOP, FILL, FILL);
	    setMenuBar(new MenuBar(new MenuItem[][] { new MenuItem[] {
	        new MenuItem("Arquivo"), new MenuItem("Sair") } }));

	  }
	
	public void onEvent(Event event) {
		 switch (event.type) {
	      case ControlEvent.WINDOW_CLOSED: {
	        if (event.target == menubar) {
	          switch (((MenuBar) menubar).getSelectedIndex()) {
	            case 1: // sair
	              exit(0);
	              break;
	          }
	        }
	        break;
	      }
	    }
	}
}
