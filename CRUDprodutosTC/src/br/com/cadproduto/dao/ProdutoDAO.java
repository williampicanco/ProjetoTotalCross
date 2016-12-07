package br.com.cadproduto.dao;

import litebase.AlreadyCreatedException;
import litebase.LitebaseConnection;
import litebase.PreparedStatement;
import litebase.ResultSet;
import totalcross.util.Vector;
import br.com.cadproduto.bean.Produto;

/**
 * Classe respons�vel por persistir e recuperar os produtos do SGBD
 * @author William Pican�o
 */
public class ProdutoDAO {

	 /** Singleton */
	  public static final ProdutoDAO INSTANCE = new ProdutoDAO();
	  
	  /** Pega a conex�o com o SGBD */
	  private final LitebaseConnection CON;

	  private final PreparedStatement pStmtInserir;
	  private final PreparedStatement pStmtAtualizar;
	  private final PreparedStatement pStmtRemover;
	  private final PreparedStatement pStmtTodos;
	  private final PreparedStatement pStmtPorId;
	  private final PreparedStatement pStmtGeraId;
	  private final PreparedStatement pStmtFoto;


	  private ProdutoDAO() {

		    CON = LitebaseConnection.getInstance("JVMG");

		    try {
		      CON.execute("create table prod (id int primary key, nome char(40), "
		          + "categoria char(40), " + "foto blob(16384))");
		    }
		    catch (AlreadyCreatedException e) {}

		    pStmtInserir = CON
		        .prepareStatement("insert into prod (id, nome, categoria, foto) values(?,?,?,?)");
		    pStmtAtualizar = CON
		        .prepareStatement("update prod set nome = ?, categoria = ?, foto = ? where id = ?");
		    pStmtRemover = CON.prepareStatement("delete from prod where id = ? ");
		    pStmtTodos = CON.prepareStatement("select id, nome, categoria from prod");
		    pStmtPorId = CON
		        .prepareStatement("select id, nome, categoria from prod where id = ? ");
		    pStmtGeraId = CON.prepareStatement(" select max(id) as vId from prod ");
		    pStmtFoto = CON.prepareStatement("select foto from prod where id = ? ");

		  }
	  
		 public boolean atualizar(Produto p) {
	
			    if (p.id <= 0) { return inserir(p); }
	
			    pStmtAtualizar.clearParameters();
			    pStmtAtualizar.setString(0, p.nome);
			    pStmtAtualizar.setString(1, p.categoria);
			    pStmtAtualizar.setBlob(2, p.foto);
			    pStmtAtualizar.setInt(3, p.id);
	
			    return pStmtAtualizar.executeUpdate() > 0;
		  }
		  
		  public byte[] carregaFoto(Produto p) {
			    pStmtFoto.clearParameters();
			    pStmtFoto.setInt(0, p.id);
			    ResultSet res = pStmtFoto.executeQuery();
			    res.beforeFirst();

			    while (res.next()) {
			      return res.getBlob(1);
			    }

			    return null;

			  }
		  
		  private Produto doResultSetParaObjeto(ResultSet res) {
			    // A imagem ser� carregada sob demanda
			    return new Produto(res.getInt(1), res.getString(2), res.getString(3),
			        null);
			  }
		  
		  public boolean inserir(Produto p) {

			    if (p.id > 0) { return atualizar(p); }

			    p.id = proximoId();

			    pStmtInserir.clearParameters();
			    pStmtInserir.setInt(0, p.id);
			    pStmtInserir.setString(1, p.nome);
			    pStmtInserir.setString(2, p.categoria);
			    pStmtInserir.setBlob(3, p.foto);

			    return pStmtInserir.executeUpdate() > 0;

			  }

		  public int proximoId() {
			    ResultSet res = pStmtGeraId.executeQuery();
			    res.beforeFirst();
			    while (res.next()) {
			      return res.getInt("vId") + 1;
			    }
			    return 1;
			  }
	  
		  public boolean remover(Produto p) {
			    pStmtRemover.clearParameters();
			    pStmtRemover.setInt(0, p.id);
			    return pStmtRemover.executeUpdate() > 0;
			  }

		  public Produto selecionarPeloId(int id) {

			    pStmtPorId.clearParameters();
			    pStmtPorId.setInt(0, id);
			    ResultSet res = pStmtPorId.executeQuery();
			    res.beforeFirst();

			    while (res.next()) {
			      return doResultSetParaObjeto(res);
			    }

			    return null;
			  }
		  
		  public Vector selecionarTodos() {

			    ResultSet res = pStmtTodos.executeQuery();
			    Vector v = new Vector(res.getRowCount());

			    res.beforeFirst();

			    while (res.next()) {
			      v.addElement(doResultSetParaObjeto(res));
			    }

			    return v;
			  }

}
