package br.com.appTarefas.ui;

import totalcross.ui.Button;
import totalcross.ui.ComboBox;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.Grid;
import totalcross.ui.Label;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;

public class FormularioDados extends Container implements Runnable{

	  public static final int BOTAO_SALVAR = 10001;
	  public static final int BOTAO_LIMPAR = 10002;

	  private Edit edtNome;
	  private Edit edtFone;
	  private ComboBox cbxSexo;
	  private Button btnSalvar;
	  private Button btnLimpar;
	  private Grid grid;
	  
	  
	  public void initUI() {		    
		    //super.initUI();

		    add(new Label("Nome"), LEFT + 2, TOP);
		    add(edtNome = new Edit(), SAME, AFTER);

		    add(new Label("Telefone"), SAME, AFTER);
		    add(edtFone = new Edit("(99)9999-9999"), SAME, AFTER);

		    add(new Label("Sexo"), SAME, AFTER);
		    add(cbxSexo = new ComboBox(new String[] { "Masculino", "Feminino" }), SAME,
		        AFTER);

		    Button.commonGap = 2;
		    add(btnSalvar = new Button("Salvar"), RIGHT - 2, SAME);
		    add(btnLimpar = new Button("Limpar"), RIGHT - 2, BOTTOM);
		    Button.commonGap = 0;

		    add(grid = new Grid(new String[] { "Nome", "Telefone", "Sexo" }, new int[] {
		        -50, -30, -20 }, new int[] { LEFT, RIGHT, CENTER }, false), LEFT + 2,
		        AFTER + 2, FILL - 2, FIT, btnSalvar);

		    edtNome.setMaxLength(30);
		    edtNome.capitalise = Edit.ALL_UPPER;

		    edtFone.setMode(Edit.NORMAL, true);
		    edtFone.setValidChars(Edit.numbersSet);

		    cbxSexo.setSelectedIndex(0);

		    btnSalvar.setBorder(Button.BORDER_3D_VERTICAL_GRADIENT);
		    btnSalvar.setFont(Font.getFont(false, Font.NORMAL_SIZE - 2));
		    btnSalvar.appId = BOTAO_SALVAR;

		    btnLimpar.setBorder(Button.BORDER_3D_VERTICAL_GRADIENT);
		    btnLimpar.setFont(Font.getFont(false, Font.NORMAL_SIZE - 2));
		    btnLimpar.appId = BOTAO_LIMPAR;

		    new Thread(this).start();

		  }
	  	
	  public void onEvent(Event event) {
		    //super.onEvent(event);

		    switch (event.type) {
		      case ControlEvent.PRESSED:
		        switch (((Control) event.target).appId) {
		          case BOTAO_SALVAR: {

		            if ((edtNome.getLength() > 0) && (edtFone.getLength() > 0)
		                && (cbxSexo.getSelectedIndex() >= 0))
		            {
		              grid.add(new String[] { edtNome.getText(), edtFone.getText(),
		                  cbxSexo.getSelectedItem().toString() });
		              edtNome.setText(clearValueStr);
		              edtFone.setText(clearValueStr);
		              cbxSexo.setSelectedIndex(0);
		              grid.repaintNow();
		            }
		            else {
		              new MessageBox("Atenção", "Cadastro Incompleto!").popup();
		            }
		            break;
		          }
		          case BOTAO_LIMPAR: {
		            grid.clear();
		            grid.repaintNow();
		            break;
		          }
		        }
		        break;

		      default:
		        break;
		    }

		  }
	    
	  public void run() {}
	  
}
