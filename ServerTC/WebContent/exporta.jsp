<%@page import="com.thoughtworks.xstream.XStream"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
  java.util.ArrayList list = new java.util.ArrayList();

  for (int i = 0; i < 100; i++) {

    list.add(new br.com.bean.Contato("Contato" + i, "Fone " + i,
        i % 2 == 0 ? "Masculino" : "Feminino"));

  }
  
  out.clear();
  XStream stream = new XStream();
  stream.processAnnotations(br.com.bean.Contato.class);
  stream.toXML(list, out);
  
%>