package service;

import java.util.Scanner;
import java.io.File;
import java.util.List;

import dao.LivroDAO;
import model.Livro;
import spark.Request;
import spark.Response;

public class LivroService {

    private LivroDAO livroDAO = new LivroDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;
    private final int FORM_ORDERBY_NOME = 2;
    private final int FORM_ORDERBY_AUTOR = 3;
    private final int FORM_ORDERBY_ANO = 4;

    public LivroService() {
        makeForm();
    }

    public void makeForm() {
        makeForm(FORM_INSERT, new Livro(), FORM_ORDERBY_NOME);
    }

    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Livro(), orderBy);
    }

    public void makeForm(int tipo, Livro livro, int orderBy) {
        String nomeArquivo = "form.html";
        form = "";
        try {
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String umLivro = "";
        if (tipo != FORM_INSERT) {
            umLivro += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umLivro += "\t\t<tr>";
            umLivro += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/livro/list/1\">Novo Livro</a></b></font></td>";
            umLivro += "\t\t</tr>";
            umLivro += "\t</table>";
            umLivro += "\t<br>";
        }

        if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/livro/";
            String name, buttonLabel, nome, autor;
            int ano;
            if (tipo == FORM_INSERT) {
                action += "insert";
                name = "Inserir Livro";
                nome = "";
                autor = "";
                ano = 0;
                buttonLabel = "Inserir";
            } else {
                action += "update/" + livro.getId_livro();
                name = "Atualizar Livro (ID " + livro.getId_livro() + ")";
                nome = livro.getNome();
                autor = livro.getAutor();
                ano = livro.getAno();
                buttonLabel = "Atualizar";
            }
            umLivro += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            umLivro += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umLivro += "\t\t<tr>";
            umLivro += "\t\t\t<td colspan=\"4\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
            umLivro += "\t\t</tr>";
            umLivro += "\t\t<tr><td colspan=\"4\" align=\"left\">&nbsp;</td></tr>";
            umLivro += "\t\t<tr>";
            umLivro += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\"" + nome + "\"></td>";
            umLivro += "\t\t\t<td>Autor: <input class=\"input--register\" type=\"text\" name=\"autor\" value=\"" + autor + "\"></td>";
            umLivro += "\t\t\t<td>Ano: <input class=\"input--register\" type=\"text\" name=\"ano\" value=\"" + (ano == 0 ? "" : ano) + "\"></td>";
            umLivro += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\"" + buttonLabel + "\" class=\"input--main__style input--button\"></td>";
            umLivro += "\t\t</tr>";
            umLivro += "\t</table>";
            umLivro += "\t</form>";
        } else if (tipo == FORM_DETAIL) {
            umLivro += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umLivro += "\t\t<tr>";
            umLivro += "\t\t\t<td colspan=\"4\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Livro (ID " + livro.getId_livro() + ")</b></font></td>";
            umLivro += "\t\t</tr>";
            umLivro += "\t\t<tr><td colspan=\"4\" align=\"left\">&nbsp;</td></tr>";
            umLivro += "\t\t<tr>";
            umLivro += "\t\t\t<td>&nbsp;Nome: " + livro.getNome() + "</td>";
            umLivro += "\t\t\t<td>Autor: " + livro.getAutor() + "</td>";
            umLivro += "\t\t\t<td>Ano: " + livro.getAno() + "</td>";
            umLivro += "\t\t\t<td>&nbsp;</td>";
            umLivro += "\t\t</tr>";
            umLivro += "\t</table>";
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }
        form = form.replaceFirst("<UM-PRODUTO>", umLivro);

        String list = "<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">";
        list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Livros</b></font></td></tr>\n" +
                "\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
                "\n<tr>\n" +
                "\t<td><a href=\"/livro/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
                "\t<td><a href=\"/livro/list/" + FORM_ORDERBY_NOME + "\"><b>Nome</b></a></td>\n" +
                "\t<td><a href=\"/livro/list/" + FORM_ORDERBY_AUTOR + "\"><b>Autor</b></a></td>\n" +
                "\t<td><a href=\"/livro/list/" + FORM_ORDERBY_ANO + "\"><b>Ano</b></a></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
                "</tr>\n";

        List<Livro> livros;
        if (orderBy == FORM_ORDERBY_ID)       livros = livroDAO.getOrderById();
        else if (orderBy == FORM_ORDERBY_NOME) livros = livroDAO.getOrderByNome();
        else if (orderBy == FORM_ORDERBY_AUTOR) livros = livroDAO.getOrderByAutor();
        else if (orderBy == FORM_ORDERBY_ANO)  livros = livroDAO.getOrderByAno();
        else                                   livros = livroDAO.get();

        int i = 0;
        String bgcolor;
        for (Livro l : livros) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "\n<tr bgcolor=\"" + bgcolor + "\">\n" +
                    "\t<td>" + l.getId_livro() + "</td>\n" +
                    "\t<td>" + l.getNome() + "</td>\n" +
                    "\t<td>" + l.getAutor() + "</td>\n" +
                    "\t<td>" + l.getAno() + "</td>\n" +
                    "\t<td align=\"center\"><a href=\"/livro/" + l.getId_livro() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\"><a href=\"/livro/update/" + l.getId_livro() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\"><a href=\"javascript:confirmarDeleteLivro('" + l.getId_livro() + "', '" + l.getNome() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "</tr>\n";
        }
        list += "</table>";
        form = form.replaceFirst("<LISTAR-PRODUTO>", list);
    }

    public Object insert(Request request, Response response) {
        String nome = request.queryParams("nome");
        String autor = request.queryParams("autor");
        int ano = Integer.parseInt(request.queryParams("ano"));

        Livro livro = new Livro(nome, autor, ano);
        String resp;

        if (livroDAO.insert(livro)) {
            resp = "Livro (" + nome + ") inserido!";
            response.status(201);
        } else {
            resp = "Livro (" + nome + ") não inserido!";
            response.status(404);
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Livro livro = livroDAO.get(id);

        if (livro != null) {
            response.status(200);
            makeForm(FORM_DETAIL, livro, FORM_ORDERBY_NOME);
        } else {
            response.status(404);
            String resp = "Livro " + id + " não encontrado.";
            makeForm();
            form = form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                    "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }

        return form;
    }

    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Livro livro = livroDAO.get(id);

        if (livro != null) {
            response.status(200);
            makeForm(FORM_UPDATE, livro, FORM_ORDERBY_NOME);
        } else {
            response.status(404);
            String resp = "Livro " + id + " não encontrado.";
            makeForm();
            form = form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                    "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }

        return form;
    }

    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Livro livro = livroDAO.get(id);
        String resp;

        if (livro != null) {
            livro.setNome(request.queryParams("nome"));
            livro.setAutor(request.queryParams("autor"));
            livro.setAno(Integer.parseInt(request.queryParams("ano")));
            livroDAO.update(livro);
            response.status(200);
            resp = "Livro (ID " + livro.getId_livro() + ") atualizado!";
        } else {
            response.status(404);
            resp = "Livro (" + id + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Livro livro = livroDAO.get(id);
        String resp;

        if (livro != null) {
            livroDAO.delete(id);
            response.status(200);
            resp = "Livro (" + id + ") excluído!";
        } else {
            response.status(404);
            resp = "Livro (" + id + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }
}
