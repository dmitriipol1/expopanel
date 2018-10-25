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
    <meta http-equiv="refresh" content="60"/>
    <title>EXPO</title>
    <!-- Bootstrap CSS -->
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="resources/bootstrap.min.css">
    <link rel="stylesheet" href="resources/jquery.dataTables.min.css">
    <link href="resources/layout.css" rel="stylesheet" type="text/css"/>

    <script src="resources/jquery-2.1.1.js"></script>
    <script src="resources/jquery.dataTables.min.js"></script>
</head>

<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">Expo</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"><a href="showOnline">Show all/online</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="ping">Ping = ${isPinging}</a></li>

                <li><a href="changeTarget">Change source(${target}/ExpoData/)</a></li>
                <li><a href="backup">Бэкап layout(${target}/ExpoBackup)</a></li>
                <li><a href="listSrv">Список серверов D://srv.txt</a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>

<body class=".container-fluid">
<div class="container-fluid myrow-container">
    <div class="panel panel-success">
        <div class="panel-body">
            <c:if test="${empty mashinesList}">
                Всё выключено!!
            </c:if>
            <c:if test="${not empty mashinesList}">

                <table id="myTable" class="table table-bordered table-hover">
                    <thead style="background-color: #bce8f1;">
                    <tr>
                        <th scope="col">Название</th>
                        <th scope="col">modules</th>
                        <th scope="col">VVVV</th>
                        <th scope="col">content</th>
                        <th scope="col">Global</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${mashinesList}" var="server">
                        <tr>
                            <td id="${server.online==true? "online":"offline"}">
                                <a href="file://////${server.name}/expo">${server.name} (${server.ip})</a>${server.kinect==true? " Kinect":""}
                            </td>
                            <td class="td-type${server.isModulesLoaded()}">
                                <a href="uploadModules?name=<c:out value='${server.name}'/>">${server.online==true? "Upload Modules":""}</a>
                            </td>
                            <td class="td-type${server.isVVVVLoaded()}">
                                <a href="uploadVVVV?name=<c:out value='${server.name}'/>">${server.online==true? "Upload VVVV":""}</a>
                            </td>
                            <td class="td-type${server.isContentLoaded()}">
                                <a href="uploadContent?name=<c:out value='${server.name}'/>">${server.online==true? "Upload Content":""}</a>
                            </td>
                            <td>
                                <a href="uploadAll?name=<c:out value='${server.name}'/>">${server.online==true? "Upload M&C":""}</a>
                            </td>
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
        $('#myTable').DataTable({
            paging: false
        });
    });
</script>


<script src="resources/jquery.min.js"></script>
<script src="resources/bootstrap.min.js"></script>
<script src="resources/jquery.dataTables.min.js"></script>
<script src="resources/dataTables.bootstrap.min.js"></script>
</body>
</html>
