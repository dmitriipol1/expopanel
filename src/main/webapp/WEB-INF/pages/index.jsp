<%--
  Created by IntelliJ IDEA.
  User: Crazz
  Date: 15.06.2018
  Time: 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=2">
    <meta http-equiv="refresh" content="30" />
    <title>Библиотека</title>
    <!-- Bootstrap CSS -->
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css">
    <link href="../../resources/layout.css" rel="stylesheet" type="text/css"/>

    <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
    <script src="http://cdn.datatables.net/1.10.11/js/jquery.dataTables.min.js"></script>
</head>
<body class=".container-fluid">
<div class="container-fluid myrow-container">
    <div class="panel panel-success">
        <div class="panel-heading">
            <h3 class="panel-title">
                <div align="left"><b><a href="/">Expo</a></b></div>
                <div align="right"><a href="changeTarget">Источник: ${target}</a></div>
                <div align="right"><a href="showOnline">Показывать все серверы / только онлайн</a></div>
                <div align="right"><a href="createBook">Добавить сервер</a></div>
            </h3>
        </div>
        <div class="panel-body">
            <c:if test="${empty mashinesList}">
                There are no Books
            </c:if>
            <c:if test="${not empty mashinesList}">

                <%--<form action="searchMashine">--%>
                <%--<div class="row">--%>
                <%--<div class="col-md-6">--%>
                <%--<div class="col-md-4">Search Mashine:</div>--%>
                <%--<div class="col-md-4"><input type="text" name="searchName" id="searchName"></div>--%>
                <%--</div>--%>
                <%--<div class="col-md-4"><input class="btn btn-success" type='submit' value='Search'/></div>--%>
                <%--</div>--%>
                <%--</form>--%>

                <table id="myTable" class="table table-bordered table-hover">
                    <thead style="background-color: #bce8f1;">
                    <tr>
                        <th scope="col">Название</th>
                        <th scope="col">online</th>
                            <%--<th scope="col">Автор</th>--%>
                            <%--<th scope="col">Описание</th>--%>
                            <%--<th scope="col">Год</th>--%>
                            <%--<th scope="col">Прочитана</th>--%>
                            <%--<th scope="col">Редактировать</th>--%>
                            <%--<th scope="col">Удалить</th>--%>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${mashinesList}" var="server">
                        <%--<c:if test="${server.online}">--%>
                        <tr>
                            <td>
                                <button type="button" class="btn btn-${server.online==true? "success":"danger"}">
                                    <a href="file://////${server.name}/expo">${server.name}</a></button>
                            </td>
                            <td>${server.online}</td>
                                <%--<td>${book.author}</td>--%>
                                <%--<td>${book.description}</td>--%>
                                <%--<td>${book.printYear}</td>--%>
                                <%--<th><a href="readBook?id=<c:out value='${book.id}'/>">${book.readAlready==true?"Да": "Нет"}</a></th>--%>
                                <%--<th><a href="editBook?id=<c:out value='${book.id}'/>">Edit</a></th>--%>
                                <%--<th><a href="deleteBook?id=<c:out value='${book.id}'/>">Delete</a></th>--%>
                        </tr>
                        <%--</c:if>--%>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>

    <script>
        $(document).ready(function () {
            $('#myTable').DataTable();
        });
    </script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>

    <%-- <script src="<c:url value="/resources/js/jquery-2.1.3.js"/>"></script>
    <script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
     --%>
</body>
</html>
