package br.com.cadproduto.ui;

import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Grid;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;
import br.com.cadproduto.bean.Produto;
import br.com.cadproduto.dao.ProdutoDAO;
import br.com.cadproduto.ui.util.ButtonUtil;

public class ListaProdutosContainer extends Container {

private Grid grd;	
	
private Produto getProduto() {

    if (grd.getSelectedIndex() < 0) return null;

    Produto p = null;
    try {
      p = ProdutoDAO.INSTANCE.selecionarPeloId(Convert.toInt(grd
          .getSelectedItem()[0]));
      p.foto = ProdutoDAO.INSTANCE.carregaFoto(p);
    }
    catch (InvalidNumberException e) {

      new MessageBox("Erro", "C�digo do funcion�rio � inv�lido!").popup();
      return null;
    }

    return p;

  }

public void initUI() {

    Button btnNovo, btnVer, btnEditar, btnRemover, btnAtualiza;

    Button.commonGap = 2;

    add(btnRemover = ButtonUtil.getButton("Remover", ButtonUtil.IMG_PATH
        + "/remove.png"), RIGHT - 2, TOP + 2);

    add(btnEditar = ButtonUtil.getButton("Editar", ButtonUtil.IMG_PATH
        + "/edit.png"), BEFORE - 2, SAME, SAME, SAME);

    add(
        btnVer = ButtonUtil.getButton("Ver", ButtonUtil.IMG_PATH + "/view.png"),
        BEFORE - 2, SAME, SAME, SAME);

    add(btnNovo = ButtonUtil
        .getButton("Novo", ButtonUtil.IMG_PATH + "/add.png"), BEFORE - 2, SAME,
        SAME, SAME);

    add(btnAtualiza = ButtonUtil.getButton("Atualiza", ButtonUtil.IMG_PATH
        + "/reload.png"), LEFT + 2, SAME, SAME, SAME);

    add(grd = new Grid(new String[] { "C�digo", "Nome", "Categoria" }, new int[] {
        -15, -80, -50 }, new int[] { RIGHT, LEFT, LEFT }, false), LEFT + 2,
        AFTER + 2, FILL - 2, FILL + 2);

    Button.commonGap = 0;

    btnNovo.appId = ButtonUtil.BUTTON_NOVO;
    btnVer.appId = ButtonUtil.BUTTON_VER;
    btnEditar.appId = ButtonUtil.BUTTON_EDITAR;
    btnRemover.appId = ButtonUtil.BUTTON_REMOVER;
    btnAtualiza.appId = ButtonUtil.BUTTON_ATUALIZA;

    onBtnAtualizaPressed();

  }
private void onBtnAtualizaPressed() {
    Vector v = ProdutoDAO.INSTANCE.selecionarTodos();
    grd.clear();
    for (int i = 0; i < v.size(); i++) {
      grd.add(((Produto) v.items[i]).toGridLine());
    }
    grd.repaintNow();
  }
	
private void onBtnEditarPressed() {

    Produto p = getProduto();

    if (p == null) return;

    EditaProdutoWindow epw = new EditaProdutoWindow(p,
    		EditaProdutoWindow.MODO_ATUALIZAR);
    epw.popup();

    String[] linha = grd.getSelectedItem();

    if ((!p.nome.equals(linha[1]) || (!p.categoria.equals(linha[2])))) {
      grd.replace(p.toGridLine(), grd.getSelectedIndex());
      grd.repaintNow();
    }

  }

private void onBtnNovoPressed() {
	EditaProdutoWindow epw = new EditaProdutoWindow(new Produto(),
			EditaProdutoWindow.MODO_INSERIR);
    epw.popup();
    if ((((Produto) epw.appObj)).id > 0) {
      grd.add(((Produto) epw.appObj).toGridLine());
      grd.repaintNow();
    }
  }

private void onBtnRemoverPressed() {
    Produto p = getProduto();

    if (p == null) return;

    EditaProdutoWindow epw = new EditaProdutoWindow(p,
    		EditaProdutoWindow.MODO_REMOVER);
    epw.popup();

    if (p.id < 0) {
      grd.del(grd.getSelectedIndex());
      grd.repaintNow();
    }
  }

private void onBtnVerPressed() {

    Produto p = getProduto();

    if (p == null) return;

    new EditaProdutoWindow(p, EditaProdutoWindow.MODO_VER).popup();

  }

public void onEvent(Event event) {

    switch (event.type) {
      case ControlEvent.PRESSED: {
        switch (((Control) event.target).appId) {
          case ButtonUtil.BUTTON_NOVO: {
            onBtnNovoPressed();
            break;
          }
          case ButtonUtil.BUTTON_VER: {
            onBtnVerPressed();
            break;
          }
          case ButtonUtil.BUTTON_EDITAR: {
            onBtnEditarPressed();
            break;
          }
          case ButtonUtil.BUTTON_REMOVER: {
            onBtnRemoverPressed();
            break;
          }
          case ButtonUtil.BUTTON_ATUALIZA: {
            onBtnAtualizaPressed();
            break;
          }
        }
        break;
      }
    }

  }

}
