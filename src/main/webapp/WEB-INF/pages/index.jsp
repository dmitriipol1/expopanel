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
    <meta http-equiv="refresh" content="30"/>
    <title>EXPO</title>
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
                <div align="right"><a href="/">бэкапы layout в: ${target}/ExpoBackup</a></div>
                <div align="left"><a href="showOnline">Показывать все серверы / только онлайн</a></div>
                <div align="right"><a href="addNewServer">Добавить сервер</a></div>
            </h3>
        </div>
        <div class="panel-body">
            <c:if test="${empty mashinesList}">
                Всё выключено!!
            </c:if>
            <c:if test="${not empty mashinesList}">

                <table id="myTable" class="table table-bordered table-hover">
                    <thead style="background-color: #bce8f1;">
                    <tr>
                        <th scope="col">Название</th>
                        <th scope="col">online</th>
                        <th scope="col">modules</th>
                        <th scope="col">VVVV</th>
                        <th scope="col">content</th>
                        <th scope="col">Global</th>

                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${mashinesList}" var="server">
                        <tr>
                            <td>
                                <button type="button" class="btn btn-${server.online==true? "success":"danger"}">
                                    <a href="file://////${server.name}/expo">${server.name}</a></button>
                                    ${server.kinect==true? " Kinect":""}
                            </td>
                            <td>${server.online}</td>
                            <td class="td-type${server.isModulesLoaded()}">
                                <a href="uploadModules?name=<c:out value='${server.name}'/>">Upload Modules</a></td>
                            <td class="td-type${server.isVVVVLoaded()}">
                                <a href="uploadVVVV?name=<c:out value='${server.name}'/>">Upload VVVV</a></td>
                            <td class="td-type${server.isContentLoaded()}">
                                <a href="uploadContent?name=<c:out value='${server.name}'/>">Upload Content</a></td>
                            <td><a href="uploadAll?name=<c:out value='${server.name}'/>">Upload All</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
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
</body>
</html>
